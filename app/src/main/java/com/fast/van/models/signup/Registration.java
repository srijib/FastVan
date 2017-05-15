package com.fast.van.models.signup;

import android.content.Context;

import com.fast.van.config.Constants;
import com.fast.van.utils.BaseUtils;
import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by Amandeep Singh Bagli on 21/09/15.
 */
public class Registration {

    @Expose
    private
    ArrayList<Service> service;
    @Expose
    private ArrayList<Vehicle> vehicle;
    @Expose
    private ArrayList<Truck> truck;
    @Expose
    private Device deviceDetails;
    @Expose
    private String appVersion = Constants.APP_VERSION;
    @Expose
    private Picture profilePicture;
    @Expose
    private Phone phone;
    @Expose
    private LatLong latLong;


    public Registration(Context context) {

        deviceDetails = new Device(context);
        profilePicture = new Picture();

    }


    public void setService(ArrayList<Service> service) {
        this.service = service;
    }

    public void setVehicle(ArrayList<Vehicle> vehicle) {
        this.vehicle = vehicle;
    }

    public void setTruck(ArrayList<Truck> truck) {
        this.truck = truck;
    }

    public String getService() {

        return BaseUtils.getJSONFromOBJ(service);

    }


    public String getVehicle() {
        return BaseUtils.getJSONFromOBJ(vehicle);

    }

    public String getTruck() {
        return BaseUtils.getJSONFromOBJ(truck);

    }

    public String getDeviceDetails() {
        return deviceDetails.toString();
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getProfilePicture() {
        return profilePicture.toString();
    }

    public String getPhone() {
        return BaseUtils.getJSONFromOBJ(phone);
    }

    public void setPhone(String phone) {
        this.phone = new Phone(phone);

    }

    public void setLatLong(LatLong latLong) {
        this.latLong = latLong;
    }

    public String getLatLong() {
        return BaseUtils.getJSONFromOBJ(latLong);
    }


    public void setDeviceDetails(Context context) {
        this.deviceDetails = new Device(context);
    }
}
