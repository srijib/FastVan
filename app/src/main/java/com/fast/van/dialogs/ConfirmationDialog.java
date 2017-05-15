package com.fast.van.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.fast.van.R;


/**
 * Common Confirmation Dialog with callbacks for activity/Fragment
 */

public class ConfirmationDialog {

    /**
     * @param act
     * @return
     */
    public static <T extends ConfirmationDialogEventsListener>  ConDialogActivity WithActivity(T act) {


        return new ConDialogActivity((Activity)act,  act);
    }

    public static ConDialogActivity WithActivity(Activity activity,ConfirmationDialogEventsListener listener) {


        return new ConDialogActivity(activity,  listener);
    }
    /**
     * @param act
     * @return
     */
    public static ConDialogFragment WithFragment(Activity act) {

        return new ConDialogFragment(act);
    }

    /**
     * Confirmation dialog class for activity
     */
    public static class ConDialogActivity {
        private Activity activity;
        private ConfirmationDialogEventsListener confirmationDialogEvents;


        /**
         * @param activity
         */
        public  <T extends ConfirmationDialogEventsListener>  ConDialogActivity(Activity activity, T confirmationDialogEvents) {
            this.activity = activity;
            this.confirmationDialogEvents = confirmationDialogEvents;
        }


        /**
         * @param message
         * @param okButtonText
         * @param cancelButtonText
         * @param confirmationDialogCode
         */
        public void show(String message, String okButtonText, String cancelButtonText, final ConfirmationDialogCodes confirmationDialogCode) {
            try {

                final Dialog dialog = new Dialog(activity,
                        R.style.Theme_AppCompat_Translucent);
                dialog.setContentView(R.layout.logout_dialog);
                WindowManager.LayoutParams layoutParams = dialog.getWindow()
                        .getAttributes();
                layoutParams.dimAmount = 0.6f;
                dialog.getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

                TextView textHead = (TextView) dialog.findViewById(R.id.textHead);

                textHead.setVisibility(View.GONE);
                TextView textMessage = (TextView) dialog
                        .findViewById(R.id.textMessage);
                textMessage.setText(message);

                Button btnOk = (Button) dialog.findViewById(R.id.btnOk);

                if (!okButtonText.isEmpty())
                    btnOk.setText(okButtonText);

                Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                if (!cancelButtonText.isEmpty())
                    btnCancel.setText(cancelButtonText);

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        confirmationDialogEvents.OnOkButtonPressed(confirmationDialogCode);
                    }

                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        confirmationDialogEvents.OnCancelButtonPressed(confirmationDialogCode);
                    }

                });

                dialog.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        /**
         * @param message
         * @param confirmationDialogCode
         */

        public void show(String message, final ConfirmationDialogCodes confirmationDialogCode) {
            try {


                final Dialog dialog = new Dialog(activity,
                        R.style.Theme_AppCompat_Translucent);
                dialog.setContentView(R.layout.logout_dialog);
                WindowManager.LayoutParams layoutParams = dialog.getWindow()
                        .getAttributes();
                layoutParams.dimAmount = 0.6f;
                dialog.getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

                TextView textHead = (TextView) dialog.findViewById(R.id.textHead);

                textHead.setVisibility(View.GONE);
                TextView textMessage = (TextView) dialog
                        .findViewById(R.id.textMessage);
                textMessage.setText(message);

                Button btnOk = (Button) dialog.findViewById(R.id.btnOk);


                Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        confirmationDialogEvents.OnOkButtonPressed(confirmationDialogCode);
                    }

                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        confirmationDialogEvents.OnCancelButtonPressed(confirmationDialogCode);
                    }

                });

                dialog.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        /**
         * @param message
         * @param okButtonText
         * @param confirmationDialogEventsListener
         */
        public void show(String message, String okButtonText, final ConfirmationDialogEventsListener confirmationDialogEventsListener) {
            try {

                final Dialog dialog = new Dialog(activity,
                        R.style.Theme_AppCompat_Translucent);
                dialog.setContentView(R.layout.logout_dialog);
                WindowManager.LayoutParams layoutParams = dialog.getWindow()
                        .getAttributes();
                layoutParams.dimAmount = 0.6f;
                dialog.getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

                TextView textHead = (TextView) dialog.findViewById(R.id.textHead);

                textHead.setVisibility(View.GONE);
                TextView textMessage = (TextView) dialog
                        .findViewById(R.id.textMessage);
                textMessage.setText(message);

                Button btnOk = (Button) dialog.findViewById(R.id.btnOk);

                if (!okButtonText.isEmpty())
                    btnOk.setText(okButtonText);

                Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                btnCancel.setVisibility(View.GONE);

                View vertSeprater = (View) dialog.findViewById(R.id.vertSperater);
                vertSeprater.setVisibility(View.GONE);
            /*    if (!cancelButtonText.isEmpty())
                    btnCancel.setText(cancelButtonText);
*/
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        confirmationDialogEventsListener.OnOkButtonPressed(null);
                    }

                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        confirmationDialogEventsListener.OnCancelButtonPressed(null);
                    }

                });

                dialog.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * Confirmation dialog class for fragments
     */
    public static class ConDialogFragment {
        private Activity activity;
        private ConfirmationDialogEventsListener confirmationDialogEvents;


        /**
         * @param activity
         */
        public ConDialogFragment(Activity activity) {
            this.activity = activity;

        }


        /**
         * @param message
         * @param okButtonText
         * @param cancelButtonText
         * @param confirmationDialogCode
         */
        public void show(ConfirmationDialogEventsListener confirmationDialogEvent, String message, String okButtonText, String cancelButtonText, final ConfirmationDialogCodes confirmationDialogCode) {
            try {

                this.confirmationDialogEvents = confirmationDialogEvent;
                final Dialog dialog = new Dialog(activity,
                        R.style.Theme_AppCompat_Translucent);
                dialog.setContentView(R.layout.logout_dialog);
                WindowManager.LayoutParams layoutParams = dialog.getWindow()
                        .getAttributes();
                layoutParams.dimAmount = 0.6f;
                dialog.getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

                TextView textHead = (TextView) dialog.findViewById(R.id.textHead);

                textHead.setVisibility(View.GONE);
                TextView textMessage = (TextView) dialog
                        .findViewById(R.id.textMessage);
                textMessage.setText(message);

                Button btnOk = (Button) dialog.findViewById(R.id.btnOk);

                if (!okButtonText.isEmpty())
                    btnOk.setText(okButtonText);

                Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                if (!cancelButtonText.isEmpty())
                    btnCancel.setText(cancelButtonText);

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        confirmationDialogEvents.OnOkButtonPressed(confirmationDialogCode);
                    }

                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        confirmationDialogEvents.OnCancelButtonPressed(confirmationDialogCode);
                    }

                });

                dialog.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        /**
         * @param message
         * @param confirmationDialogCode
         */

        public void show(ConfirmationDialogEventsListener confirmationDialogEvent, String message, final ConfirmationDialogCodes confirmationDialogCode) {
            try {

                this.confirmationDialogEvents = confirmationDialogEvent;
                final Dialog dialog = new Dialog(activity,
                        R.style.Theme_AppCompat_Translucent);
                dialog.setContentView(R.layout.logout_dialog);
                WindowManager.LayoutParams layoutParams = dialog.getWindow()
                        .getAttributes();
                layoutParams.dimAmount = 0.6f;
                dialog.getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

                TextView textHead = (TextView) dialog.findViewById(R.id.textHead);

                textHead.setVisibility(View.GONE);
                TextView textMessage = (TextView) dialog
                        .findViewById(R.id.textMessage);
                textMessage.setText(message);

                Button btnOk = (Button) dialog.findViewById(R.id.btnOk);


                Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();

                        confirmationDialogEvents.OnOkButtonPressed(confirmationDialogCode);
                    }

                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        confirmationDialogEvents.OnCancelButtonPressed(confirmationDialogCode);
                    }

                });

                dialog.show();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
