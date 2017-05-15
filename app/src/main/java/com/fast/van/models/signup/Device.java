package com.fast.van.models.signup;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.fast.van.FastVanApplication;
import com.fast.van.utils.BaseUtils;
import com.fast.van.utils.CommonData;
import com.fast.van.utils.CommonUtils;
import com.fast.van.utils.Log;

import com.google.gson.annotations.Expose;

/**
 * Created by Amandeep Singh Bagli on 21/09/15.
 */
public class Device {
  @Expose String deviceType;
    @Expose String deviceName;
    @Expose String deviceToken;



    public Device(Context context){
        deviceType= CommonUtils.getDeviceType();
        deviceName=CommonUtils.getDeviceName();
        deviceToken=CommonUtils.getDeviceToken(context);

    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getDeviceName() {
        return deviceName;
    }

    @Override
    public String toString() {
        String string= BaseUtils.getJSONFromOBJ(this);
        Log.e("Device",":"+string);
        return  string;
    }
}
