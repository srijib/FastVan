package com.fast.van.activities;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fast.van.R;
import com.fast.van.callbacks.AppUpdateCallback;
import com.fast.van.cloudmessaging.QuickstartPreferences;
import com.fast.van.common.DebugLog;
import com.fast.van.common.SessionManager;
import com.fast.van.common.UpdateApp;
import com.fast.van.common.ValidationEditText;
import com.fast.van.common.Validator;
import com.fast.van.config.Constants;
import com.fast.van.dialogs.CommonDialog;
import com.fast.van.dialogs.CompanyNamesDialog;
import com.fast.van.dialogs.ConfirmationDialogCodes;
import com.fast.van.dialogs.ConfirmationDialogEventsListener;
import com.fast.van.dialogs.ServiceDialog;
import com.fast.van.dialogs.ServiceDialog.ServiceDialogListener;
import com.fast.van.dialogs.TruckTypeDialog;
import com.fast.van.dialogs.VehicleDialog;
import com.fast.van.loadingindicator.LoadingBox;
import com.fast.van.models.ServiceReply;
import com.fast.van.models.login.Login;
import com.fast.van.models.signup.CompanyList;
import com.fast.van.models.signup.LatLong;
import com.fast.van.models.signup.Registration;
import com.fast.van.models.signup.Service;
import com.fast.van.models.signup.Truck;
import com.fast.van.models.signup.UserData;
import com.fast.van.models.signup.Vehicle;
import com.fast.van.retrofit.RestClient;
import com.fast.van.transformer.Transformer;
import com.fast.van.utils.BaseUtils;
import com.fast.van.utils.CommonData;
import com.fast.van.utils.CommonUtils;
import com.fast.van.utils.ErrorCodes;
import com.fast.van.utils.InternetDetector;
import com.fast.van.utils.Log;
import com.fast.van.utils.file.AppFileUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * Created by Amandeep Singh Bagli on 17/09/15.
 */
public class ActivityRegistration extends ImageChooserBaseActivity implements ServiceDialogListener, VehicleDialog.VehicleDialogListener, TruckTypeDialog.TruckDialogListener, CompanyNamesDialog.CompanyDialogListener, ConfirmationDialogEventsListener, AppUpdateCallback {

