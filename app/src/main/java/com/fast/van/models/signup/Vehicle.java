package com.fast.van.models.signup;

import com.google.gson.annotations.Expose;

/**
 * Created by Amandeep Singh Bagli on 21/09/15.
 */
public class Vehicle {

    @Expose private  VehicalType vehicleType;
    public Vehicle(VehicalType vehicleType){
        this.vehicleType=vehicleType;
    }
}
