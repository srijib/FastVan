package com.fast.van.models.login;

import com.fast.van.models.signup.LatLong;
import com.fast.van.models.signup.Phone;
import com.fast.van.models.signup.ServiceType;
import com.fast.van.models.signup.VehicalType;
import com.google.gson.annotations.Expose;

/**
 * Created by Amandeep Singh Bagli on 24/09/15.
 */
public class LoginData {

    @Expose private String accessToken;
   @Expose private ServiceType serviceType[];
    @Expose private VehicalType vehicleType[];
    @Expose private String firstName;
    @Expose private String lastName;
    @Expose private String fullName;
    @Expose private String email;
    @Expose private String companyName;
    @Expose private int appVersion;
    @Expose private LatLong addressLatLong;
    @Expose private Phone phone;
    @Expose private Phone adminDataPhone;
    @Expose private String driverImageUrl;
    @Expose private String vehicleRegistrationNumber;

    public String getAccessToken() {
        return accessToken;
    }

    public ServiceType[] getServiceType() {
        return serviceType;
    }

    public VehicalType[] getVehicleType() {
        return vehicleType;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public int getAppVersion() {
        return appVersion;
    }

    public Phone getPhone() {
        return phone;
    }

    public String getDriverImageUrl() {
        return driverImageUrl;
    }

    public String getVehicleRegistrationNumber() {
        return vehicleRegistrationNumber;
    }

    public String getAdminDataPhone() {
        if(adminDataPhone==null)
            return null;

        return adminDataPhone.getPhoneNumber();
    }
}
