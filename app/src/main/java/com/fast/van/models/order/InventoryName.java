package com.fast.van.models.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amandeep Singh Bagli on 08/12/15.
 */
public class InventoryName {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("subInventory")
    @Expose
    private List<Inventory> data = new ArrayList<Inventory>();

    public InventoryName(String name,String id){
        this.name=name;
        this.id=id;
    }

    public void setInventory(Inventory inventory) {
        this.data.add(inventory);
    }

    public List<Inventory> getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }
}
