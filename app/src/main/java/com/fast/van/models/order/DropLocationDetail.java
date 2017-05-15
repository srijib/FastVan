package com.fast.van.models.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Amandeep Singh Bagli on 07/10/15.
 */
public class DropLocationDetail {
    @SerializedName("latitude")
    @Expose
    private double latitude;
    @SerializedName("longitude")
    @Expose
    private double longitude;
    @SerializedName("fullAddress")
    @Expose
    private String fullAddress;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("details")
    @Expose
    private String details;


    public String getFullAddress() {
        return fullAddress;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getDetails() {
        return details;
    }
}
