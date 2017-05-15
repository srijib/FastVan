package com.fast.van.fragments;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fast.van.R;
import com.fast.van.activities.ActivityBookingDetails;
import com.fast.van.activities.ActivityHome;
import com.fast.van.activities.ActivityViewDetailsMap;
import com.fast.van.activities.BaseActivity;
import com.fast.van.dialogs.CommonDialog;
import com.fast.van.loadingindicator.LoadingBox;
import com.fast.van.models.Command;
import com.fast.van.models.ServiceReply;
import com.fast.van.models.login.Login;
import com.fast.van.models.order.OrderDetails;
import com.fast.van.retrofit.RestClient;
import com.fast.van.transformer.Transformer;
import com.fast.van.utils.BaseUtils;
import com.fast.van.utils.CommonData;
import com.fast.van.utils.ErrorCodes;
import com.fast.van.utils.Log;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Amandeep Singh Bagli on 30/09/15.
 */
public class FragmentOrder extends BaseFragment implements GoogleMapFragment.OnGoogleMapFragmentListener, LocationListener {

    private static View view;
    // Google Map
    private GoogleMap googleMap;
    private Marker myLocationMarker;
    private static String ORDERKEY = "order";
    private String customerNumber;

    private OrderDetails orderDetails;
    private Command requestStatus;
    private Command updateStatus;
    private AppCompatButton statusButton;

    public static FragmentOrder newInstance(OrderDetails details) {
        FragmentOrder f = new FragmentOrder();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(ORDERKEY, BaseUtils.getJSONFromOBJ(details));
        f.setArguments(args);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fastvan_fragment_order, container, false);
        } catch (InflateException e) {
        /* map is already there, just return view as it is */
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        statusButton = (AppCompatButton) view.findViewById(R.id.submit);
        statusButton.setOnClickListener(this);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            if (getArguments() != null) {

                orderDetails = BaseUtils.getOBJFromJSON(getArguments().getString(ORDERKEY), OrderDetails.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            ActivityHome activityHome = (ActivityHome) getActivity();
            activityHome.setLocationListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, GoogleMapFragment.newInstance(this), "googlemap");
        fragmentTransaction.commit();

        if (orderDetails != null)
            initForm(orderDetails);

    }


    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        if (googleMap != null) {
            googleMap.setMyLocationEnabled(true);
            Log.d("googlemap", "onMapReady initialized :)");


            if (orderDetails != null && orderDetails.getPickupData() != null) {
                LatLng pickuplocation = new LatLng(orderDetails.getPickupData().getLatitude(), orderDetails.getPickupData().getLongitude());
                googleMap.addMarker(new MarkerOptions()
                        .position(pickuplocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location_pin)));
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(pickuplocation, 13f);
                googleMap.moveCamera(cameraUpdate);
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

    @Override
    public void onLocationChanged(Location location) {
        if (googleMap != null) {
            Log.d("googlemap", "onMapReady initialized :)");
            //googleMap.clear();

            LatLng myLocation = new LatLng(location.getLatitude(), location.getLongitude());
            if (myLocationMarker == null) {
                myLocationMarker = googleMap.addMarker(new MarkerOptions()
                        .position(myLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_van_icon)));

                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(myLocation, 13f);
                googleMap.moveCamera(cameraUpdate);
            } else
                myLocationMarker.setPosition(myLocation);

            //  map.animateCamera(cameraUpdate);

//            googleMap.getUiSettings().setMyLocationButtonEnabled(true);


        } else {
            Log.d("googlemap", "onMapReady error :(");
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
        }
    }


    private void initForm(OrderDetails orderDetails) {
        TextView pickupLocation = (TextView) view.findViewById(R.id.pickuplocation);
        TextView servicetype = (TextView) view.findViewById(R.id.servicetype);
        TextView pickuploaction = (TextView) view.findViewById(R.id.pickuplocation);
        TextView parcelDetails = (TextView) view.findViewById(R.id.parcelDetails);
        TextView customername = (TextView) view.findViewById(R.id.customerName);
        customername.setText(orderDetails.getCustomerName());
        pickupLocation.setText(orderDetails.getPickupLocation());
        servicetype.setText(orderDetails.getServiceType());

        pickuploaction.setText(orderDetails.getPickupLocation());

        parcelDetails.setOnClickListener(this);
        view.findViewById(R.id.viewDetails).setOnClickListener(this);
        view.findViewById(R.id.makeCall).setOnClickListener(this);
        if (orderDetails.getCustomerPhoneNo() != null)
            customerNumber = orderDetails.getCustomerPhoneNo().getPhoneNumber();

        updateStatus(orderDetails.getRequestStatus());
        setStatus();

    }

    private void parcelClicked() {

        try {
            orderDetails.setRequestStatus(requestStatus.name());
        } catch (Exception e) {
            Log.e(TAG,"setRequestStatus",e);
        }


        Intent intent = new Intent(activity, ActivityBookingDetails.class);
        intent.putExtra("orderDetails", BaseUtils.getJSONFromOBJ(orderDetails));
        startActivityForResult(intent, 55);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, requestCode + ":Result:" + resultCode);
        if (resultCode == 55) {
            ((ActivityHome) getActivity()).getTodaysMyOrder();
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

}
