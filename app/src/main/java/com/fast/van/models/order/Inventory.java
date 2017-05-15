package com.fast.van.models.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Amandeep Singh Bagli on 04/12/15.
 */
public class Inventory {



    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    private String imagePath;
    @SerializedName("inventoryPicture")
    @Expose
    private String inventoryPicture;
    @SerializedName("hasPicture")
    @Expose
    private boolean hasPicture;
    private String inventoryName;






   // Application Number: 50526943 Subject Code : 87
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getId() {
                return id;
    }

    public int getQty(){
        int qty=0;
        try {
            qty= Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

        return qty;
    }

    public String getImagePath() {
        return inventoryPicture;
    }

    public void setImagePath(String imagePath) {
        if(imagePath!=null)
            this.hasPicture=!imagePath.isEmpty();
        this.inventoryPicture = imagePath;
    }


    public boolean isHasPicture() {
        return hasPicture;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }
}
