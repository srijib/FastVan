package com.fast.van.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.*;
import android.widget.TextView;

import com.fast.van.cloudmessaging.RegistrationIntentService;
import com.fast.van.common.SessionManager;
import com.fast.van.config.Constants;
import com.fast.van.models.login.Login;
import com.fast.van.models.signup.CompanyList;
import com.fast.van.models.signup.UserData;
import com.fast.van.sharedpreferences.Prefs;
import com.fast.van.sharedpreferences.SharedPreferencesName;
import com.fast.van.utils.file.AppFileUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.Calendar;

//import com.google.android.gms.maps.model.LatLng;
//import com.selfdrive.R;
//import com.selfdrive.facebook.SocialLoginData;
//import com.selfdrive.gcm.GCMClientManager;
//import com.selfdrive.models.bookings.MyAllBookings;
//import com.selfdrive.models.bookingsummery.BookingSummeryData;
//import com.selfdrive.models.cardata.CarsData;
//import com.selfdrive.models.enums.BookingStatus;
//import com.selfdrive.models.userdata.UserData;
//import com.selfdrive.sharedpreferences.Prefs;
//import com.selfdrive.sharedpreferences.SharedPreferencesName;

public class CommonData {

  //  public static SocialLoginData loginDataStatic;
    public static String CallUsNumber = "+91-9250035555";

    public static String PrivacyPolicyURL = "http://m.revv.co.in/privacy.html";
    public static String TermsAndCondititionsUrl = "http://termsandconditions.revv.co.in/";

//    private static LatLng cLatLngOnMap;
    private static UserData userData = null;
    private static Login loginData = null;
    private static CompanyList companyList = null;

//    private static CarsData carsData = null;
//    private static BookingSummeryData bookingSummeryData = null;
//    private static MyAllBookings myAllBookings;

    /**
     * @param context
     * @param deviceToken
     */
    public static void saveDeviceToken(Context context, String deviceToken) {
        Prefs.with(context).save(SharedPreferencesName.DEVICE_TOKEN, deviceToken);
    }

    /**
     *
     * @param context
     * @param orderId
     * @param message
     */
    public static void saveNotificationData(Context context, String orderId,String message) {
        Prefs.with(context).save(SharedPreferencesName.ORDER_ID, orderId);
        Prefs.with(context).save(SharedPreferencesName.MESSAGE, message);
    }

    /**
     *
     * @param context
     */
    public static void removeNotificationData(Context context) {
        Prefs.with(context).remove(SharedPreferencesName.ORDER_ID);
        Prefs.with(context).remove(SharedPreferencesName.MESSAGE);
    }
    public static String getNotificationOrderId(Context context) {

        String orderId = Prefs.with(context).getString(SharedPreferencesName.ORDER_ID, "");
        return  orderId;
    }

