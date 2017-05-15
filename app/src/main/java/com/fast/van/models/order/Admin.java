package com.fast.van.models.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Amandeep Singh Bagli on 12/01/16.
 */
public class Admin {

    @SerializedName("_id")
    @Expose
    private String Id;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("phoneNumberPrefix")
    @Expose
    private String phoneNumberPrefix;
    @SerializedName("phoneNumber")
    @Expose
    private String  phoneNumber;

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
