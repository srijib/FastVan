package com.fast.van.loadingindicator;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.drawable.AnimationDrawable;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.fast.van.R;
import com.fast.van.utils.Log;


public class LoadingBox {
    private static ProgressDialog progressDialog;

    /**
     * @param context
     * @param message
     */
    public static void showLoadingDialog(Activity context, String message) {

        if (message.isEmpty())
            message = "Loading...";

        if (isDialogShowing()) {
            dismissLoadingDialog();
        }
        if (context.isFinishing()) {
            return;
        }

        progressDialog = new ProgressDialog(context, R.style.Theme_AppCompat_Translucent);

        progressDialog.show();
        WindowManager.LayoutParams layoutParams = progressDialog.getWindow().getAttributes();
        layoutParams.dimAmount = 0.7f;
        progressDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.loading_box);

        ImageView loadingfastvan=(ImageView)progressDialog.findViewById(R.id.fastvanloading);
        AnimationDrawable rocketAnimation;
        loadingfastvan.setBackgroundResource(R.anim.fastvan_loading);
        rocketAnimation = (AnimationDrawable) loadingfastvan.getBackground();
        rocketAnimation.start();

//        ((ProgressWheel) progressDialog.findViewById(R.id.progress_wheel)).spin();
        TextView messageText = (TextView) progressDialog.findViewById(R.id.tvProgress);
        messageText.setText(message);


    }


    /**
     * Check if loading box showing or not
     *
     * @return
     */
    public static boolean isDialogShowing() {
        try {
            if (progressDialog == null) {
                return false;
            } else {
                return progressDialog.isShowing();
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Dismisses above loading dialog
     */
    public static void dismissLoadingDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            Log.e("e", "=" + e);
        }
    }

}
