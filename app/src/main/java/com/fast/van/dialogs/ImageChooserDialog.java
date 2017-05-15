package com.fast.van.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import android.widget.TextView;

import com.fast.van.R;


/**
 * Created by Amandeep Singh Bagli on 14/10/15.
 */
public class ImageChooserDialog {
    /**
     * @param act
     * @return
     */

    public static ConDialogActivity withActivity(Activity act) {


        return new ConDialogActivity(act, (ImageChooserDialogListener) act);
    }




    /**
     * Confirmation dialog class for activity
     */
    public static class ConDialogActivity {
        private Activity activity;
        private ImageChooserDialogListener confirmationDialogEvents;


        /**
         * @param activity
         */
        public ConDialogActivity(Activity activity, ImageChooserDialogListener confirmationDialogEvents) {
            this.activity = activity;
            this.confirmationDialogEvents = confirmationDialogEvents;
        }





        /**
         *
         * @param confirmationDialogCode
         */

        public void show(final ConfirmationDialogCodes confirmationDialogCode) {
            try {


                final Dialog dialog = new Dialog(activity,
                        R.style.Theme_AppCompat_Translucent);
                dialog.setContentView(R.layout.image_chooser_options_dialog);
                WindowManager.LayoutParams layoutParams = dialog.getWindow()
                        .getAttributes();
                layoutParams.dimAmount = 0.6f;
                dialog.getWindow().addFlags(
                        WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);




                TextView textMessage = (TextView) dialog
                        .findViewById(R.id.textMessage);
                //textMessage.setText(message);
                final Button btnCamera = (Button) dialog.findViewById(R.id.btnFromCamera);
                final Button btnGallery = (Button) dialog.findViewById(R.id.btnFromGallery);
                final Button btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                btnCamera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                        confirmationDialogEvents.fromCamera();
                    }
                });
                btnGallery.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        confirmationDialogEvents.fromGallery();
                    }
                });
   btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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


    public interface ImageChooserDialogListener extends ConfirmationDialogEventsListener{
        void fromCamera();
        void fromGallery();
    }
}
