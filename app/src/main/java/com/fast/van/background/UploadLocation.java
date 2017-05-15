package com.fast.van.background;

import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.fast.van.models.ServiceReply;
import com.fast.van.models.login.Login;
import com.fast.van.models.signup.LatLong;
import com.fast.van.retrofit.RestClient;
import com.fast.van.utils.CommonData;
import com.fast.van.utils.Log;


import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Amandeep Singh Bagli on 29/10/15.
 */
public class UploadLocation {

    public static void update(Context context,Location location){

        Login user = CommonData.getSessionData(context);
        if(user!=null&&!user.getAccessToken().isEmpty()) {
            if (location != null)
                RestClient.getApiServiceForPojo().updateDriverLocation(user.getAccessToken(), new LatLong(location.getLatitude(), location.getLongitude()).toString(), new Callback<ServiceReply>() {
                    @Override
                    public void success(ServiceReply serviceReply, Response response) {
                        Log.d("UploadLocation", "success:" + serviceReply.getMessage());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("UploadLocation", "failure:" + error.getKind());
                    }
                });
        }
        else
        {
            Intent tracking = new Intent(context, UpdateDriverLocation.class);
            context.stopService(tracking);
        }

    }

}
