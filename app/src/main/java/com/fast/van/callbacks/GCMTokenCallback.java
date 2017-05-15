package com.fast.van.callbacks;

/**
 * Created by Amandeep Singh Bagli on 09/10/15.
 */
public interface GCMTokenCallback {

   void onGCMSuccess(String id);
   void onGCMFailure();

}
