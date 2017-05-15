package com.fast.van.models;


import com.fast.van.utils.BaseUtils;
import com.google.gson.annotations.Expose;

/**
 * Created by Amandeep Singh Bagli on 22/09/15.
 */
public class ServiceReply {
@Expose private int statusCode;
@Expose private String message;
@Expose private String error;

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return  BaseUtils.getJSONFromOBJ(this);
    }
}
