package com.fast.van.models.order;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amandeep Singh Bagli on 07/10/15.
 */
public class GoodsDropLocationDetail {
    @SerializedName("_id")
    @Expose
    private String Id;
    @SerializedName("parcelDetail")
    @Expose

    private List<ParcelDetail> parcelDetail = new ArrayList<ParcelDetail>();
    @SerializedName("inventoryDetail")
    @Expose
    private List<InventoryName> inventoryDetail ;
    @SerializedName("dropLocationDetail")
    @Expose
    private DropLocationDetail dropLocationDetail;
    public GoodsDropLocationDetail(DropLocationDetail dropLocationDetail){
        this.dropLocationDetail=dropLocationDetail;
    }

    public void setParcelDetail(ParcelDetail parcelDetail) {
        this.parcelDetail.add(parcelDetail);
    }


    public String getTypeOfGood() {
        if(parcelDetail.size()>0)
            return parcelDetail.get(0).getTypeOfGood();
        return null;

    }

    public String getQuantity() {
        if(parcelDetail.size()>0)
            return parcelDetail.get(0).getQuantity();
        return null;

    }


    public String getDescriptions() {
        if(parcelDetail.size()>0)
            return parcelDetail.get(0).getDescriptions();
        return null;

    }

    public String getFullAddress() {
        if(dropLocationDetail==null)
            return null;
        return dropLocationDetail.getFullAddress();
    }

    public DropLocationDetail getDropLocationDetail() {
        return dropLocationDetail;
    }

    public List<ParcelDetail> getParcelDetail() {
        return parcelDetail;
    }

    public List<InventoryName> getInventoryDetail() {
        return inventoryDetail;
    }
}
