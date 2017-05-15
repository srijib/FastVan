package com.fast.van.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fast.van.R;
import com.fast.van.adapters.DeliveryViewPagerAdapter;
import com.fast.van.adapters.InventoryViewPagerAdapter;
import com.fast.van.dialogs.CommonDialog;
import com.fast.van.loadingindicator.LoadingBox;
import com.fast.van.models.Command;
import com.fast.van.models.ServiceReply;
import com.fast.van.models.login.Login;
import com.fast.van.models.order.Customer;
import com.fast.van.models.order.Inventory;
import com.fast.van.models.order.InventoryName;
import com.fast.van.models.order.OrderDetails;
import com.fast.van.models.order.GoodsDropLocationDetail;
import com.fast.van.models.order.Phone;
import com.fast.van.models.signup.ServiceType;
import com.fast.van.models.signup.VehicalType;
import com.fast.van.retrofit.RestClient;
import com.fast.van.transformer.DepthPageTransformer;
import com.fast.van.transformer.Transformer;
import com.fast.van.utils.BaseUtils;
import com.fast.van.utils.CommonData;
import com.fast.van.utils.ErrorCodes;
import com.fast.van.utils.Log;

import net.fastvan.indicator.PageIndicator;
import net.fastvan.indicator.SimpleCirclePageIndicator;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Amandeep Singh Bagli on 26/10/15.
 */
public class ActivityBookingDetails extends BaseActivity {