    /**
     * @param context
     * @return
     */
    public static String getDeviceToken(Context context) {

        String token = Prefs.with(context).getString(SharedPreferencesName.DEVICE_TOKEN, "");

        if (token.isEmpty()) {

           // token = GCMClientManager.getRegistrationId(context);

            if (checkPlayServices(context)) {
                // Start IntentService to register this application with GCM.
                Intent intent = new Intent(context, RegistrationIntentService.class);
                context.startService(intent);
            }
        }
        return token;
    }
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private static boolean checkPlayServices(Context context) {
        try {
            GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
            int resultCode = apiAvailability.isGooglePlayServicesAvailable(context);
            if (resultCode != ConnectionResult.SUCCESS) {
                if (apiAvailability.isUserResolvableError(resultCode)) {
                    apiAvailability.getErrorDialog((Activity) context, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                            .show();
                } else {
                    android.util.Log.i(context.getPackageName(), "This device is not supported.");

                }
                return false;
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }

    /**
     * @param context
     * @return
     */
//    public static LatLng getMapCurrentLocation(Context context) {
//
//        cLatLngOnMap = new LatLng(Double.parseDouble(Prefs.with(context).getString(SharedPreferencesName.MapCurrentLatitude,
//                "28.59167210820013")),
//                Double.parseDouble(Prefs.with(context).getString(SharedPreferencesName.MapCurrentLongitude, "77.23178587853909")));
//        return cLatLngOnMap;
//
//    }


    /**
     * @param context
     * @param location
     */
    public static void updateMapCurrentLocation(Context context, Location location) {
        if (location != null) {
            Prefs.with(context).save(SharedPreferencesName.MapCurrentLatitude, location.getLatitude());
            Prefs.with(context).save(SharedPreferencesName.MapCurrentLongitude, location.getLongitude());
        }
    }


    /**
     * @param context
     * @param _userData
     */
    public static void saveUserData(Context context, UserData _userData) {
        userData = _userData;
        Prefs.with(context).save(SharedPreferencesName.USERDATA, _userData);
    }
    public static void saveLoginData(Context context, Login _loginData) {
        loginData = _loginData;
        Prefs.with(context).save(SharedPreferencesName.SESSIONDATA, _loginData);
    }

    /**
     *
     * @param context
     * @param _companyList
     */
    public static void saveCompanyList(Context context, CompanyList _companyList) {
        companyList = _companyList;
        Prefs.with(context).save(SharedPreferencesName.COMPANYNAMES, _companyList);
    }


    /**
     * @param context
     * @return
     */
    public static UserData getUserData(Context context) {
        if (userData == null)
            userData = Prefs.with(context).getObject(SharedPreferencesName.USERDATA, UserData.class);
        return userData;
    }
    public static Login getSessionData(Context context) {
        if (loginData == null)
            loginData = Prefs.with(context).getObject(SharedPreferencesName.SESSIONDATA, Login.class);
        return loginData;
    }

    /**
     * @param context
     * @return
     */
    public static CompanyList getCompanyList(Context context) {
        if (companyList == null)
            companyList = Prefs.with(context).getObject(SharedPreferencesName.COMPANYNAMES, CompanyList.class);
        return companyList;
    }


    /**
     * @param context
     * @param _carsData
     */
//    public static void saveCarsData(Context context, CarsData _carsData) {
//        carsData = _carsData;
//        Prefs.with(context).save(SharedPreferencesName.CARS_DATA, _carsData);
//    }


    /**
     * @param context
     * @return
     */
//    public static CarsData getCarsData(Context context) {
//        if (carsData == null)
//            carsData = Prefs.with(context).getObject(SharedPreferencesName.CARS_DATA, CarsData.class);
//        return carsData;
//    }


    /**
     * @param context
     * @param _bookingSummeryData
     */
//    public static void saveBookingSummeryData(Context context, BookingSummeryData _bookingSummeryData) {
//        bookingSummeryData = _bookingSummeryData;
//        Prefs.with(context).save(SharedPreferencesName.BOOKING_SUMMERY_DATA, _bookingSummeryData);
//    }


    /**
     * @param context
     * @return
     */
//    public static BookingSummeryData getBookingSummeryData(Context context) {
//        if (bookingSummeryData == null)
//            bookingSummeryData = Prefs.with(context).getObject(SharedPreferencesName.BOOKING_SUMMERY_DATA, BookingSummeryData.class);
//        return bookingSummeryData;
//    }


    /**
     * @param context
     * @param _myAllBookings
     */
//    public static void saveMyBookingsData(Context context, MyAllBookings _myAllBookings) {
//        myAllBookings = _myAllBookings;
//        Prefs.with(context).save(SharedPreferencesName.MY_BOOKING_DATA, _myAllBookings);
//    }


    /**
     * @param context
     * @return
     */
//    public static MyAllBookings getMyBookingsData(Context context) {
//        if (myAllBookings == null)
//            myAllBookings = Prefs.with(context).getObject(SharedPreferencesName.MY_BOOKING_DATA, MyAllBookings.class);
//        return myAllBookings;
//    }


    /**
     *
     */
    public static void resetData() {
        userData = null;
        loginData = null;

    }
public static void logout(Context context){
    AppFileUtils.saveDataFile(Constants.DataRequest.NOTIFICATIONS + ".json", "", context);
    SessionManager.instance.addSpecificLastRequested(context, Constants.DataRequest.NOTIFICATIONS, Calendar.getInstance());
    try {BaseUtils.removeNotification(context);

        BaseUtils.removeCancelNotification(context);
    } catch (Exception e) {
        e.printStackTrace();
    }
    resetData();
    Prefs.with(context).removeAll();
}

    /**
     * @param context
     * @param isSocailLogin
     */
    public static void SaveIsSocailLogin(Context context, boolean isSocailLogin) {
        Prefs.with(context).save(SharedPreferencesName.IS_SOCIAL_LOGIN, isSocailLogin);
    }


    /**
     * @param context
     * @return
     */
    public static boolean GetIsSocailLogin(Context context) {
        return Prefs.with(context).getBoolean(SharedPreferencesName.IS_SOCIAL_LOGIN, false);
    }


    /**
     * @param mContext
     * @param status
     * @param statusTextView
     */
    public static void setStatus(Context mContext, int status, TextView statusTextView) {


//        bookingStatus.MODIFIED_SUCCESSFULLY = 0;
//        bookingStatus.ONGOING = 1;
//        bookingStatus.CONFIRMED = 2;
//        bookingStatus.COMPLETED = 3;
//        bookingStatus.CANCELLED = 4;
//        bookingStatus.ONTHEWAY = 5;
//        bookingStatus.DELIVERED = 6;
//        bookingStatus.CHECKEDOUT = 7;
//        bookingStatus.REJECTED = 8;
//        bookingStatus.PAYMENT_PENDING = 9;
//        bookingStatus.PAYMENT_UNSUCCESSFUL = 10;
//        bookingStatus.PAYMENT_SUCCESSFUL_BOOKING_INACTIVE = 11;
//        bookingStatus.New = 12;
//        bookingStatus.ToBeApproved = 13;
//        bookingStatus.Approved = 14;
//        bookingStatus.INVOICE_SENT = 15;



//         if (status == BookingStatus.ONGOING.getStatus()) {
//            statusTextView.setText("Ongoing");
//        } else if (status == BookingStatus.CONFIRMED.getStatus()) {
//            statusTextView.setText("Confirmed");
//        } else if (status == BookingStatus.COMPLETED.getStatus()) {
//            statusTextView.setText("Completed");
//        } else if (status == BookingStatus.CANCELLED.getStatus()) {
//            statusTextView.setText("Cancelled");
//        } else if (status == BookingStatus.ONTHEWAY.getStatus()) {
//            statusTextView.setText("On the way");
//        } else if (status == BookingStatus.DELIVERED.getStatus()) {
//            statusTextView.setText("Delivered");
//        } else if (status == BookingStatus.CHECKEDOUT.getStatus()) {
//            statusTextView.setText("Checked out");
//        }

//         else if (status == BookingStatus.REJECTED.getStatus()) {
//            statusTextView.setText("Rejected");
//        } else if (status == BookingStatus.PAYMENT_PENDING.getStatus()) {
//            statusTextView.setText("Payment Pending");
//        } else if (status == BookingStatus.PAYMENT_UNSUCCESSFUL.getStatus()) {
//            statusTextView.setText("Payment Unsuccessful");
//        } else if (status == BookingStatus.PAYMENT_SUCCESSFUL_BOOKING_INACTIVE.getStatus()) {
//            statusTextView.setText("Inactive");
//        } else if (status == BookingStatus.NEW.getStatus()) {
//            statusTextView.setText("New");
//        } else if (status == BookingStatus.TO_BE_APPROVED.getStatus()) {
//            statusTextView.setText("To Be Approved");
//        } else if (status == BookingStatus.APPROVED.getStatus()) {
//            statusTextView.setText("Approved");
//        } else if (status == BookingStatus.INVOICE_SENT.getStatus()) {
//            statusTextView.setText("Invoice sent");
//        }

//        statusTextView.setTextColor(mContext.getResources().getColor(R.color.light_grey));
    }

}