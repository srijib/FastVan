package com.fast.van.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.fast.van.R;
import com.fast.van.dialogs.CommonDialog;
import com.fast.van.fragments.GoogleMapFragment;
import com.fast.van.loadingindicator.LoadingBox;
import com.fast.van.models.Command;
import com.fast.van.models.ServiceReply;
import com.fast.van.models.login.Login;
import com.fast.van.models.order.Customer;
import com.fast.van.models.order.GoodsDropLocationDetail;
import com.fast.van.models.order.OrderDetails;
import com.fast.van.retrofit.RestClient;
import com.fast.van.transformer.Transformer;
import com.fast.van.utils.BaseUtils;
import com.fast.van.utils.CommonData;
import com.fast.van.utils.ErrorCodes;
import com.fast.van.utils.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Amandeep Singh Bagli on 11/01/16.
 */
public class ActivityTodaysBookingOnMap extends LocationBaseActivity implements GoogleMapFragment.OnGoogleMapFragmentListener {

    private GoogleMap googleMap;
    private Marker myLocationMarker;
    private static String ORDERKEY = "orderDetails";
    private String customerNumber;

    private OrderDetails orderDetails;
    private Command requestStatus;
    private Command updateStatus;
    private AppCompatButton statusButton;
    private boolean isClearMap;
    private Marker targetGoogleMarker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fastvan_activity_todays_booking);
        TextView title = (TextView) findViewById(R.id.screenTitle);
        title.setText("My Booking");
        findViewById(R.id.backbutton).setOnClickListener(this);
        statusButton = (AppCompatButton) findViewById(R.id.submit);
        statusButton.setOnClickListener(this);


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


        try {
            if (getIntent().getExtras() != null) {

                orderDetails = BaseUtils.getOBJFromJSON(getIntent().getExtras().getString(ORDERKEY), OrderDetails.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, GoogleMapFragment.newInstance(this), "googlemap");
        fragmentTransaction.commit();

        if (orderDetails != null)
            initForm(orderDetails);
    }


    private void initForm(OrderDetails orderDetails) {
        TextView pickupLocation = (TextView) findViewById(R.id.pickuplocation);
        TextView deliverylocation = (TextView) findViewById(R.id.deliverylocation);
        TextView servicetype = (TextView) findViewById(R.id.servicetype);
        TextView pickuploaction = (TextView) findViewById(R.id.pickuplocation);
        TextView parcelDetails = (TextView) findViewById(R.id.parcelDetails);
        TextView customername = (TextView) findViewById(R.id.customerName);
        final ImageView imageview = (ImageView) findViewById(R.id.imgAvatar);
        customername.setText(orderDetails.getCustomerName());
        pickupLocation.setText(orderDetails.getPickupLocation());
        servicetype.setText(orderDetails.getServiceType());

        if(updateProgress(orderDetails.getRequestStatus())<33){
            deliverylocation.setText(getString(R.string.pickuplocation));
            pickuploaction.setText(orderDetails.getPickupLocation());
        }else{
            deliverylocation.setText(getString(R.string.dropofflocation));
            pickuploaction.setText(orderDetails.getParcelDropLocation());
        }



        parcelDetails.setOnClickListener(this);
        findViewById(R.id.viewDetails).setOnClickListener(this);
        findViewById(R.id.makeCall).setOnClickListener(this);
        if (orderDetails.getCustomerPhoneNo() != null)
            customerNumber = orderDetails.getCustomerPhoneNo().getPhoneNumber();
//        customerNumber=orderDetails.getAdminNumber();
        if (customerNumber == null)
            findViewById(R.id.makeCall).setVisibility(View.INVISIBLE);

        Customer customer = orderDetails.getCustomerInfo();
        if (customer != null) {
            customername.setText(customer.getFullName());

            if (customer.getCustomerImageUrls() != null) {
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
                                imageview.setImageBitmap(bitmap);

                            }
                        });
            }
        }


        updateStatus(orderDetails.getRequestStatus());
        setStatus();
        if (googleMap != null) {
            googleMap.clear();
            isClearMap = true;
            onMapReady(googleMap);
        }

    }

    @Override
    public void onLocationUpdate(Location location) {
        if (googleMap != null) {
            Log.d("googlemap", "onMapReady initialized :)");
            //googleMap.clear();

            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            if (myLocationMarker == null || isClearMap) {
                isClearMap = false;
                myLocationMarker = googleMap.addMarker(new MarkerOptions()
                        .position(myLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_van_icon)));

              //       CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(myLocation, 13f);
             //   googleMap.moveCamera(cameraUpdate);


                LatLngBounds.Builder b = new LatLngBounds.Builder();
               // for (Marker m : mMarkers) {
                    b.include(myLocationMarker.getPosition());
                if(targetGoogleMarker!=null)
                    b.include(targetGoogleMarker.getPosition());
               // }
                LatLngBounds bounds = b.build();
//Change the padding as per needed
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 25, 25, 5);
                googleMap.animateCamera(cu);



            } else
                myLocationMarker.setPosition(myLocation);

            //  map.animateCamera(cameraUpdate);

