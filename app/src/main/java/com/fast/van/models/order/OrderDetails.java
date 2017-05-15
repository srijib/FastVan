package com.fast.van.models.order;

import com.fast.van.models.signup.ServiceType;
import com.fast.van.models.signup.VehicalType;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amandeep Singh Bagli on 07/10/15.
 */
public class OrderDetails {
    @SerializedName("_id")
    @Expose
    private String Id;
    @SerializedName("serviceType")
    @Expose
    private String serviceType;

    @SerializedName("vehicleId")
    @Expose
    private String vehicleId;
    @SerializedName("scheduledTime")
    @Expose
    private String scheduledTime;
    @SerializedName("serviceAdditionalInfo")
    @Expose
    private ArrayList<String> serviceAdditionalInfo;
     @SerializedName("deliveryDescription")
    @Expose
    private String descriptiondelivery;
    @SerializedName("orderId")
    @Expose
    private String orderId;
    @SerializedName("adminId")
    @Expose
    private Admin adminNumber;
    @SerializedName("requestStatus")
    @Expose
    private String requestStatus;
    @SerializedName("customerName")
    @Expose
    private String customerName;
    @SerializedName("quoteStatus")
    @Expose
    private String quoteStatus;
    @SerializedName("parcelDropLocationDetails")
    @Expose
    private List<GoodsDropLocationDetail> parcelDropLocationDetails ;
    @SerializedName("inventoryDropLocationDetails")
    @Expose
    private List<GoodsDropLocationDetail> inventoryDropLocationDetails;
    @SerializedName("pickupLocation")
    @Expose
    private PickupLocation pickupLocation;
    @SerializedName("customerPhoneNo")
    @Expose
    private Phone customerPhoneNo;
    @SerializedName("serviceScope")
    @Expose
    private VehicalType serviceScope;

    @SerializedName("vehicleType")
    @Expose
    private VehicalType vehicleType;
    @SerializedName("customerId")
    @Expose
    private Customer customerInfo;
    @SerializedName("inventoryDetails")
    @Expose
    private List<InventoryName> inventoryList;

    public String getId() {
        return Id;
    }

    public String getServiceType() {
        return serviceType;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getRequestStatus() {
        return requestStatus;
    }


    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getQuoteStatus() {
        return quoteStatus;
    }

    public List<GoodsDropLocationDetail> getParcelDropLocationDetails() {
        return parcelDropLocationDetails;
    }


    public String getPickupLocation() {

        if(pickupLocation==null)
            return null;

        return pickupLocation.getFullAddress();
    }
    public PickupLocation getPickupData() {



        return pickupLocation;
    }

    public Phone getCustomerPhoneNo() {
        return customerPhoneNo;
    }

    public StringBuilder getServiceAdditionalInfo() {
        StringBuilder additional =null;

        if (serviceAdditionalInfo != null) {


            for (String services :serviceAdditionalInfo) {

                if (additional == null) {
                    additional= new StringBuilder();
                    additional.append(services);
                } else {
                    additional.append(", "+services);

                }


            }

        }
        return additional;
    }

    public VehicalType getServiceScope() {
        return serviceScope;
    }

    public VehicalType getVehicleType() {
        return vehicleType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Customer getCustomerInfo() {
        return customerInfo;
    }


    public GoodsDropLocationDetail getParcelDrop() {

        if(serviceType!=null&&serviceType.equals(ServiceType.FREIGHT.name())){
            if(inventoryDropLocationDetails!=null&&inventoryDropLocationDetails.size()>0)
                return     inventoryDropLocationDetails.get(0);

        }else{
            if(parcelDropLocationDetails!=null&&parcelDropLocationDetails.size()>0)
                return     parcelDropLocationDetails.get(0);
        }


/*        int size=parcelDropLocationDetails.size();
        if(size>0)
            return  parcelDropLocationDetails.get(0);*/
        return null;
    }
    public String getParcelDropLocation() {


      if(serviceType!=null&&serviceType.equals(ServiceType.FREIGHT.name())){
            if(inventoryDropLocationDetails!=null&&inventoryDropLocationDetails.size()>0)
                return     inventoryDropLocationDetails.get(0).getFullAddress();

        }else{
            if(parcelDropLocationDetails!=null&&parcelDropLocationDetails.size()>0)
                return     parcelDropLocationDetails.get(0).getFullAddress();
        }


        return null;


    /*    if(parcelDropLocationDetails.size()>0)
            return     parcelDropLocationDetails.get(0).getFullAddress();
       ;*/
    }

    public List<InventoryName> getInventoryList() {
        if(inventoryDropLocationDetails!=null&&inventoryDropLocationDetails.size()>0&&inventoryDropLocationDetails.get(0).getInventoryDetail()!=null){
            return inventoryDropLocationDetails.get(0).getInventoryDetail();
        }

        return new ArrayList<InventoryName>();
    }

    public String getDescriptiondelivery() {
        return descriptiondelivery;
    }

    public String getAdminNumber() {

        if(adminNumber==null)
        return null;
        return adminNumber.getPhoneNumber();
    }
}
