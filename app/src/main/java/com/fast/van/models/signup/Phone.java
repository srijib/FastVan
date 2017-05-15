package com.fast.van.models.signup;


import com.fast.van.utils.BaseUtils;
import com.google.gson.annotations.Expose;

/**
 * Created by Amandeep Singh Bagli on 21/09/15.
 */
public class Phone {
  @Expose String prefix;
  @Expose String phoneNumber;


   public Phone(String phoneNumber){
        prefix="+27";
this.phoneNumber=phoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {


        return  BaseUtils.getJSONFromOBJ(this);
    }
}