    private ViewPager mPager;
    private PageIndicator mIndicator;
    private ImageView mImageView;
    private OrderDetails orderDetails = null;
    private String customerNumber;
    private TextView vehicleName;
    private ImageView vehicleicon;
    private Command requestStatus;
    private Command updateStatus;
    private Command currentStatus;
    private AppCompatButton statusButton;
    private View pagerLayout;
    private View inventoryLayout;
    private TextView title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fastvan_activity_new_request);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            orderDetails = BaseUtils.getOBJFromJSON(extras.getString("orderDetails"), OrderDetails.class);
        }
        init();
    }


    //private float RATIO_SCALE=0.2f;


    private void init() {
        pagerLayout = findViewById(R.id.viewPaggerLayout);
        inventoryLayout = findViewById(R.id.inventoryDetailsLayout);
        vehicleName = (TextView) findViewById(R.id.textVehicle);
        vehicleicon = (ImageView) findViewById(R.id.iconVehicle);
        mPager = (ViewPager) findViewById(R.id.pager);
        mIndicator = (SimpleCirclePageIndicator) findViewById(R.id.indicator);
        mImageView = (ImageView) findViewById(R.id.imgAvatar);
        findViewById(R.id.backbutton).setOnClickListener(this);
        findViewById(R.id.makeCall).setOnClickListener(this);
        findViewById(R.id.viewDetailsDelivery).setOnClickListener(this);
        title = (TextView) findViewById(R.id.screenTitle);
        title.setText("My Booking");
        Glide.with(this)
                .load(R.drawable.icon_avatar_big)
                .fitCenter()
                .into(mImageView);
        statusButton = (AppCompatButton) findViewById(R.id.accept);
        statusButton.setOnClickListener(this);
        findViewById(R.id.reject).setOnClickListener(this);
        findViewById(R.id.viewDetails).setOnClickListener(this);
        initform();
    }


    private void initform() {
        TextView serviceType = (TextView) findViewById(R.id.servicetype);
        TextView schduletime = (TextView) findViewById(R.id.scheduledtime);
        TextView additionalinfo = (TextView) findViewById(R.id.additionalinfo);
        TextView pickuploaction = (TextView) findViewById(R.id.pickuplocation);
        TextView customerName = (TextView) findViewById(R.id.txtFullName);
        TextView deliverylocation = (TextView) findViewById(R.id.deliveryAddress);

        TextView tvInventoryList = (TextView) findViewById(R.id.tvInventoryList);
        if (orderDetails != null) {
            if (orderDetails.getServiceType().equals(ServiceType.DELIVERY.name()))
                serviceType.setText(orderDetails.getServiceType() + " - " + orderDetails.getDescriptiondelivery());
            else
                serviceType.setText(orderDetails.getServiceType());
            schduletime.setText(BaseUtils.stringToDate(orderDetails.getScheduledTime()));
            additionalinfo.setText(orderDetails.getServiceAdditionalInfo());
            pickuploaction.setText(orderDetails.getPickupLocation());
            customerName.setText(orderDetails.getCustomerName());
            Customer customer = orderDetails.getCustomerInfo();
            deliverylocation.setText(orderDetails.getParcelDropLocation());


            if (orderDetails.getServiceType() != null && orderDetails.getServiceType().equals(ServiceType.FREIGHT.name())) {
                pagerLayout.setVisibility(View.GONE);
                inventoryLayout.setVisibility(View.VISIBLE);


                if (orderDetails.getInventoryList() != null) {
                    StringBuilder list = new StringBuilder();
                    for (InventoryName inventoryName : orderDetails.getInventoryList()) {

                        for (Inventory inventory : inventoryName.getData()) {
                            if (list.toString().isEmpty()) {
                                list.append(inventory.getName() + ":" + inventory.getQuantity());
                            } else {
                                list.append(", \n" + inventory.getName() + ":" + inventory.getQuantity());
                            }
                        }


                    }


                    tvInventoryList.setText(list.toString());

                }

            } else {
                pagerLayout.setVisibility(View.VISIBLE);
                inventoryLayout.setVisibility(View.GONE);
            }


            pagerLayout.setVisibility(View.VISIBLE);
            inventoryLayout.setVisibility(View.GONE);


            if (customer != null) {
                customerName.setText(customer.getFullName());

                    if(customer.getCustomerImageUrls()!=null){
                        Glide.with(this)
                                .load(customer.getCustomerImageUrls().getProfilePicture())
                                .asBitmap()
                                .toBytes()
                                .centerCrop()
                                .into(new SimpleTarget<byte[]>(200, 200) {
                                    @Override
                                    public void onResourceReady(byte[] data, GlideAnimation anim) {
                                        // Post your bytes to a background thread and upload them here.
                                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        mImageView.setImageBitmap(bitmap);

                                    }
                                });
                    }


                 /*   Glide.with(this)
                            .load(customer.getCustomerImageUrls().getProfilePicture())
                            .placeholder(R.drawable.icon_avatar_big)
                            .fitCenter()
                            .into(mImageView);*/
            }
            /*else
                findViewById(R.id.makeCall).setVisibility(View.INVISIBLE);*/

            if(orderDetails.getCustomerPhoneNo()!=null)
            customerNumber=orderDetails.getCustomerPhoneNo().getPhoneNumber();

            if (customerNumber== null) {
                findViewById(R.id.makeCall).setVisibility(View.INVISIBLE);
            }
            if (orderDetails.getServiceType().equals(ServiceType.COURIER.name()))
                vehicleType(orderDetails.getServiceScope());
            else
                vehicleType(orderDetails.getVehicleType());
            setUpPager();

        }


    }

    private void setUpPager() {

        updateStatus(orderDetails.getRequestStatus());
        if (!orderDetails.getRequestStatus().equals("DRIVER_ASSIGNED")) {
            //  findViewById(R.id.llAcceptReject).setVisibility(View.GONE);
            if (requestStatus == null || requestStatus.equals(Command.ORDER_DELIVERED)|| requestStatus.equals(Command.CANCELLED)|| requestStatus.equals(Command.REFUND)) {
                statusButton.setVisibility(View.GONE);
            } else {
                setStatus();
                statusButton.setVisibility(View.VISIBLE);
            }
        }

        //  FragmentDelivery.updateStatus(orderDetails.getRequestStatus());

        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10 * 2, getResources().getDisplayMetrics());
        if (orderDetails != null && orderDetails.getParcelDropLocationDetails() != null && orderDetails.getParcelDropLocationDetails().size() > 0) {
            DeliveryViewPagerAdapter adapter = new DeliveryViewPagerAdapter(orderDetails.getParcelDropLocationDetails().get(0).getParcelDetail(), orderDetails.getOrderId(), getSupportFragmentManager());
            mPager.setAdapter(adapter);
            mPager.setClipToPadding(false);
            //mPager.setPageMargin(-margin);
            mPager.setPadding(margin, 0, margin, 0);
            // mPager.setPaddingRelative(margin,0,margin,0);
            mPager.setOffscreenPageLimit(3);

            mIndicator.setViewPager(mPager);
            mPager.setPageTransformer(true, new DepthPageTransformer());
        }

        if (orderDetails.getServiceType() != null && orderDetails.getServiceType().equals(ServiceType.FREIGHT.name())) {
            if (orderDetails.getInventoryList() != null) {
                InventoryViewPagerAdapter adapter = new InventoryViewPagerAdapter(orderDetails.getInventoryList(), getSupportFragmentManager());
                mPager.setAdapter(adapter);
                mPager.setClipToPadding(false);
                //mPager.setPageMargin(-margin);
                mPager.setPadding(margin, 0, margin, 0);
                // mPager.setPaddingRelative(margin,0,margin,0);
                mPager.setOffscreenPageLimit(3);

                mIndicator.setViewPager(mPager);
                mPager.setPageTransformer(true, new DepthPageTransformer());
            } else
                pagerLayout.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClickView(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.viewDetails:
                Intent intent = new Intent(activity, ActivityViewDetailsMap.class);
                intent.putExtra(ActivityViewDetailsMap.TITLEKEY, getString(R.string.pickuplocation));
                intent.putExtra(ActivityViewDetailsMap.ISPICKUP, true);
                if (orderDetails != null) {
                    intent.putExtra(ActivityViewDetailsMap.SCREENDATA, "" + BaseUtils.getJSONFromOBJ(orderDetails.getPickupData()));
                }


                startActivity(intent);
                Transformer.rightToLeft(activity);
                break;
            case R.id.viewDetailsDelivery:
                intent = new Intent(activity, ActivityViewDetailsMap.class);
                intent.putExtra(ActivityViewDetailsMap.TITLEKEY, getString(R.string.dropofflocation));
                intent.putExtra(ActivityViewDetailsMap.ISPICKUP, false);
                if (orderDetails != null) {
                    GoodsDropLocationDetail locationDetail = orderDetails.getParcelDrop();
                    if (locationDetail != null)
                        intent.putExtra(ActivityViewDetailsMap.SCREENDATA, "" + BaseUtils.getJSONFromOBJ(locationDetail.getDropLocationDetail()));
                }


                startActivity(intent);
                Transformer.rightToLeft(activity);
                break;
            case R.id.accept:
                if (statusButton.getText().toString().equals(getString(R.string.accept)))
                    acceptServiceCall();
                else
                    updateStatus();
                break;
            case R.id.reject:
                // Toast.makeText(activity,"Under Construction",Toast.LENGTH_LONG).show();
                rejectServiceCall();
                break;
            case R.id.backbutton:
                onBackPressed();
                break;
            case R.id.makeCall:
                if (customerNumber != null && !customerNumber.isEmpty()) {
                    BaseUtils.makeCall(customerNumber, activity);
                } else
                    findViewById(R.id.makeCall).setVisibility(View.INVISIBLE);
                // Toast.makeText(activity,"under Construction",Toast.LENGTH_LONG).show();
                break;
        }
    }

    private void acceptServiceCall() {
        Log.e(TAG, "FASTVAN APP FOREGROUND: " + BaseActivity.isForeGround());
        Login user = CommonData.getSessionData(activity);

        if (user != null && orderDetails != null) {
            LoadingBox.showLoadingDialog(activity, "Loading...");
            RestClient.getApiServiceForPojo().acceptRequestByPartner(user.getAccessToken(), "" + orderDetails.getOrderId(), Command.ACCEPT, new Callback<ServiceReply>() {
                @Override
                public void success(ServiceReply s, Response response) {
                    Log.d(TAG, "changePassword:" + s.toString());
                    Log.e(TAG, "FASTVAN APP FOREGROUND: " + BaseActivity.isForeGround());
                    LoadingBox.dismissLoadingDialog();

                    CommonDialog.With(activity).show(s.getMessage());
                    Log.e(TAG, "FASTVAN APP FOREGROUND: " + BaseActivity.isForeGround());
                    orderDetails.setRequestStatus(Command.DRIVER_ACCEPTED.name());
                    // setUpPager();
                    updateStatus("DRIVER_ACCEPTED");
                    setStatus();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "FASTVAN APP FOREGROUND: " + BaseActivity.isForeGround());
                    LoadingBox.dismissLoadingDialog();
                    ErrorCodes.checkCode(activity, error);

                    Log.e(TAG, "FASTVAN APP FOREGROUND: " + BaseActivity.isForeGround());

                }
            });
        }

    }

    private void rejectServiceCall() {
        Login user = CommonData.getSessionData(activity);

        if (user != null && orderDetails != null) {
            LoadingBox.showLoadingDialog(activity, "Loading...");
            RestClient.getApiServiceForPojo().acceptRequestByPartner(user.getAccessToken(), "" + orderDetails.getOrderId(), Command.REJECT, new Callback<ServiceReply>() {
                @Override
                public void success(ServiceReply s, Response response) {
                    Log.d(TAG, "changePassword:" + s.toString());

                    LoadingBox.dismissLoadingDialog();

                    CommonDialog.With(activity).show(s.getMessage());
                    onBackPressed();

                }

                @Override
                public void failure(RetrofitError error) {
                    LoadingBox.dismissLoadingDialog();
                    ErrorCodes.checkCode(activity, error);


                }
            });
        }

    }

    @Override
    public void onBackPressed() {

        try {
            Intent intent = new Intent();
            orderDetails.setRequestStatus(currentStatus.name());
            intent.putExtra("orderDetails", BaseUtils.getJSONFromOBJ(orderDetails));
            setResult(55, intent);
        } catch (Exception e) {
            Log.e(TAG, "setResult ", e);
        }

        super.onBackPressed();
        //Intent intent=new Intent();


    }

    private void vehicleType(VehicalType vehicalType) {

        if (vehicalType != null)
            switch (vehicalType) {
                case ONE_TON_TRUCK:
                    vehicleicon.setImageResource(R.drawable.icon_3_ton_truck);
                    vehicleName.setText(getString(R.string.oneton));
                    return;
                case TWO_TON_TRUCK:
                    vehicleicon.setImageResource(R.drawable.icon_5_ton_truck);
                    vehicleName.setText(getString(R.string.twoton));
                    return;
                case FOUR_TON_TRUCK:
                    vehicleicon.setImageResource(R.drawable.icon_5_ton_truck);
                    vehicleName.setText(getString(R.string.fourton));
                    return;
                case BIKE:
                    vehicleicon.setImageResource(R.drawable.icon_bike);
                    vehicleName.setText(getString(R.string.bike));
                    return;
                case VAN:
                    vehicleicon.setImageResource(R.drawable.icon_van);
                    vehicleName.setText(getString(R.string.van));
                    return;
                case NATIONAL:
                    vehicleicon.setImageResource(R.drawable.icon_national);
                    vehicleName.setText(getString(R.string.national));
                    return;
                case INTERNATIONAL:
                    vehicleicon.setImageResource(R.drawable.icon_bikeinternational);
                    vehicleName.setText(getString(R.string.international));
                    return;

            }
        vehicleicon.setVisibility(View.INVISIBLE);
        vehicleName.setVisibility(View.INVISIBLE);

    }

    private void setStatus() {


        switch (requestStatus) {
            case DRIVER_ASSIGNED:
                break;
            case ACCEPT:
                statusButton.setText(getString(R.string.arrived));
                title.setText(getString(R.string.arrived));
                updateStatus = Command.REACHED_PICKUP_POINT;
                currentStatus = Command.ACCEPT;
                break;
            case REACHED_PICKUP_POINT:
                statusButton.setText(getString(R.string.pickup));
                title.setText(getString(R.string.pickup));
                updateStatus = Command.PICKED_UP;
                currentStatus = Command.REACHED_PICKUP_POINT;
                break;
            case PICKED_UP:
                statusButton.setText(getString(R.string.delivering));
                title.setText(getString(R.string.delivering));
                updateStatus = Command.REACHED_DELIVERY_POINT;
                currentStatus = Command.PICKED_UP;
                break;
            case REACHED_DELIVERY_POINT:
                statusButton.setText(getString(R.string.delivered));
                title.setText(getString(R.string.delivered));
                updateStatus = Command.ORDER_DELIVERED;
                currentStatus = Command.REACHED_DELIVERY_POINT;
                break;
            case ORDER_DELIVERED:
                statusButton.setVisibility(View.GONE);
                break;
            default:
                statusButton.setVisibility(View.GONE);
        }

    }

    private void updateStatus() {
        Log.e(TAG, "FASTVAN APP FOREGROUND: " + BaseActivity.isForeGround());
        Login user = CommonData.getSessionData(activity);

        if (user != null && orderDetails != null) {
            LoadingBox.showLoadingDialog(activity, "Loading...");
            RestClient.getApiServiceForPojo().updateOrderStatus(user.getAccessToken(), orderDetails.getOrderId(), updateStatus, new Callback<ServiceReply>() {
                @Override
                public void success(ServiceReply s, Response response) {
                    Log.d(TAG, "changePassword:" + s.toString());
                    Log.e(TAG, "FASTVAN APP FOREGROUND: " + BaseActivity.isForeGround());
                    LoadingBox.dismissLoadingDialog();

                    CommonDialog.With(activity).show(s.getMessage());
                    Log.e(TAG, "FASTVAN APP FOREGROUND: " + BaseActivity.isForeGround());

                    updateStatus(updateStatus.name());
                    setStatus();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "FASTVAN APP FOREGROUND: " + BaseActivity.isForeGround());
                    LoadingBox.dismissLoadingDialog();
                    ErrorCodes.checkCode(activity, error);

                    Log.e(TAG, "FASTVAN APP FOREGROUND: " + BaseActivity.isForeGround());

                }
            });
        }
    }

    public void updateStatus(String status) {


        if (status.equals("DRIVER_ASSIGNED")) {
            requestStatus = Command.DRIVER_ASSIGNED;
            currentStatus = Command.DRIVER_ASSIGNED;
            return;
        }
        if (status.equals("DRIVER_ACCEPTED")) {
            requestStatus = Command.ACCEPT;
            currentStatus = Command.ACCEPT;

            return;
        }
        if (status.equals("REACHED_PICKUP_POINT")) {
            requestStatus = Command.REACHED_PICKUP_POINT;
            currentStatus = Command.REACHED_PICKUP_POINT;
            return;
        }
        if (status.equals("PICKED_UP")) {
            requestStatus = Command.PICKED_UP;
            currentStatus = Command.PICKED_UP;
            return;
        }
        if (status.equals("REACHED_DELIVERY_POINT")) {
            requestStatus = Command.REACHED_DELIVERY_POINT;
            currentStatus = Command.REACHED_DELIVERY_POINT;
            return;
        }
        if (status.equals("ORDER_DELIVERED")) {
            requestStatus = Command.ORDER_DELIVERED;
            currentStatus = Command.ORDER_DELIVERED;
            return;
        }

        if (status.equals("CANCELLED")) {
            requestStatus = Command.CANCELLED;
            currentStatus = Command.CANCELLED;
            return;
        }
        if (status.equals("REFUND")) {
            requestStatus = Command.REFUND;
            currentStatus = Command.REFUND;
            return;
        }


    }

    private Command getStatus(Command requestStatus) {


        switch (requestStatus) {
            case DRIVER_ASSIGNED:
                updateStatus = Command.DRIVER_ASSIGNED;
                break;
            case DRIVER_ACCEPTED:
                updateStatus = Command.DRIVER_ASSIGNED;
                break;
            case REACHED_PICKUP_POINT:
                updateStatus = Command.DRIVER_ACCEPTED;
                break;
            case PICKED_UP:
                updateStatus = Command.REACHED_PICKUP_POINT;
                break;
            case REACHED_DELIVERY_POINT:
                updateStatus = Command.PICKED_UP;
                break;
            case ORDER_DELIVERED:
                updateStatus = Command.REACHED_DELIVERY_POINT;
                break;

        }
        return requestStatus;
    }


}
