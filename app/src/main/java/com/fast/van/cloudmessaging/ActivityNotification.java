package com.fast.van.cloudmessaging;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.fast.van.activities.ActivityHome;
import com.fast.van.activities.ActivityNewRequest;
import com.fast.van.activities.BaseActivity;
import com.fast.van.utils.Log;

/**
 * Created by Amandeep Singh Bagli on 23/10/15.
 */
public class ActivityNotification extends AppCompatActivity{

    boolean is=BaseActivity.isForeGround();
    @Override
    protected void onStart() {

        super.onStart();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.e("Nofitcation",""+is);

            Intent intent=new Intent(this,ActivityHome.class);
            startActivity(intent);

        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();


    }


    public void onClickView(View v) {

    }
}