//            googleMap.getUiSettings().setMyLocationButtonEnabled(true);


        } else {
            Log.d("googlemap", "onMapReady error :(");
        }
    }

    @Override
    public void onGoogleConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onClickView(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.viewDetails:

                Intent intent = new Intent(activity, ActivityViewDetailsMap.class);
                if(orderDetails != null&&updateProgress(orderDetails.getRequestStatus())<33) {

                    intent.putExtra(ActivityViewDetailsMap.TITLEKEY, getString(R.string.pickuplocation));
                    intent.putExtra(ActivityViewDetailsMap.ISPICKUP, true);

                        intent.putExtra(ActivityViewDetailsMap.SCREENDATA, "" + BaseUtils.getJSONFromOBJ(orderDetails.getPickupData()));
                        startActivity(intent);
                        Transformer.rightToLeft(activity);

                }else {
                    intent.putExtra(ActivityViewDetailsMap.TITLEKEY, getString(R.string.dropofflocation));
                    intent.putExtra(ActivityViewDetailsMap.ISPICKUP, false);
                    if (orderDetails != null) {
                        GoodsDropLocationDetail locationDetail = orderDetails.getParcelDrop();
                        if (locationDetail != null)
                            intent.putExtra(ActivityViewDetailsMap.SCREENDATA, "" + BaseUtils.getJSONFromOBJ(locationDetail.getDropLocationDetail()));

                        startActivity(intent);
                        Transformer.rightToLeft(activity);
                    }


                }

                break;
            case R.id.parcelDetails:
                if (orderDetails != null)
                    parcelClicked();
                else
                    Toast.makeText(activity, "parcel clicked", Toast.LENGTH_LONG).show();
                break;
            case R.id.makeCall:
                if (customerNumber != null && !customerNumber.isEmpty()) {
                    BaseUtils.makeCall(customerNumber, activity);
                }
                break;
            case R.id.submit:
                if (statusButton.getText().toString().equals(getString(R.string.accept)))
                    acceptServiceCall();
                else
                    updateStatus();
                break;
            case R.id.backbutton:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        if (googleMap != null) {
            googleMap.setMyLocationEnabled(true);
            Log.d("googlemap", "onMapReady initialized :)");


            if (orderDetails != null && updateProgress(orderDetails.getRequestStatus()) < 33 && orderDetails.getPickupData() != null) {
                LatLng pickuplocation = new LatLng(orderDetails.getPickupData().getLatitude(), orderDetails.getPickupData().getLongitude());
                targetGoogleMarker= googleMap.addMarker(new MarkerOptions()
                        .position(pickuplocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location_pin)));
               // CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(pickuplocation, 13f);
               // googleMap.moveCamera(cameraUpdate);

                LatLngBounds.Builder b = new LatLngBounds.Builder();
                // for (Marker m : mMarkers) {
                b.include(targetGoogleMarker.getPosition());
                if(myLocationMarker!=null)
                    b.include(targetGoogleMarker.getPosition());
                // }
                LatLngBounds bounds = b.build();
//Change the padding as per needed
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 25, 25, 5);
                googleMap.animateCamera(cu);

            } else {
                GoodsDropLocationDetail dropLocationDetail = orderDetails.getParcelDrop();
                if (dropLocationDetail != null && dropLocationDetail.getDropLocationDetail() != null) {
                    LatLng pickuplocation = new LatLng(dropLocationDetail.getDropLocationDetail().getLatitude(), dropLocationDetail.getDropLocationDetail().getLongitude());
                    targetGoogleMarker= googleMap.addMarker(new MarkerOptions()
                            .position(pickuplocation)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location_pin)));
              //      CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(pickuplocation, 13f);
                 //   googleMap.moveCamera(cameraUpdate);




                    LatLngBounds.Builder b = new LatLngBounds.Builder();
                    // for (Marker m : mMarkers) {
                    b.include(targetGoogleMarker.getPosition());
                    if(myLocationMarker!=null)
                        b.include(targetGoogleMarker.getPosition());
                    // }
                    LatLngBounds bounds = b.build();
