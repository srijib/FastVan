package com.fast.van.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fast.van.R;
import com.fast.van.common.SessionManager;
import com.fast.van.common.ValidationEditText;
import com.fast.van.common.Validator;
import com.fast.van.config.Constants;
import com.fast.van.dialogs.CompanyNamesDialog;
import com.fast.van.dialogs.ConfirmationDialogCodes;
import com.fast.van.dialogs.ConfirmationDialogEventsListener;
import com.fast.van.dialogs.ServiceDialog;
import com.fast.van.dialogs.VehicleDialog;
import com.fast.van.loadingindicator.LoadingBox;
import com.fast.van.models.ServiceReply;
import com.fast.van.models.login.Login;
import com.fast.van.models.login.LoginData;
import com.fast.van.models.signup.CompanyList;
import com.fast.van.models.signup.LatLong;
import com.fast.van.models.signup.Registration;
import com.fast.van.models.signup.Service;
import com.fast.van.models.signup.ServiceType;
import com.fast.van.models.signup.UserData;
import com.fast.van.models.signup.VehicalType;
import com.fast.van.models.signup.Vehicle;
import com.fast.van.retrofit.RestClient;
import com.fast.van.transformer.Transformer;
import com.fast.van.utils.BaseUtils;
import com.fast.van.utils.CommonData;
import com.fast.van.utils.ErrorCodes;
import com.fast.van.utils.Log;
import com.fast.van.utils.file.AppFileUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * Created by Amandeep Singh Bagli on 13/10/15.
 */
public class ActivityDriverEditProfile extends ImageChooserBaseActivity implements ServiceDialog.ServiceDialogListener, VehicleDialog.VehicleDialogListener, CompanyNamesDialog.CompanyDialogListener, ConfirmationDialogEventsListener {

    private EditText firstname, lastname, phonenumber, email, companyname, servicetype, vehicletype, vehicalnumber;

