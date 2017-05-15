package com.fast.van.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.fast.van.R;


public class CommonDialog {


    private Activity activity;

    private ConfirmationDialogEventsListener confirmationDialogEventsListener;


    /**
     * @param act
     */
    public CommonDialog(Activity act) {
        activity = act;
    }

    public CommonDialog(Activity act,ConfirmationDialogEventsListener confirmationDialogEventsListener) {
        activity = act;
        this.confirmationDialogEventsListener=confirmationDialogEventsListener;
    }

    /**
     * @param activity
     * @return
     */
    public static CommonDialog With(Activity activity) {
        return new CommonDialog(activity);
    }
    public static CommonDialog with(Activity activity,ConfirmationDialogEventsListener confirmationDialogEventsListener) {
        return new CommonDialog(activity,confirmationDialogEventsListener);
    }
    /**
     * @param message
     */
    public void show(String message) {
        try {

            final Dialog dialog = new Dialog(activity,
                    R.style.Theme_AppCompat_Translucent);
            dialog.setContentView(R.layout.dialog_custom_msg);
            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            TextView textMessage = (TextView) dialog
                    .findViewById(R.id.textMessage);
            textMessage.setMovementMethod(new ScrollingMovementMethod());
            textMessage.setText(message);
            Button btnOk = (Button) dialog.findViewById(R.id.btnOk);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }

            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void show(String message, final ConfirmationDialogCodes listenercode) {
        try {

            final Dialog dialog = new Dialog(activity,
                    R.style.Theme_AppCompat_Translucent);
            dialog.setContentView(R.layout.dialog_custom_msg);
            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            TextView textMessage = (TextView) dialog
                    .findViewById(R.id.textMessage);
            textMessage.setMovementMethod(new ScrollingMovementMethod());
            textMessage.setText(message);
            Button btnOk = (Button) dialog.findViewById(R.id.btnOk);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    confirmationDialogEventsListener.OnOkButtonPressed(listenercode);
                }

            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
