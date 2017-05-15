package com.fast.van.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
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
import com.fast.van.common.SessionManager;
import com.fast.van.config.Constants;
import com.fast.van.dialogs.CommonDialog;
import com.fast.van.loadingindicator.LoadingBox;
import com.fast.van.models.Command;
import com.fast.van.models.ServiceReply;
import com.fast.van.models.login.Login;
import com.fast.van.models.order.Customer;
import com.fast.van.models.order.Inventory;
import com.fast.van.models.order.InventoryName;
import com.fast.van.models.order.Order;
import com.fast.van.models.order.OrderDetails;
import com.fast.van.models.order.GoodsDropLocationDetail;
import com.fast.van.models.signup.ServiceType;
import com.fast.van.models.signup.VehicalType;
import com.fast.van.retrofit.RestClient;
import com.fast.van.transformer.DepthPageTransformer;
import com.fast.van.transformer.Transformer;
import com.fast.van.utils.BaseUtils;
import com.fast.van.utils.CommonData;
import com.fast.van.utils.ErrorCodes;
import com.fast.van.utils.Log;
import com.fast.van.utils.file.AppFileUtils;


import net.fastvan.indicator.PageIndicator;
import net.fastvan.indicator.SimpleCirclePageIndicator;

import java.util.Calendar;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Amandeep Singh Bagli on 23/10/15.
 */
