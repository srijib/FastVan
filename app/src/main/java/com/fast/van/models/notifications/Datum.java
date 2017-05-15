package com.fast.van.models.notifications;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Amandeep Singh Bagli on 17/11/15.
 */
public class Datum {
    @SerializedName("_id")
    @Expose
    private String Id;
    @SerializedName("orderId")
    @Expose
    private int orderId;
    @SerializedName("customerId")
    @Expose
    private String customerId;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    public String getId() {
        return Id;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getType() {
        return type;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
