package com.fast.van.models.signup;


import com.fast.van.utils.BaseUtils;
import com.google.gson.annotations.Expose;

/**
 * Created by Amandeep Singh Bagli on 21/09/15.
 */
public class UserData {
    @Expose int statusCode;
    @Expose String message;
    @Expose TokenData data[];

    @Override
    public String toString() {
        return BaseUtils.getJSONFromOBJ(this);
    }

    public String getAccessToken() {

if(data!=null&&data.length>0){

    return data[0].getAccessToken();
}

        return null;
    }


}
