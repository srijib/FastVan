package com.fast.van.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fast.van.R;

/**
 * Created by Amandeep Singh Bagli on 17/09/15.
 */
public class ActivitySignUp extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.fastvan_activity_signup);
        findViewById(R.id.signupwithemail).setOnClickListener(this);
        ImageView layout= (ImageView) findViewById(R.id.parentLayout);

      //  Picasso.with(activity).load(R.drawable.splash_screen).into(layout);
        Glide.with(activity)
                .load(R.drawable.splash)
                .fitCenter()
                .into(layout);
    }

    @Override
    public void onClickView(View v) {
        int id=v.getId();

        switch (id){
            case R.id.signupwithfb:
                break;
            case R.id.signupwithemail:
                Intent intent=new Intent(activity,ActivityRegistration.class);
                startActivity(intent);

                break;

        }
    }
}
