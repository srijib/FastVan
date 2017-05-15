package com.fast.van.models.signup;

import com.google.gson.annotations.Expose;

/**
 * Created by Amandeep Singh Bagli on 21/09/15.
 */
public class TokenData {
    @Expose
    int statusCode;
    @Expose
    String accessToken;
    @Expose
    int appVersion;
    @Expose
    String verificationToken;

    public int getStatusCode() {
        return statusCode;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getAppVersion() {
        return appVersion;
    }

    public String getVerificationToken() {
        return verificationToken;
    }
}