    private EditText firstname, lastname, emailaddress, phonenumber, companyname, servicetype, vehicletype, trucktype, vehicalnumber, password, confirmpassword;
    private CheckBox iagree;
    private Location location1;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private boolean isSignupButtonPressed;
    private Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fastvan_activity_registration);

        TextView title = (TextView) findViewById(R.id.screenTitle);
        title.setText(getTitle());
        findViewById(R.id.backbutton).setOnClickListener(this);
        ImageView mImageView = (ImageView) findViewById(R.id.imgAvatar);
        Glide.with(activity)
                .load(R.drawable.icon_avatar_big)
                .fitCenter()
                .into(mImageView);

        initform();
        //  getDriverInfo();
    }

    @Override
    public void onLocationUpdate(Location location) {
        location1 = location;
        Log.e("Location", "Lat:" + location.getLatitude() + " long:" + location.getLongitude());

    }

    @Override
    public void onGoogleConnectionFailed(ConnectionResult connectionResult) {

    }

    private void initform() {
        ValidationEditText validationEditText = new ValidationEditText();
        firstname = (EditText) findViewById(R.id.firstname);
        //validationEditText.setValidationFilter(ValidationEditText.TextType.FirstName,firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        validationEditText.setValidationFilter(ValidationEditText.TextType.LastName, lastname);
        emailaddress = (EditText) findViewById(R.id.emailaddress);
        phonenumber = (EditText) findViewById(R.id.mobilenumber);//^(\+?27|0)[6-9][1-7][0-9]{7}$
        validationEditText.setValidationFilter(ValidationEditText.TextType.PhoneNumber, phonenumber);
        companyname = (EditText) findViewById(R.id.companyname);
        servicetype = (EditText) findViewById(R.id.servicetype);
        vehicletype = (EditText) findViewById(R.id.vehicaltype);
        trucktype = (EditText) findViewById(R.id.trucktype);
        vehicalnumber = (EditText) findViewById(R.id.vehicalnumber);
        validationEditText.setValidationFilter(ValidationEditText.TextType.VehicleNumber, vehicalnumber);
        password = (EditText) findViewById(R.id.password);
        validationEditText.setValidationFilter(ValidationEditText.TextType.Password, password);
        confirmpassword = (EditText) findViewById(R.id.confirmpassword);
        validationEditText.setValidationFilter(ValidationEditText.TextType.Password, confirmpassword);
        iagree = (CheckBox) findViewById(R.id.checkboxAgree);
        iagree.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    findViewById(R.id.textviewTermsAndConditions).requestFocus();

            }
        });
        findViewById(R.id.signup).setOnClickListener(this);
        findViewById(R.id.signin).setOnClickListener(this);
        findViewById(R.id.textviewTermsAndConditions).setOnClickListener(this);
        servicetype.setOnClickListener(this);
        vehicletype.setOnClickListener(this);
        trucktype.setOnClickListener(this);
        companyname.setOnClickListener(this);

        servicetype.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    servicetype.performClick();
            }
        });
        vehicletype.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    vehicletype.performClick();
            }
        });

        trucktype.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    trucktype.performClick();
            }
        });

        companyname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    companyname.performClick();
            }
        });

        findViewById(R.id.imgAvatar).setOnClickListener(this);
        //GPSTracker gps=new GPSTracker(activity);
    /*   SmartLocation.with(activity).location()
               .oneFix()
               .start(new OnLocationUpdatedListener() {
                   @Override
                   public void onLocationUpdated(Location location) {

                   }
               });*/


    /*   // check if GPS enabled
       if(gps.canGetLocation()){

           double latitude = gps.getLatitude();
           double longitude = gps.getLongitude();


           // \n is for new line
           Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
       }else{
           // can't get location
           // GPS or Network is not enabled
           // Ask user to enable GPS/network in settings
           gps.showSettingsAlert();
       } */


        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.i(TAG, "GCM Registration Token: ActivityLogin");
                if (isSignupButtonPressed)
                    registration();
            }
        };


        companyListDialog(false);
    }

    private boolean validateform() {
        if (location1 != null) {
            Log.e("Location", "Lat:" + location1.getLatitude() + " long:" + location1.getLongitude());
        } else {
            Log.e("Location", "No location");
        }
        boolean validated = true;
        boolean focus = false;
        if (Validator.nameValidator(firstname)) {
            validated = false;
            firstname.setError(getString(R.string.firstnamerequired));

            if (!focus) {
                focus = true;
                firstname.requestFocus();
            }

        }
        if (Validator.nameValidator(lastname)) {
            validated = false;
            lastname.setError(getString(R.string.lastnamerequired));
            if (!focus) {
                focus = true;
                lastname.requestFocus();
            }

        }
        if (!Validator.emailValidator(emailaddress)) {
            validated = false;
            emailaddress.setError(getString(R.string.emailrequired));
            if (!focus) {
                focus = true;
                emailaddress.requestFocus();
            }

        }
        if (Validator.numberValidator(phonenumber)) {
            validated = false;
            phonenumber.setError(getString(R.string.phonerequired));
            if (!focus) {
                focus = true;
                phonenumber.requestFocus();
            }

        }
        if (Validator.textValidator(companyname)) {
            validated = false;
            companyname.setError(getString(R.string.companynamerequired));
            if (!focus) {
                focus = true;
                companyname.requestFocus();
            }


        }
        if (Validator.textValidator(servicetype)) {
            validated = false;
            servicetype.setError(getString(R.string.servicetyperequired));
            if (!focus) {
                focus = true;
                servicetype.requestFocus();
            }

        }
        if (Validator.textValidator(vehicletype)) {
            validated = false;
            vehicletype.setError(getString(R.string.vehicalnumberrequired));
            if (!focus) {
                focus = true;
                vehicletype.requestFocus();
            }

        }
        if (vehicletype.getText().toString().contains("Truck")) {

            if (Validator.textValidator(trucktype)) {
                validated = false;
                trucktype.setError(getString(R.string.trucktyperequired));
                if (!focus) {
                    focus = true;
                    trucktype.requestFocus();
                }

            }
        }
        if (Validator.vehicleNumValidator(vehicalnumber)) {
            validated = false;
            vehicalnumber.setError(getString(R.string.vehicalnumberrequired));
            if (!focus) {
                focus = true;
                vehicalnumber.requestFocus();
            }

        }
        if (Validator.passwordValidator(password)) {
            validated = false;
            if (password.getText().toString().isEmpty())
                password.setError(getString(R.string.passwordrequired));
            else
                password.setError(getString(R.string.passwordlenghtrequired));
            if (!focus) {
                focus = true;
                password.requestFocus();
            }

        }

        if (Validator.passwordValidator(confirmpassword)) {
            validated = false;

            confirmpassword.setError(getString(R.string.confirmpasswordrequired));
            if (!focus) {
                focus = true;
                confirmpassword.requestFocus();
            }

        }
        if (!confirmpassword.getText().toString().contentEquals(password.getText().toString())) {

            validated = false;
            confirmpassword.setError(getString(R.string.confirmpasswordnotmatchedrequired));
            if (!focus) {
                focus = true;
                confirmpassword.requestFocus();
            }
        }

        if (!iagree.isChecked() && validated) {
            validated = false;
            CommonDialog.With(activity).show("Please accept Agreement");
        }


        return validated;
    }

    @Override
    public void onClickView(View v) {

        Intent intent = null;
        int id = v.getId();
        switch (id) {
            case R.id.signup:
                registration();

                break;

            case R.id.servicetype:
                ServiceDialog.WithActivity(this).Show(ConfirmationDialogCodes.serviceType);
                break;
            case R.id.vehicaltype:
                VehicleDialog.WithActivity(this).Show(ConfirmationDialogCodes.vehicleType);
                break;
            case R.id.trucktype:
                if (vehicletype.getText().toString().contains("Truck")) {
                    TruckTypeDialog.WithActivity(this).Show(ConfirmationDialogCodes.vehicleType);
                } else {
//                    Toast.makeText(activity, "Only for trucks", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.companyname:
                companyListDialog(true);

                break;
            case R.id.imgAvatar:
                chooseImageFrom((ImageView) findViewById(R.id.imgAvatar));
                break;
            case R.id.backbutton:
                onBackPressed();
                break;
            case R.id.signin:
                intent = new Intent(activity, ActivityLogin.class);
                startActivity(intent);
                finish();
                break;

            case R.id.textviewTermsAndConditions:
                intent = new Intent(activity, ActivityTermsAndConditions.class);
                startActivity(intent);
                Transformer.rightToLeft(activity);
                break;

        }

    }

    private static Gson GSON = new Gson();
    private ArrayList<Service> services;
    private ArrayList<Vehicle> vehicles;


    Registration registration;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        registration = new Registration(this);
    }

    private void registration() {


     /*   Intent intent=new Intent(activity,ActivityVerificationSignUp.class);
        startActivity(intent);
        Transformer.leftToRight(activity);
*/

        boolean valid = validateform();
        Log.d("validate", "form: " + valid);
        if (valid) {

            getValuesCallService();
        }

    }

    private void getValuesCallService() {
        try {

            LoadingBox.showLoadingDialog(activity, "Loading...");
            isSignupButtonPressed = true;

            if (InternetDetector.isInternetAvailable(activity)) {

                if (CommonUtils.getDeviceToken(activity).isEmpty()) {
                    return;
                }


            } else {
                LoadingBox.dismissLoadingDialog();
                CommonDialog.With(activity).show(getString(R.string.interneterror));
                return;
            }


            registration.setPhone(phonenumber.getText().toString());

            registration.setDeviceDetails(activity);

            if (location1 != null) {

                registration.setLatLong(new LatLong(location1.getLatitude(), location1.getLongitude()));
                Log.e("Location", "Lat:" + location1.getLatitude() + " long:" + location1.getLongitude());
            } else {
                registration.setLatLong(new LatLong());
                Log.e("Location", "No location");
            }

            Map<String, TypedFile> images = new HashMap<String, TypedFile>();
            if (getOriginalFilePath() != null)
                images.put("profilePicture", new TypedFile("image/*", new File(getOriginalFilePath())));

//Add truck type variable here+verify this variable in form


//            Toast.makeText(activity, "" + jsonArray.getJSONObject(0).getString("truckType"), Toast.LENGTH_SHORT).show();


            String truckvariavle;

            if (trucktype.length() == 0 || trucktype.equals("") || trucktype == null) {
                //EditText is empty
                truckvariavle = "";
            } else {
                //EditText is not empty
                JSONArray jsonArray = new JSONArray(registration.getTruck());
                truckvariavle = jsonArray.getJSONObject(0).getString("truckType");
            }
            DebugLog.console("Name: " + firstname.getText().toString() + " TruckType " + registration.getTruck() + " TruckType String" + truckvariavle);
            RestClient.getApiServiceForPojo().signUpEmail(new TypedString(firstname.getText().toString()), new TypedString(lastname.getText().toString()), new TypedString(emailaddress.getText().toString()), new TypedString(registration.getPhone()), new TypedString(companyname.getText().toString()), new TypedString(registration.getService()), new TypedString(registration.getVehicle()), new TypedString(truckvariavle), new TypedString(registration.getLatLong()), new TypedString(password.getText().toString()), new TypedString(vehicalnumber.getText().toString()), new TypedString(registration.getDeviceDetails()), new TypedString("" + registration.getAppVersion()), images, new Callback<UserData>() {
                @Override
                public void success(UserData userData, Response response) {
                    Log.d("Success", "" + userData.toString());
                    Log.d("Success", "AccessToken: " + userData.getAccessToken());
                    CommonData.saveUserData(getApplicationContext(), userData);
                    LoadingBox.dismissLoadingDialog();
                    varificationDialog();
                }

                @Override
                public void failure(RetrofitError error) {

                    LoadingBox.dismissLoadingDialog();

                    ErrorCodes.checkCode(activity, error);


                }
            });

        } catch (Exception ex) {
            DebugLog.console(ex, "[ActivityRegistration]:Exception inside getValuesCallService");
        }
    }

    @Override
    public void OnOkButtonPressed(ConfirmationDialogCodes confirmationDialogCode) {


        switch (confirmationDialogCode) {
            case registrationComplete:
                Intent intent = new Intent(activity, ActivityHome.class);
                startActivity(intent);
                finish();
                break;
        }

    }

    @Override
    public void OnCancelButtonPressed(ConfirmationDialogCodes confirmationDialogCode) {
        Log.e("services", "cancel:" + confirmationDialogCode);


    }

    @Override
    public void onServiceSelection(ArrayList<Service> services, String serviceString) {

        Log.e("services", ":" + GSON.toJson(services));

        registration.setService(services);
        servicetype.setText(serviceString);
        servicetype.setError(null);
    }

    @Override
    public void onVehicleSelection(ArrayList<Vehicle> vehicles, String list) {
        Log.e("Vehicles", ":" + GSON.toJson(vehicles));
        registration.setVehicle(vehicles);
        vehicletype.setText(list);
        vehicletype.setError(null);
        if (vehicletype.getText().toString().contains("Truck")) {
            //if truck is selected then okay ortherwise set Truck filed to null
        } else {
//                    Toast.makeText(activity, "Only for trucks", Toast.LENGTH_SHORT).show();
            trucktype.setText(null);
            trucktype.setError(null);
        }
    }

    @Override
    public void onTruckTypeSelection(ArrayList<Truck> trucks, String trucksString) {

        Log.e("Truck", ":" + GSON.toJson(trucks));
        registration.setTruck(trucks);
        trucktype.setText(trucksString);
        trucktype.setError(null);


    }

    private void varificationDialog() {
        try {

            dialog = new Dialog(activity,
                    R.style.Theme_AppCompat_Translucent);
//            final Dialog dialog = new Dialog(activity,
//                    R.style.Theme_AppCompat_Translucent);
            dialog.setContentView(R.layout.varification_dialog);
            WindowManager.LayoutParams layoutParams = dialog.getWindow()
                    .getAttributes();
            layoutParams.dimAmount = 0.6f;
            dialog.getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//            dialog.getWindow().addFlags(
//                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
//            dialog.getWindow().addFlags(
//                    WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//            dialog.getWindow().addFlags(
//                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
//            TextView textMessage = (TextView) dialog
//                    .findViewById(R.id.textMessage);
//            textMessage.setMovementMethod(new ScrollingMovementMethod());
//            textMessage.setText("Grtttt JOb");
            final Button btnOk = (Button) dialog.findViewById(R.id.btnOk);
            final EditText editTextVarificationCode = (EditText) dialog.findViewById(R.id.textVarificatonCode);
            editTextVarificationCode.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0)
                        btnOk.setEnabled(true);
                    else
                        btnOk.setEnabled(false);
                }
            });
