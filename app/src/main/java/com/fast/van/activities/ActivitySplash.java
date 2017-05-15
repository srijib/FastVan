package com.fast.van.activities;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;

import com.fast.van.R;


import com.fast.van.background.UpdateDriverLocation;
import com.fast.van.callbacks.AppUpdateCallback;
import com.fast.van.callbacks.GCMTokenCallback;
import com.fast.van.cloudmessaging.RegistrationIntentService;
import com.fast.van.common.UpdateApp;
import com.fast.van.config.Constants;
import com.fast.van.dialogs.CommonDialog;
import com.fast.van.dialogs.ConfirmationDialogCodes;
import com.fast.van.loadingindicator.LoadingBox;
import com.fast.van.models.login.Login;
import com.fast.van.models.order.Order;
import com.fast.van.models.signup.Device;
import com.fast.van.models.signup.LatLong;
import com.fast.van.models.signup.UserData;
import com.fast.van.retrofit.RestClient;
import com.fast.van.testing.ImageChooserActivity;
import com.fast.van.testing.LocationActivity;
import com.fast.van.testing.MainActivity;
import com.fast.van.testing.SampleCirclesDefault;
import com.fast.van.transformer.Transformer;
import com.fast.van.utils.BaseUtils;
import com.fast.van.utils.CommonData;
import com.fast.van.utils.ErrorCodes;
import com.fast.van.utils.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.logging.LogRecord;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Amandeep Singh Bagli on 17/09/15.
 */
public class ActivitySplash extends LocationBaseActivity implements GCMTokenCallback, AppUpdateCallback {
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(activity, ActivitySignUpSignIn.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            Transformer.rightToLeft(activity);
        }
    };
    Handler handle = new Handler();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fastvan_activity_splash);

        String orderId = CommonData.getNotificationOrderId(activity);
        if (orderId != null && !orderId.isEmpty()) {
            Bundle extra = getIntent().getExtras();
            if (extra != null) {
                String orderId2 = extra.getString("orderId");
                if (orderId2 != null && !orderId2.isEmpty()) {

                } else {

                    getIntent().putExtra("message", "New Booking");
                    getIntent().putExtra("orderId", orderId);
                }
            }


        }
        //  handle.postDelayed(testing, 2000);
        //  handle.postDelayed(runnable,2000);
        // getOrder();
    }

    @Override
    public void onLocationUpdate(Location location) {
        Log.e(TAG, location.toString());
    }

    @Override
    public void onGoogleConnectionFailed(ConnectionResult connectionResult) {

    }


    Runnable testing = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(activity, ActivityHome.class);
            if (getIntent().getExtras() != null)
                intent.putExtras(getIntent().getExtras());
            startActivity(intent);

        }
    };

    @Override
    public void onResume() {
        super.onResume();


//        Intent intent = new Intent(activity, RegistrationIntentService.class);
//        startService(intent);

//            try {
//                Log.e("User", "URLDecoder:"+URLDecoder.decode(user.getAccessToken(), "UTF-8"));
//                Log.e("User", "URLEncoder:"+ URLEncoder.encode(user.getAccessToken(), "UTF-8"));
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//
//        }
//        else
//        Log.e("User","Data:NULL");


        BaseUtils.removeNotification(activity);

    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        boolean test = true;
        if (test) {
            String token = CommonData.getDeviceToken(this);
            Login user = CommonData.getSessionData(this);
            Log.e(TAG, "Token:" + token);
            if (user != null) {
                Log.e(TAG, "Data:" + user.toString());
                Log.e(TAG, "getAccessToken:" + user.getAccessToken());

                checkSession(user);
            } else {
                chooseOption();
            }
        }
        //startService();
    }

    private void chooseOption() {

        if (!checkPlayServices())
            return;

        handle.postDelayed(runnable, 1500);

    }

    private void checkSession(Login user) {


        if (!checkPlayServices())
            return;
        Device device = new Device(this);
        LatLong latLong;
        if (mCurrentLocation != null) {
            latLong = new LatLong(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        } else {
            latLong = new LatLong();
        }
        RestClient.getApiServiceForPojo().driverAccessTokenLogin(user.getAccessToken(), device.getDeviceType(), device.getDeviceToken(), latLong.toString(), Constants.APP_VERSION, new Callback<Login>() {
            @Override
            public void success(Login s, Response response) {
                Log.d("Success", "AccessToken Login:" + s.toString());

                new UpdateApp(activity, s);
            }

            @Override
            public void failure(RetrofitError error) {
                //ErrorCodes.checkCode(activity, error);
                chooseOption();


            }
        });
    }


    @Override
    public void onGCMSuccess(String id) {
        Log.e(TAG, ":" + id);
    }

    @Override
    public void onGCMFailure() {
        Log.e(TAG, ":FAILURE");
    }


    @Override
    public void onClickView(View v) {

    }

    private void startService1() {

        Intent tracking = new Intent(activity, UpdateDriverLocation.class);
        startService(tracking);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, activity,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
            }
            return false;
        }
        return true;
    }

    @Override
    public void onForceUpdate() {

    }

    @Override
    public void onCancelUpdate(Login userData, boolean isShowMessage) {
        CommonData.saveLoginData(activity, userData);

        Intent intent = new Intent(activity, ActivityHome.class);
        if (getIntent().getExtras() != null)
            intent.putExtras(getIntent().getExtras());
        startActivity(intent);
    }
}