    private ImageButton buttonEditProfile;
    private ImageView buttonEditImageIcon, imageViewAvatar;
    private RatingBar ratingBar;
    private TextView serviceHint, vehicleHint;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fastvan_activity_driver_edit_profile);
        TextView title = (TextView) findViewById(R.id.screenTitle);
        title.setText("My Profile");
        findViewById(R.id.backbutton).setOnClickListener(this);


        initform();
        //  getDriverInfo();

    }

    @Override
    public void onLocationUpdate(Location location) {

    }


    @Override
    public void onGoogleConnectionFailed(ConnectionResult connectionResult) {

    }

    private void myProfileView(boolean enable) {
        enableDisableView(findViewById(R.id.parentLayout), enable);

        ratingBar.setVisibility(enable ? View.GONE : View.VISIBLE);
        buttonEditImageIcon.setVisibility(enable ? View.VISIBLE : View.GONE);
        serviceHint.setVisibility(enable ? View.VISIBLE : View.GONE);
        vehicleHint.setVisibility(enable ? View.VISIBLE : View.GONE);
        buttonEditProfile.setImageResource(enable ? R.drawable.icon_edit_done : R.drawable.icon_edit);
        email.setEnabled(false);
        phonenumber.setEnabled(false);
        companyname.setEnabled(false);
        vehicalnumber.setEnabled(false);

    }


    private void initform() {

        imageViewAvatar = (ImageView) findViewById(R.id.imgAvatar);
        buttonEditProfile = (ImageButton) findViewById(R.id.buttonUpdateProfile);
        buttonEditImageIcon = (ImageView) findViewById(R.id.buttonEditImageIcon);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        serviceHint = (TextView) findViewById(R.id.serviceHint);
        vehicleHint = (TextView) findViewById(R.id.vehicleHint);
        ValidationEditText validationEditText = new ValidationEditText();
        firstname = (EditText) findViewById(R.id.firstname);
        validationEditText.setValidationFilter(ValidationEditText.TextType.FirstName, firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        validationEditText.setValidationFilter(ValidationEditText.TextType.LastName, lastname);

        phonenumber = (EditText) findViewById(R.id.mobilenumber);//^(\+?27|0)[6-9][1-7][0-9]{7}$
        // validationEditText.setValidationFilter(ValidationEditText.TextType.PhoneNumber,phonenumber);
        companyname = (EditText) findViewById(R.id.companyname);
        servicetype = (EditText) findViewById(R.id.servicetype);
        vehicletype = (EditText) findViewById(R.id.vehicaltype);
        vehicalnumber = (EditText) findViewById(R.id.vehicalnumber);
        email = (EditText) findViewById(R.id.emailaddress);
        validationEditText.setValidationFilter(ValidationEditText.TextType.VehicleNumber, vehicalnumber);


        findViewById(R.id.submit).setOnClickListener(this);
        findViewById(R.id.editprofileLayout).setOnClickListener(this);
        findViewById(R.id.buttonUpdateProfile).setOnClickListener(this);
        findViewById(R.id.buttonEditImageIcon).setOnClickListener(this);

        servicetype.setOnClickListener(this);
        vehicletype.setOnClickListener(this);
        companyname.setOnClickListener(this);

        findViewById(R.id.imgAvatar).setOnClickListener(this);


        //companyListDialog(false);
        myProfileView(update);
    }

    private boolean validateform() {
        if (mCurrentLocation != null) {
            Log.e("Location", "Lat:" + mCurrentLocation.getLatitude() + " long:" + mCurrentLocation.getLongitude());
        } else {
            Log.e("Location", "No location");
        }
        boolean validated = true;
        boolean focus = false;
        if (Validator.textValidator(firstname)) {
            validated = false;
            firstname.setError(getString(R.string.firstnamerequired));
            if (!focus) {
                focus = true;
                firstname.requestFocus();
            }

        }
        if (Validator.textValidator(lastname)) {
            validated = false;
            lastname.setError(getString(R.string.lastnamerequired));
            if (!focus) {
                focus = true;
                lastname.requestFocus();
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
        if (!Validator.emailValidator(email)) {
            validated = false;
            email.setError(getString(R.string.emailrequired));
            if (!focus) {
                focus = true;
                email.requestFocus();
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
       /* if (Validator.vehicleNumValidator(vehicalnumber)) {
            validated = false;
            vehicalnumber.setError(getString(R.string.vehicalnumberrequired));
            if(!focus){
                focus=true;
                vehicalnumber.requestFocus();
            }

        }*/


        return validated;
    }

    private boolean update;

    @Override
    public void onClickView(View v) {


        int id = v.getId();
        switch (id) {
            case R.id.submit:
                //Toast.makeText(activity, "testing..", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(activity, ActivityChangePassword.class);
                startActivity(intent);
                Transformer.rightToLeft(activity);

                break;

            case R.id.servicetype:
                ServiceDialog.WithActivity(this).Show(ConfirmationDialogCodes.serviceType);
                break;
            case R.id.vehicaltype:
                VehicleDialog.WithActivity(this).Show(ConfirmationDialogCodes.vehicleType);
                break;
            case R.id.companyname:
                companyListDialog(true);

                break;
            case R.id.editprofileLayout:
            case R.id.buttonUpdateProfile:

                update = !update;
                if (!update) {
                    update = !update;
                    boolean valid = validateform();
                    Log.d("validate", "form: " + valid);
                    if (valid) {
                        updateprofile();
                    }
                } else

                    myProfileView(update);
                break;
            case R.id.backbutton:
                onBackPressed();
                break;
            case R.id.imgAvatar:
            case R.id.buttonEditImageIcon:
                if (update)
                    chooseImageFrom((ImageView) findViewById(R.id.imgAvatar));
                //   chooseImageFrom(null);
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

        Login data = CommonData.getSessionData(this);
        if (data != null && data.getData() != null) {
            try {
                fillData(data.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void fillData(LoginData data) {

        firstname.setText(data.getFirstName());
        lastname.setText(data.getLastName());
        companyname.setText(data.getCompanyName());
        email.setText(data.getEmail());
        vehicalnumber.setText(data.getVehicleRegistrationNumber());
        if (data.getPhone() != null)
            phonenumber.setText(data.getPhone().getPhoneNumber());
        String services = Arrays.toString(data.getServiceType());
        // servicetype.setText(services.substring(1, services.length()-1));
//        String vehicles=Arrays.toString(data.getVehicleType());
//        vehicletype.setText(vehicles.substring(1, vehicles.length() - 1));
        if (data.getServiceType() != null)
            setUpServices(data.getServiceType());
        if (data.getVehicleType() != null)
            setUpVehicles(data.getVehicleType());


        Glide.with(this)
                .load(data.getDriverImageUrl())
                .asBitmap()
                .toBytes()
                .centerCrop()
                .into(new SimpleTarget<byte[]>(200, 200) {
                    @Override
                    public void onResourceReady(byte[] data, GlideAnimation anim) {
                        // Post your bytes to a background thread and upload them here.
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        imageViewAvatar.setImageBitmap(bitmap);

                    }
                });

       /*     Glide.with(this)
                    .load(data.getDriverImageUrl())
                    .fitCenter().thumbnail(0.1f)
                    .placeholder(R.drawable.icon_avatar_big)
                    .into(imageViewAvatar);*/

    }

    private void updateprofile() {
        LoadingBox.showLoadingDialog(activity, "Updating...");

        getValuesCallService();
//        Toast.makeText(activity, "Under Construction", Toast.LENGTH_LONG).show();

    }

    private void getValuesCallService() {


        registration.setPhone(phonenumber.getText().toString());

        if (mCurrentLocation != null) {

            registration.setLatLong(new LatLong(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude()));
            Log.e("Location", "Lat:" + mCurrentLocation.getLatitude() + " long:" + mCurrentLocation.getLongitude());
        } else {
            registration.setLatLong(new LatLong());
            Log.e("Location", "No location");
        }

        Map<String, TypedFile> images = new HashMap<String, TypedFile>();
        if (getOriginalFilePath() != null)
            images.put("profilePicture", new TypedFile("image/*", new File(getOriginalFilePath())));
        Login data = CommonData.getSessionData(this);
        if (data != null)
            RestClient.getApiServiceForPojo().editDriverInfo(new TypedString(data.getAccessToken()), new TypedString(firstname.getText().toString()), new TypedString(lastname.getText().toString()), new TypedString(registration.getService()), new TypedString(registration.getVehicle()), new TypedString(registration.getLatLong()), images, new Callback<Login>() {
                @Override
                public void success(Login userData, Response response) {
                    Log.d("Success", "" + userData.toString());
                    // Log.d("Success", "AccessToken: " + userData.getAccessToken());
                    CommonData.saveLoginData(getApplicationContext(), userData);
                    LoadingBox.dismissLoadingDialog();
                    // varificationDialog();

                    update = !update;
                    myProfileView(update);
                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    LoadingBox.dismissLoadingDialog();
//                try{
//                    JSONArray jsonObject=(JSONArray)error.getBody();
//                    Log.e("error","object:"+jsonObject.toString());
//                }catch (Exception e){
//                    e.printStackTrace();
//                    Log.e("error", "jsonobject:error" );
//                }
                    ErrorCodes.checkCode(activity, error);


//                DisplayDialog.show(activity,code);

                }
            });
        else
            LoadingBox.dismissLoadingDialog();

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

    }


    private void companyListDialog(final boolean showDialog) {

        String json = "";
        final Gson gson = new Gson();
        if (!SessionManager.instance.lastUpdatedExpired(activity, Constants.DataRequest.PARTNERS)) {
            Log.e(TAG, "data from file");
            json = AppFileUtils.readDataFile(Constants.DataRequest.PARTNERS + ".json", activity);
            Log.e(TAG, "data:" + json);
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
                        Log.e("registration", "data from network");
                        CommonData.saveCompanyList(activity, companyList);
                        AppFileUtils.saveDataFile(Constants.DataRequest.PARTNERS + ".json", gson.toJson(companyList), activity);
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
        Log.e(TAG, "onCompanySelection:" + companyName);
        companyname.setText(companyName);
        companyname.setError(null);
    }

    //QW1hbldlZCBTZXAgMzAgMjAxNSAxMToxMTo1OSBHTVQrMDAwMCAoVVRDKQ\u003d\u003d
    //
    private void getDriverInfo() {
        UserData user = CommonData.getUserData(this);
        Log.e(TAG, "getDriverInfo:LOADING...");
        if (user != null) {
            Log.e(TAG, "getAccessToken:" + user.getAccessToken());

            RestClient.getApiServiceForPojo().getDriverInfo(user.getAccessToken(), new Callback<ServiceReply>() {
                @Override
                public void success(ServiceReply serviceReply, Response response) {
                    Log.e(TAG, "getDriverInfo:" + serviceReply.toString());
                }

                @Override
                public void failure(RetrofitError error) {
                    ErrorCodes.checkCode(activity, error);
                }
            });

        }

    }

    private void enableDisableView(View view, boolean enabled) {

        if (view instanceof EditText) {
            view.setEnabled(enabled);

        }

        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;

            for (int idx = 0; idx < group.getChildCount(); idx++) {
                enableDisableView(group.getChildAt(idx), enabled);
            }
        }
    }

    private void setUpServices(ServiceType[] serviceType) {

        ArrayList<Service> services = new ArrayList<>();
        StringBuilder list = new StringBuilder();
        for (ServiceType serviceType1 : serviceType) {
            switch (serviceType1) {
                case COURIER:
                    list.append(getString(R.string.courier) + ", ");
                    services.add(new Service(ServiceType.COURIER));
                    break;
                case FREIGHT:
                    list.append(getString(R.string.removal) + ", ");
                    services.add(new Service(ServiceType.FREIGHT));
                    break;
                case DELIVERY:
                    list.append(getString(R.string.delivery) + ", ");
                    services.add(new Service(ServiceType.DELIVERY));
                    break;
                case ALL_SERVICES:
               /*     list.append(getString(R.string.allservices) + ", ");
                    services.add(new Service(ServiceType.ALL_SERVICES));*/
                    break;
            }
        }


        String string = list.toString();
        try {
            string = string.substring(0, string.length() - 2);
        } catch (Exception e) {
            Log.e(TAG, "Service Type Empty from API", e);
        }

        servicetype.setText(string);
        registration.setService(services);
    }

    private void setUpVehicles(VehicalType[] vehicleType) {
        ArrayList<Vehicle> vehicles = new ArrayList<>();
        StringBuilder list = new StringBuilder();
        for (VehicalType vehicalType1 : vehicleType) {
            switch (vehicalType1) {
                case BIKE:
                    vehicles.add(new Vehicle(VehicalType.BIKE));
                    list.append(getString(R.string.bike) + ", ");
                    break;
                case VAN:
                    vehicles.add(new Vehicle(VehicalType.VAN));
                    list.append(getString(R.string.van) + ", ");
                    break;
                case ONE_TON_TRUCK:
                    vehicles.add(new Vehicle(VehicalType.ONE_TON_TRUCK));
                    list.append(getString(R.string.oneton) + ", ");
                    break;
                case TWO_TON_TRUCK:
                    vehicles.add(new Vehicle(VehicalType.TWO_TON_TRUCK));
                    list.append(getString(R.string.twoton) + ", ");
                    break;
            }
        }

        String string = list.toString();
        try {
            string = string.substring(0, string.length() - 2);
        } catch (Exception e) {
            Log.e(TAG, "Vehicle Type Empty from API", e);
        }
        vehicletype.setText(string);
        registration.setVehicle(vehicles);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Transformer.leftToRight(activity);
    }
}
