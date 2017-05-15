package com.fast.van.models.signup;

import com.google.gson.annotations.Expose;

/**
 * Created by Amandeep Singh Bagli on 22/09/15.
 */
public class CompanyList {

   @Expose int statusCode;
   @Expose String message;
   @Expose Company data[];

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public Company[] getData() {
        return data;
    }
}
