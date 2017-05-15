package com.fast.van.activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;


import com.fast.van.R;
import com.fast.van.callbacks.FastVanClickListener;
import com.fast.van.fonts.TypekitContextWrapper;
import com.fast.van.utils.BaseUtils;
import com.fast.van.utils.CommonData;
import com.fast.van.utils.Log;


import java.util.logging.LogRecord;

/**
 * Created by Amandeep Singh Bagli on 17/09/15.
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener, FastVanClickListener {
    protected AppCompatActivity activity;
    public String TAG = getClass().getCanonicalName();

    private Handler handler;
    private Runnable runnable;
    private boolean canClick;
    protected InputMethodManager imm;
    private static boolean isForeGround;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        canClick = true;
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                canClick = true;
            }
        };
        activity = this;


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // hideKeyboard();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //   hideKeyboard();
        isForeGround = true;
        checkNotification();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isForeGround = false;
    }

    @Override
    public void onClick(View v) {
        if (canClick) {
            canClick = false;
            onClickView(v);
            handler.postDelayed(runnable, 500);

        } else {
            Log.e(TAG, "Click Not allowed at the moment");
        }
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            //inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            Log.e(TAG, "getCurrentFocus:NULL");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        try {
            View view=getWindow().getCurrentFocus();
            if(view!=null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            Log.e(TAG, "NUll VIEW", e);
        }
    }

    public static boolean isForeGround() {
        Log.d("FastVan", "FOREGROUND: " + isForeGround);
        return isForeGround;
    }

    private void checkNotification() {
//      Toast.makeText(activity,"checkNotification",Toast.LENGTH_LONG).show();
        Log.e(TAG,"this is check Notification");
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            String orderId = extra.getString("orderId");
            if (orderId!=null&&!orderId.isEmpty())
                return;

        }

        Log.e(TAG,"this is check Notification Bundle=NULL");
        String orderId = CommonData.getNotificationOrderId(activity);
        if (orderId!=null&&!orderId.isEmpty()) {

            Intent intent = new Intent(activity, ActivityNewRequest.class);
            intent.putExtra("message", "New Booking");
            intent.putExtra("orderId", orderId);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        Log.e(TAG,"this is check Notification Bottom");
        BaseUtils.removeNotification(activity);
    }
}
