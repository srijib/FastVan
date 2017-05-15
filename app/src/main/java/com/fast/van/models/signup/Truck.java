package com.fast.van.models.signup;

import com.google.gson.annotations.Expose;

/**
 * Created by Amandeep Singh Bagli on 21/09/15.
 */
public class Truck {

    @Expose
    private TruckType truckType;

    public Truck(TruckType truckType) {
        this.truckType = truckType;
    }
}
