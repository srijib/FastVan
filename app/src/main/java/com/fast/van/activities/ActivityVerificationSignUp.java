package com.fast.van.activities;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.fast.van.R;

/**
 * Created by Amandeep Singh Bagli on 07/11/15.
 */
public class ActivityVerificationSignUp extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.varification_dialog);
    }

    @Override
    public void onClickView(View v) {

    }
}
