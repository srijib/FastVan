package com.fast.van.models.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Amandeep Singh Bagli on 23/10/15.
 */
public class Phone {

    @SerializedName("prefix")
    @Expose
    private String prefix;
    @SerializedName("phoneNumber")
    @Expose
    private String phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
