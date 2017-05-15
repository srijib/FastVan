package com.fast.van.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.fast.van.R;
import com.fast.van.callbacks.GCMTokenCallback;
import com.fast.van.transformer.Transformer;
import com.fast.van.utils.Log;

/**
 * Created by Amandeep Singh Bagli on 17/09/15.
 */
public class ActivitySignUpSignIn extends BaseActivity implements GCMTokenCallback{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.fastvan_activity_signup_or_signin);

findViewById(R.id.signin).setOnClickListener(this);
findViewById(R.id.signup).setOnClickListener(this);
        ImageView layout= (ImageView) findViewById(R.id.parentBgImageView);

        //  Picasso.with(activity).load(R.drawable.splash_screen).into(layout);
        Glide.with(activity)
                .load(R.drawable.splash)
                .fitCenter()
                .into(layout);
//        Intent intent = new Intent(activity, RegistrationIntentService.class);
//        startService(intent);

    }

    @Override
    public void onClickView(View v) {
        int id=v.getId();
        Intent intent;
        switch (id){
            case R.id.signup:
intent=new Intent(activity,ActivityRegistration.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                Transformer.rightToLeft(activity);
                break;
            case R.id.signin:
                intent=new Intent(activity,ActivityLogin.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                Transformer.rightToLeft(activity);

                break;
        }

    }
    @Override
    public void onGCMSuccess(String id) {
        Log.e(TAG, ":" + id);
    }

    @Override
    public void onGCMFailure() {
        Log.e(TAG,":FAILURE");
    }
}
