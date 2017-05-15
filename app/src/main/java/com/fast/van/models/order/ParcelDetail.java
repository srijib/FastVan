package com.fast.van.models.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Amandeep Singh Bagli on 07/10/15.
 */
public class ParcelDetail {
    @SerializedName("typeOfGood")
    @Expose
    private String typeOfGood;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("parcelPicture")
    @Expose

    private String imagePath;
    @SerializedName("weightUnit")
    @Expose
    private String weightUnit;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("dimensions")
    @Expose
    private Dimensions dimensions;
    @SerializedName("descriptions")
    @Expose
    private String descriptions;


    public String getTypeOfGood() {
        return typeOfGood;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getWeight() {
        return weight;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Dimensions getDimensions() {
        return dimensions;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public String getImagePath() {
        return imagePath;
    }
}
