package com.fast.van.models.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Amandeep Singh Bagli on 23/10/15.
 */
public class Customer {
    @SerializedName("_id")
    @Expose
    private String Id;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;

    @SerializedName("phone")
    @Expose
    private Phone phone;
    @SerializedName("customerImageUrls")
    @Expose
    private ProfilePicture customerImageUrls;

    public String getFullName() {


        return firstName+" "+lastName;
    }

    public Phone getPhone() {
        return phone;
    }

    public ProfilePicture getCustomerImageUrls() {
        return customerImageUrls;
    }
}
