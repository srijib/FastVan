package com.fast.van.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fast.van.R;
import com.fast.van.callbacks.AppUpdateCallback;
import com.fast.van.cloudmessaging.QuickstartPreferences;
import com.fast.van.cloudmessaging.RegistrationIntentService;
import com.fast.van.common.UpdateApp;
import com.fast.van.common.ValidationEditText;
import com.fast.van.common.Validator;
import com.fast.van.config.Constants;
import com.fast.van.dialogs.CommonDialog;
import com.fast.van.dialogs.ConfirmationDialog;
import com.fast.van.dialogs.ConfirmationDialogCodes;
import com.fast.van.dialogs.ConfirmationDialogEventsListener;
import com.fast.van.loadingindicator.LoadingBox;
import com.fast.van.models.ServiceReply;
import com.fast.van.models.login.Login;
import com.fast.van.models.signup.Device;
import com.fast.van.models.signup.LatLong;
import com.fast.van.retrofit.RestClient;
import com.fast.van.transformer.Transformer;
import com.fast.van.utils.CommonData;
import com.fast.van.utils.CommonUtils;
import com.fast.van.utils.ErrorCodes;
import com.fast.van.utils.InternetDetector;
import com.fast.van.utils.Log;
import com.google.android.gms.common.ConnectionResult;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Amandeep Singh Bagli on 17/09/15.
 */
public class ActivityLogin extends LocationBaseActivity implements ConfirmationDialogEventsListener, AppUpdateCallback {


    private Button passwordCross, phoneCross;
    private EditText phonenumber, password;
    private boolean isLoginButtonPressed;


    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fastvan_activity_login);
        TextView title = (TextView) findViewById(R.id.screenTitle);
        title.setText(getTitle());
        findViewById(R.id.forgetPassword).setOnClickListener(this);
        findViewById(R.id.backbutton).setOnClickListener(this);
        findViewById(R.id.signin).setOnClickListener(this);
        initScreen();
    }

    @Override
    public void onLocationUpdate(Location location) {
        Log.e(TAG, location.toString());
    }

    @Override
    public void onGoogleConnectionFailed(ConnectionResult connectionResult) {

    }


    private void initScreen() {
        ValidationEditText validationEditText = new ValidationEditText();
        phonenumber = (EditText) findViewById(R.id.mobilenumber);
        validationEditText.setValidationFilter(ValidationEditText.TextType.PhoneNumber, phonenumber);
        password = (EditText) findViewById(R.id.password);
        validationEditText.setValidationFilter(ValidationEditText.TextType.Password, password);
        passwordCross = (Button) findViewById(R.id.passwordCross);


        phoneCross = (Button) findViewById(R.id.phoneCross);
        setCrossClickListener();
        setTextChangeListeners();
        setFocusChangeListeners();


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.i(TAG, "GCM Registration Token: ActivityLogin");
                if (isLoginButtonPressed)
                    login();
            }
        };
        String token = CommonData.getDeviceToken(this);