//editTextVarificationCode.setOnFocusChangeListener(new View.OnFocusChangeListener()
//{
//    public void onFocusChange (View v, boolean hasFocus)
//    {
//        if (hasFocus)
//        {
//            Log.d(TAG,"editTextVarificationCode:onFocusChange");
//            dialog.getWindow().setSoftInputMode(
//                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
//
////                InputMethodManager mgr =
////                  (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
////                mgr.showSoftInput(dialogField,0);
////chainway india
//            //qualca ...
//        }
//    }
//});

            Button resend = (Button) dialog.findViewById(R.id.btnResendOTP);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Validator.varificationCodeValidator(editTextVarificationCode)) {
                        editTextVarificationCode.setError(getString(R.string.verificationRequired));


                    } else {

                        String code = editTextVarificationCode.getText().toString();
                        varificationServerCall(code, dialog);
                        // dialog.dismiss();

                    }

                }

            });
            resend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resendVarificationCodeServerCall();
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void varificationServerCall(String varificationCode, final Dialog dialog) {
        final ConfirmationDialogEventsListener confirmationDialogEventsListener = this;
        LoadingBox.showLoadingDialog(activity, "Verifying...");

        UserData userData = CommonData.getUserData(activity);
        if (userData != null)
            RestClient.getApiServiceForPojo().verifySignUp(userData.getAccessToken(), varificationCode, new Callback<Login>() {
                @Override
                public void success(Login s, Response response) {
                    Log.d("Success", "" + s.toString());

                    new UpdateApp(activity, s);

                }

                @Override
                public void failure(RetrofitError error) {
                    ErrorCodes.checkCode(activity, error);
                    LoadingBox.dismissLoadingDialog();
                }
            });
    }

    private void resendVarificationCodeServerCall() {
        LoadingBox.showLoadingDialog(activity, "Resending...");
        UserData userData = CommonData.getUserData(activity);
        if (userData != null)
            RestClient.getApiServiceForPojo().resendVerificationToken(userData.getAccessToken(), new Callback<ServiceReply>() {
                @Override
                public void success(ServiceReply s, Response response) {
                    Log.d("Success", "" + s.toString());
                    CommonDialog.With(activity).show(s.getMessage());
                    LoadingBox.dismissLoadingDialog();
                }

                @Override
                public void failure(RetrofitError error) {
                    LoadingBox.dismissLoadingDialog();
                    ErrorCodes.checkCode(activity, error);
                }
            });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.e("Registration", "onBackPressed");

        Intent intent = new Intent(activity, ActivitySignUpSignIn.class);
        startActivity(intent);
        Transformer.leftToRight(activity);
    }

    private void companyListDialog(final boolean showDialog) {

        String json = "";
        final Gson gson = new Gson();
        if (!SessionManager.instance.lastUpdatedExpired(activity, Constants.DataRequest.PARTNERS)) {
            Log.e("registration", "data from file");
            json = AppFileUtils.readDataFile(Constants.DataRequest.PARTNERS + ".json", activity);
            Log.e("registration", "data:" + json);
            try {
                CompanyList companyList = gson.fromJson(json, CompanyList.class);

                if (showDialog)
                    showCompanyDialog(companyList);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }


        }

        if (BaseUtils.isNullOrEmpty(json)) {


            CompanyList companyList = CommonData.getCompanyList(activity);

            if (companyList != null && showDialog) {
                showCompanyDialog(companyList);
            } else {
                LoadingBox.showLoadingDialog(activity, "Loading...");
                RestClient.getApiServiceForPojo().getPartnerList(new Callback<CompanyList>() {
                    @Override
                    public void success(CompanyList companyList, Response response) {
                        Log.e("registration", "data from network:" + companyList.getMessage());
                        CommonData.saveCompanyList(activity, companyList);
                        AppFileUtils.saveDataFile(Constants.DataRequest.PARTNERS + ".json", BaseUtils.getJSONFromOBJ(companyList), activity);
                        SessionManager.instance.addSpecificLastRequested(activity, Constants.DataRequest.PARTNERS, Calendar.getInstance());
                        LoadingBox.dismissLoadingDialog();
                        if (showDialog)
                            showCompanyDialog(companyList);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        LoadingBox.dismissLoadingDialog();
                        ErrorCodes.checkCode(activity, error);
                    }
                });
            }

        }
    }

    private void showCompanyDialog(CompanyList companyList) {
        CompanyNamesDialog.WithActivity(this).show(companyList, ConfirmationDialogCodes.companyName);
    }

    @Override
    public void onCompanySelection(String companyName) {
        Log.e("onCompanySelection", "onCompanySelection:" + companyName);
        companyname.setText(companyName);
        companyname.setError(null);
    }

    //QW1hbldlZCBTZXAgMzAgMjAxNSAxMToxMTo1OSBHTVQrMDAwMCAoVVRDKQ\u003d\u003d
    //
    private void getDriverInfo() {
        UserData user = CommonData.getUserData(this);
        Log.e("getDriverInfo", "getDriverInfo:LOADING...");
        if (user != null) {
            Log.e("getDriverInfo", "getAccessToken:" + user.getAccessToken());

            RestClient.getApiServiceForPojo().getDriverInfo(user.getAccessToken(), new Callback<ServiceReply>() {
                @Override
                public void success(ServiceReply serviceReply, Response response) {
                    Log.e("getDriverInfo", "getDriverInfo:" + serviceReply.toString());
                }

                @Override
                public void failure(RetrofitError error) {
                    ErrorCodes.checkCode(activity, error);
                }
            });

        }

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
    public void onForceUpdate() {
        if (dialog != null)
            dialog.dismiss();
        clearEditTexts(findViewById(R.id.parentLayout));

    }

    private void clearEditTexts(View view) {

        if (view instanceof EditText)
            ((EditText) view).setText(null);

        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;

            for (int idx = 0; idx < group.getChildCount(); idx++) {
                clearEditTexts(group.getChildAt(idx));
            }
        }
    }


    @Override
    public void onCancelUpdate(Login userData, boolean isShowMessage) {
        CommonData.saveLoginData(activity, userData);
        LoadingBox.dismissLoadingDialog();
        dialog.dismiss();
        if (isShowMessage)
            CommonDialog.with(activity, this).show(userData.getMessage(), ConfirmationDialogCodes.registrationComplete);
        else OnOkButtonPressed(ConfirmationDialogCodes.registrationComplete);
    }
}
