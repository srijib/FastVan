package com.fast.van.models.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amandeep Singh Bagli on 07/10/15.
 */
public class Order {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<OrderDetails> data;// = new ArrayList<OrderDetails>();

    public OrderDetails getData() {

        if(data==null||data.size()<1)
        return null;

        return data.get(0);
    }

    public List<OrderDetails> getOrderList() {

        if(data==null)
        return new ArrayList<OrderDetails>();

        return data;
    }
}
