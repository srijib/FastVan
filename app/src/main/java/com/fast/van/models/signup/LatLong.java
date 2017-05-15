package com.fast.van.models.signup;


import com.fast.van.utils.BaseUtils;
import com.google.gson.annotations.Expose;

/**
 * Created by Amandeep Singh Bagli on 21/09/15.
 */
public class LatLong {

   @Expose double latitude;
   @Expose double longitude;

   public LatLong(){

   }
   public LatLong(double latitude,double longitude){
this.latitude=latitude;
      this.longitude=longitude;
   }

   @Override
   public String toString() {
      return  BaseUtils.getJSONFromOBJ(this);
   }
}
