package com.fast.van.models.order;

import com.google.gson.annotations.Expose;

/**
 * Created by Amandeep Singh Bagli on 07/12/15.
 */
public class ProfilePicture {
    @Expose
  private String profilePicture;
    @Expose
    private String profilePictureThumb;

    public String getProfilePicture() {
        return profilePicture;
    }
}