//        Intent intent = new Intent(activity, RegistrationIntentService.class);
//        startService(intent);

    }


    public void setCrossClickListener() {
        phoneCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phonenumber.setText("");
            }
        });

        passwordCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password.setText("");
            }
        });

    }

    public void setFocusChangeListeners() {
        phonenumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View arg0, boolean focus) {
                if (focus && !phonenumber.getText().toString().isEmpty()) {
                    phoneCross.setVisibility(View.VISIBLE);
                    phonenumber.setError(null);
                } else {
                    phoneCross.setVisibility(View.GONE);
                }
            }
        });


        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View arg0, boolean focus) {
                if (focus && !password.getText().toString().isEmpty()) {
                    passwordCross.setVisibility(View.VISIBLE);
                    password.setError(null);

                } else {
                    passwordCross.setVisibility(View.GONE);
                }
            }
        });
    }


    public void setTextChangeListeners() {
        password.addTextChangedListener(new TextWatcher() {


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int
                    count) {

                if (s.toString().length() > 0) {

                    passwordCross.setVisibility(View.VISIBLE);
                } else {

                    passwordCross.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

                if (s.toString().length() > 0) {

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                password.setError(null);

            }
        });


        phonenumber.addTextChangedListener(new TextWatcher() {


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int
                    count) {

                if (s.toString().length() > 0) {

                    phoneCross.setVisibility(View.VISIBLE);
                } else {

                    phoneCross.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

                if (s.toString().length() > 0) {

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                phonenumber.setError(null);

            }
        });
    }

    private boolean validateform() {
        boolean validated = true;
        if (Validator.numberValidator(phonenumber)) {
            validated = false;
            phoneCross.setVisibility(View.GONE);
            phonenumber.setError(getString(R.string.phonerequired));

        }
//        if(Validator.textValidator(password)){
//            validated=false;
//            passwordCross.setVisibility(View.GONE);
//            password.setError(getString(R.string.passwordrequired));
//
//        }
        if (Validator.passwordValidator(password)) {
            validated = false;
            passwordCross.setVisibility(View.GONE);
            if (password.getText().toString().isEmpty())
                password.setError(getString(R.string.passwordrequired));
            else
                password.setError(getString(R.string.passwordlenghtrequired));

        }
        return validated;
    }

    @Override
    public void onClickView(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.forgetPassword:
                Intent intent = new Intent(activity, ActivityForgetPassword.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                Transformer.rightToLeft(activity);
                break;
            case R.id.backbutton:
                onBackPressed();
                break;
            case R.id.signin:
                login();

                break;
        }

    }


    private void login() {

        boolean valid = validateform();
        Log.d("validate", "login form: " + valid);
        if (!valid) {
            return;
        }
        isLoginButtonPressed = true;

        try {
            imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }


        LoadingBox.showLoadingDialog(activity, "Signing in...");

        if (InternetDetector.isInternetAvailable(activity)) {

            if (CommonUtils.getDeviceToken(activity).isEmpty()) {
                return;
            }


        } else {
            LoadingBox.dismissLoadingDialog();
            CommonDialog.With(activity).show(getString(R.string.interneterror));
            return;
        }


        LatLong latLong;
        if (mCurrentLocation != null) {
            latLong = new LatLong(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        } else {
            latLong = new LatLong();
        }
        final ConfirmationDialogEventsListener confirmationDialogEventsListener = this;

        Device device = new Device(this);

/*        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE );
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!statusOfGPS){
            LoadingBox.dismissLoadingDialog();
            ConfirmationDialog.WithActivity(this).show("Enable Location service to use this app. Go to settings", "Settings", "Cancel", ConfirmationDialogCodes.gpsSetting);
            return;
        }
*/
        Log.d(TAG, "getDeviceToken:" + device.getDeviceToken());
        RestClient.getApiServiceForPojo().driverPhoneLogin(phonenumber.getText().toString(), password.getText().toString(), device.getDeviceType(), device.getDeviceType(), device.getDeviceToken(), latLong.toString(), Constants.APP_VERSION, new Callback<Login>() {
            @Override
            public void success(Login s, Response response) {
                Log.d("Success", "Login:" + s.toString());
                //  CommonDialog.with(activity, confirmationDialogEventsListener).show(s.getMessage(), ConfirmationDialogCodes.signedIn);

                new UpdateApp(activity, s);
            }

            @Override
            public void failure(RetrofitError error) {
                LoadingBox.dismissLoadingDialog();
                ErrorCodes.checkCode(activity, error);


            }
        });
    }

    @Override
    public void OnOkButtonPressed(ConfirmationDialogCodes confirmationDialogCode) {
        switch (confirmationDialogCode) {
            case signedIn:
                Intent intent = new Intent(activity, ActivityHome.class);
                startActivity(intent);
                finish();
                break;
        }

    }

    @Override
    public void OnCancelButtonPressed(ConfirmationDialogCodes confirmationDialogCode) {

    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(activity, ActivitySignUpSignIn.class);
        startActivity(intent);
        Transformer.leftToRight(activity);
    }

    @Override
    public void onForceUpdate() {

    }

    @Override
    public void onCancelUpdate(Login userData, boolean isShowMessage) {
        CommonData.saveLoginData(activity, userData);
        LoadingBox.dismissLoadingDialog();
        Intent intent = new Intent(activity, ActivityHome.class);
        startActivity(intent);
        finish();
        Transformer.rightToLeft(activity);
    }
}
