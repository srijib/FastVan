package com.fast.van.retrofit;

import android.app.Activity;
import android.widget.Toast;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;


/**
 * Class to handle the Errors caused during PROCESS OF API INJECTION INTO NETWORK
 * <p/>
 * Developer: Rishabh
 * 28/04/15.
 */
public class ApiErrorHandler implements ErrorHandler {

    private Activity activity;

    public ApiErrorHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public Throwable handleError(RetrofitError cause) {


        String errorMessage = "";

        switch (cause.getKind()) {

            case NETWORK:
                errorMessage = "PLEASE CHECK INTERNET CONNECTIVITY STATUS";
                break;

            case CONVERSION:
                errorMessage = "AN ERROR WAS PROCURED WHILE PARSING";
                break;

            case HTTP:
                errorMessage = "THE REMOTE SERVER COULD NOT RESPOND";
                break;

            case UNEXPECTED:
                errorMessage = "AN UNEXPECTED ERROR OCCURRED";
                break;
        }

        final String message = errorMessage;

        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(activity, message, Toast.LENGTH_LONG).show();
                //    CustomDialog.dismissProgressDialog();
                //     CustomDialog.showAlertDialog(activity, "ERROR", message);
            }
        });

        return new Exception(errorMessage);
    }
}
