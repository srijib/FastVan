package com.fast.van.models.login;

import com.fast.van.models.ServiceReply;

import com.fast.van.utils.BaseUtils;
import com.google.gson.annotations.Expose;

import retrofit.Callback;

/**
 * Created by Amandeep Singh Bagli on 22/09/15.
 */
public class Login {

     @Expose private String statusCode;
     @Expose private String message;
         @Expose private LoginData  data[];

    public String getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public LoginData getData() {
        if(data!=null&&data.length>0){
            return data[0];
        }

        return null;
    }
    public String getAccessToken() {

        if (data != null && data.length > 0) {

            return data[0].getAccessToken();
        }
        return  null;
    }
    @Override
    public String toString() {
        return  BaseUtils.getJSONFromOBJ(this);
    }


    public String getAdminDataPhone() {
        if(getData()==null)
            return null;

        return getData().getAdminDataPhone();
    }
}
