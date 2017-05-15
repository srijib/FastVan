package com.fast.van.models.signup;

import com.google.gson.annotations.Expose;

/**
 * Created by Amandeep Singh Bagli on 21/09/15.
 */
public class Service {
    @Expose ServiceType serviceType;
    public Service(ServiceType serviceType){
        this.serviceType=serviceType;
    }
}