//Change the padding as per needed
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 25, 25, 5);
                    googleMap.animateCamera(cu);


                }
            }
            googleMap.setMyLocationEnabled(true);
/*
            LatLng MELBOURNE = new LatLng(-37.813, 144.962);
            Marker melbourne = googleMap.addMarker(new MarkerOptions()
                    .position(MELBOURNE)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_van_icon)));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(MELBOURNE, 10);
          //  map.animateCamera(cameraUpdate);
            map.moveCamera(cameraUpdate);
//            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            googleMap.setMyLocationEnabled(true);*/


        } else {
            Log.d("googlemap", "onMapReady error :(");
        }
    }

    private void parcelClicked() {

        try {
            orderDetails.setRequestStatus(requestStatus.name());
        } catch (Exception e) {
            Log.e(TAG, "setRequestStatus", e);
        }


        Intent intent = new Intent(activity, ActivityBookingDetails.class);
        intent.putExtra("orderDetails", BaseUtils.getJSONFromOBJ(orderDetails));
        startActivityForResult(intent, 55);
        Transformer.rightToLeft(activity);
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
                    orderDetails.setRequestStatus(updateStatus.name());
                    updateStatus(updateStatus.name());
                    if (googleMap != null) {
                        googleMap.clear();
                        isClearMap = true;
                        onMapReady(googleMap);
                    }
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
            return;
        }
        if (status.equals("DRIVER_ACCEPTED")) {
            requestStatus = Command.DRIVER_ACCEPTED;

            return;
        }
        if (status.equals("REACHED_PICKUP_POINT")) {
            requestStatus = Command.REACHED_PICKUP_POINT;
            return;
        }
        if (status.equals("PICKED_UP")) {
            requestStatus = Command.PICKED_UP;
            return;
        }
        if (status.equals("REACHED_DELIVERY_POINT")) {
            requestStatus = Command.REACHED_DELIVERY_POINT;
            return;
        }
        if (status.equals("ORDER_DELIVERED")) {
            requestStatus = Command.ORDER_DELIVERED;
            return;
        }

        if (status.equals("CANCELLED")) {
            requestStatus = Command.CANCELLED;
            return;
        }
        if (status.equals("REFUND")) {
            requestStatus = Command.REFUND;
            return;
        }


    }

    private void setStatus() {


        switch (requestStatus) {
            case DRIVER_ASSIGNED:
                break;
            case DRIVER_ACCEPTED:
                statusButton.setText(getString(R.string.arrived));
                updateStatus = Command.REACHED_PICKUP_POINT;
                break;
            case REACHED_PICKUP_POINT:
                statusButton.setText(getString(R.string.pickup));
                updateStatus = Command.PICKED_UP;
                break;
            case PICKED_UP:
                statusButton.setText(getString(R.string.delivering));
                updateStatus = Command.REACHED_DELIVERY_POINT;
                break;
            case REACHED_DELIVERY_POINT:
                statusButton.setText(getString(R.string.delivered));
                updateStatus = Command.ORDER_DELIVERED;
                break;
            case ORDER_DELIVERED:
                statusButton.setVisibility(View.GONE);
                break;
            default:
                statusButton.setVisibility(View.GONE);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, requestCode + ":Result:" + resultCode);

        //do code to update booking
        if (resultCode == 55) {
            if (data != null && data.hasExtra("orderDetails")) {
                orderDetails = BaseUtils.getOBJFromJSON(data.getStringExtra(ORDERKEY), OrderDetails.class);
                if (orderDetails != null) {
                    initForm(orderDetails);
                } else {
                    setResult(55);
                    finish();
                }

            }
        }
    }


    private int updateProgress(String status) {


        if (status.equals("DRIVER_ASSIGNED")) {

            return 5;
        }
        if (status.equals("DRIVER_ACCEPTED")) {


            return 10;
        }
        if (status.equals("REACHED_PICKUP_POINT")) {

            return 15;
        }
        if (status.equals("PICKED_UP")) {

            return 33;
        }
        if (status.equals("REACHED_DELIVERY_POINT")) {

            return 66;
        }
        if (status.equals("ORDER_DELIVERED")) {

            return 100;
        }

        return 0;
    }

    @Override
    public void onBackPressed() {
        setResult(99);
        super.onBackPressed();
        Transformer.leftToRight(activity);

    }
}
