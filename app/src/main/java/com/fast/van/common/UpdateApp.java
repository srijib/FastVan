package com.fast.van.common;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.fast.van.callbacks.AppUpdateCallback;
import com.fast.van.dialogs.CommonDialog;
import com.fast.van.dialogs.ConfirmationDialog;
import com.fast.van.dialogs.ConfirmationDialogCodes;
import com.fast.van.dialogs.ConfirmationDialogEventsListener;
import com.fast.van.exception.FastVanRuntimeException;
import com.fast.van.loadingindicator.LoadingBox;
import com.fast.van.models.login.Login;

/**
 * Created by Amandeep Singh Bagli on 23/03/16.
 */
public class UpdateApp implements ConfirmationDialogEventsListener {
    private Activity activity;
    private Login login;
    private AppUpdateCallback appUpdateCallback;


    public UpdateApp(Activity activity, Login login) {
        this.activity = activity;
        this.login = login;
        try {
            appUpdateCallback = (AppUpdateCallback) activity;
        } catch (Exception e) {
            throw new FastVanRuntimeException("Illegal Argument Params, Implements AppUpdateCallback",e);
        }



        checkUpdate();

    }

    private void checkUpdate() {

        if(checkVersionCodeValidation(login.getStatusCode())){
            appUpdateCallback.onCancelUpdate(login,true);
        }

    }

    @Override
    public void OnOkButtonPressed(ConfirmationDialogCodes confirmationDialogCode) {
        switch (confirmationDialogCode) {
            case FORCE_UPDATE:
                appUpdateCallback.onForceUpdate();
                LoadingBox.dismissLoadingDialog();
                activity.startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.fastvan.driver")));
                //ErrorCodes.AppExitForUpdate(activity);
                break;
        }

    }

    @Override
    public void OnCancelButtonPressed(ConfirmationDialogCodes confirmationDialogCode) {
        switch (confirmationDialogCode) {
            case FORCE_UPDATE:
                appUpdateCallback.onCancelUpdate(login,false);
                break;
        }
    }

    public boolean checkVersionCodeValidation(String statusCode) {
        if (statusCode == null) {

            return true;
        }

        if (statusCode.equals("100")) {//force update
            CommonDialog.with(activity, this).show(login.getMessage(), ConfirmationDialogCodes.FORCE_UPDATE);
            return false;

        } else if (statusCode.equals("101")) {// normal update
            ConfirmationDialog.WithActivity(activity, this).show(login.getMessage(), "Update", "Cancel", ConfirmationDialogCodes.FORCE_UPDATE);
            return false;
        }

        return true;
    }
}