public class ActivityNewRequest extends BaseActivity {
    private ViewPager mPager;
    private PageIndicator mIndicator;
    private ImageView mImageView;
    private String message = null;
    private String orderId = null;
    private String customerNumber;
    private TextView vehicleName;
    private ImageView vehicleicon;
    private View pagerLayout;
    private View inventoryLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fastvan_activity_new_request);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            message = extras.getString("message");
            orderId = extras.getString("orderId");
        }


        init();
    }


    //private float RATIO_SCALE=0.2f;


    private void init() {
        findViewById(R.id.toolbarwithtitleNlogo).setVisibility(View.INVISIBLE);
        findViewById(R.id.makeCall).setOnClickListener(this);
        findViewById(R.id.viewDetailsDelivery).setOnClickListener(this);
        mPager = (ViewPager) findViewById(R.id.pager);
        mIndicator = (SimpleCirclePageIndicator) findViewById(R.id.indicator);
        mImageView = (ImageView) findViewById(R.id.imgAvatar);
        vehicleName = (TextView) findViewById(R.id.textVehicle);
        vehicleicon = (ImageView) findViewById(R.id.iconVehicle);
        pagerLayout = findViewById(R.id.viewPaggerLayout);
        inventoryLayout = findViewById(R.id.inventoryDetailsLayout);
        CommonData.removeNotificationData(activity);

        Glide.with(this)
                .load(R.drawable.icon_avatar_big)
                .fitCenter()
                .into(mImageView);
        findViewById(R.id.accept).setOnClickListener(this);
        findViewById(R.id.reject).setOnClickListener(this);
        findViewById(R.id.viewDetails).setOnClickListener(this);


        LoadingBox.showLoadingDialog(activity, "Loading...");
        getOrder();

    }


    void getOrder() {


        String json = "";

        if (!SessionManager.instance.lastUpdatedExpired(activity, Constants.DataRequest.ORDERS)) {
            Log.e(TAG, "data from file");
            json = AppFileUtils.readDataFile(Constants.DataRequest.ORDERS + ".json", activity);
            Log.e(TAG, "data:" + json);
            Order order = BaseUtils.getOBJFromJSON(json, Order.class);
            initform(order);
            LoadingBox.dismissLoadingDialog();
        }

        if (BaseUtils.isNullOrEmpty(json)) {
            Login user = CommonData.getSessionData(activity);
            Log.e(TAG, "getOrder:LOADING...");
            if (user != null && !user.getAccessToken().isEmpty()) {
                Log.e(TAG, "getAccessToken:" + user.getAccessToken());

                RestClient.getApiServiceForPojo().getBookinginfo(user.getAccessToken(), orderId, new Callback<Order>() {
                    @Override
                    public void success(Order order, Response response) {
                        Log.e(TAG, "getDriverInfo:NETWORK:" + order.toString());
                        AppFileUtils.saveDataFile(Constants.DataRequest.ORDERS + ".json", BaseUtils.getJSONFromOBJ(order), activity);
                        SessionManager.instance.addSpecificLastRequested(activity, Constants.DataRequest.ORDERS, Calendar.getInstance());
                        initform(order);
                        LoadingBox.dismissLoadingDialog();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        LoadingBox.dismissLoadingDialog();
                        ErrorCodes.checkCode(activity, error);
                        finish();
                    }
                });

            }

        }


    }

    private OrderDetails orderDetails;

    private void initform(Order order) {
        TextView serviceType = (TextView) findViewById(R.id.servicetype);
        TextView schduletime = (TextView) findViewById(R.id.scheduledtime);
        TextView additionalinfo = (TextView) findViewById(R.id.additionalinfo);
        TextView pickuploaction = (TextView) findViewById(R.id.pickuplocation);
        TextView customerName = (TextView) findViewById(R.id.txtFullName);
        TextView deliverylocation = (TextView) findViewById(R.id.deliveryAddress);
        TextView tvInventoryList = (TextView) findViewById(R.id.tvInventoryList);

        orderDetails = order.getData();
        if (orderDetails != null) {
            if (orderDetails.getServiceType().equals(ServiceType.DELIVERY.name()))
                serviceType.setText(orderDetails.getServiceType() + " - " + orderDetails.getDescriptiondelivery());
            else
                serviceType.setText(orderDetails.getServiceType());

            schduletime.setText(BaseUtils.stringToDate(orderDetails.getScheduledTime()));
            additionalinfo.setText(orderDetails.getServiceAdditionalInfo());
            pickuploaction.setText(orderDetails.getPickupLocation());
            customerName.setText(orderDetails.getCustomerName());
            deliverylocation.setText(orderDetails.getParcelDropLocation());
            Customer customer = orderDetails.getCustomerInfo();


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

            }

            if(orderDetails.getCustomerPhoneNo()!=null)
                customerNumber=orderDetails.getCustomerPhoneNo().getPhoneNumber();
//                customerNumber = orderDetails.getAdminNumber() ;
            if(customerNumber==null)
                findViewById(R.id.makeCall).setVisibility(View.INVISIBLE);
            if (orderDetails.getServiceType().equals(ServiceType.COURIER.name()))
                vehicleType(orderDetails.getServiceScope());
            else
                vehicleType(orderDetails.getVehicleType());


            if (!orderDetails.getRequestStatus().equals("DRIVER_ASSIGNED")) {
                findViewById(R.id.llAcceptReject).setVisibility(View.GONE);


            }


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


            //   FragmentDelivery.updateStatus(orderDetails.getRequestStatus());
            int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10 * 2, getResources().getDisplayMetrics());
            if (order.getData() != null && order.getData().getParcelDropLocationDetails().size() > 0) {
                DeliveryViewPagerAdapter adapter = new DeliveryViewPagerAdapter(order.getData().getParcelDropLocationDetails().get(0).getParcelDetail(), order.getData().getOrderId(), getSupportFragmentManager());
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
                acceptServiceCall();
                break;
            case R.id.reject:
                // Toast.makeText(activity,"Under Construction",Toast.LENGTH_LONG).show();
                rejectServiceCall();
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
                    finish();

                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, "FASTVAN APP FOREGROUND: " + BaseActivity.isForeGround());
                    LoadingBox.dismissLoadingDialog();
                    ErrorCodes.checkCode(activity, error);

                    Log.e(TAG, "FASTVAN APP FOREGROUND: " + BaseActivity.isForeGround());
                    finish();
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
                    finish();
                }

                @Override
                public void failure(RetrofitError error) {
                    LoadingBox.dismissLoadingDialog();
                    ErrorCodes.checkCode(activity, error);
                    finish();


                }
            });
        }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
