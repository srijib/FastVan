package com.fast.van.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fast.van.R;
import com.fast.van.activities.ActivityBookingDetails;
import com.fast.van.activities.ActivityTodaysBookingOnMap;
import com.fast.van.adapters.MyBookingAdapter;
import com.fast.van.callbacks.MyBookingCallBack;
import com.fast.van.callbacks.MyBookingClickListener;
import com.fast.van.common.SessionManager;
import com.fast.van.config.Constants;
import com.fast.van.loadingindicator.LoadingBox;
import com.fast.van.models.login.Login;
import com.fast.van.models.order.Order;
import com.fast.van.models.order.OrderDetails;
import com.fast.van.retrofit.RestClient;
import com.fast.van.utils.BaseUtils;
import com.fast.van.utils.CommonData;
import com.fast.van.utils.ErrorCodes;
import com.fast.van.utils.Log;
import com.fast.van.utils.file.AppFileUtils;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Amandeep Singh Bagli on 11/01/16.
 */
public class FragmentTodaysBookings extends BaseFragment implements MyBookingClickListener {

    RecyclerView recyclerView;
    TextView noRecord;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fastvan_fragment_payments,container,false);
        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerViewPayment);
        noRecord=(TextView) view.findViewById(R.id.noRecord);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



//        ArrayList<String> list = new ArrayList<>();
//        list.add("My Bookings");
//        list.add("My Payments");
//        list.add("Fare Estimator");
//        list.add("Support");
//        list.add("Logout");

//        MyBookingAdapter adapter=new MyBookingAdapter(getContext(),new ArrayList<OrderDetails>());
//
//        recyclerView.setAdapter(adapter);


        getMyBooking();
    }


    private void getMyBooking(){

        LoadingBox.showLoadingDialog(activity, "Loading...");
        String json = "";
        final Gson gson = new Gson();
        if (!SessionManager.instance.lastUpdatedExpired(activity, Constants.DataRequest.ORDERS)) {
            Log.e(TAG, "data from file");
            json = AppFileUtils.readDataFile(Constants.DataRequest.ORDERS + ".json", activity);
            Log.e("o","data:"+json);
            Order order=gson.fromJson(json,Order.class);
            initlist(order);
            LoadingBox.dismissLoadingDialog();

        }

        if(BaseUtils.isNullOrEmpty(json)) {
            Login user= CommonData.getSessionData(activity);
            Log.e(TAG, "getOrder:LOADING...");
            if(user!=null&&!user.getAccessToken().isEmpty()){
                Log.e(TAG, "getAccessToken:"+user.getAccessToken());
                //QW1hbk1vbiBPY3QgMTIgMjAxNSAwNjozNTo1MSBHTVQrMDAwMCAoVVRDKQ==
                RestClient.getApiServiceForPojo().getTodayBooking(user.getAccessToken(), new Callback<Order>() {
                    @Override
                    public void success(Order order, Response response) {
                        Log.e(TAG, "getDriverInfo:NETWORK:" + order.toString());
                        AppFileUtils.saveDataFile(Constants.DataRequest.ORDERS + ".json", gson.toJson(order), activity);
                        SessionManager.instance.addSpecificLastRequested(activity, Constants.DataRequest.ORDERS, Calendar.getInstance());
                        LoadingBox.dismissLoadingDialog();
                        initlist(order);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        ErrorCodes.checkCode(activity, error);
                        if(adapter!=null&&mValues!=null){
                            mValues.clear();
                            adapter.notifyDataSetChanged();
                        }

                        noRecord.setVisibility(View.VISIBLE);
                        LoadingBox.dismissLoadingDialog();
                    }
                });

            }

        }
    }
    private MyBookingAdapter adapter;
    private List<OrderDetails> mValues;
    private void initlist(Order order){


        mValues=order.getOrderList();

        if(mValues.size()==0)
            noRecord.setVisibility(View.VISIBLE);
        else
            noRecord.setVisibility(View.GONE);

        adapter = new MyBookingAdapter(getContext(), mValues,this,true);

        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onClickView(View v) {

    }

    public void startActivity(){

    }



    @Override
    public void onBookingClick(int position, OrderDetails details,boolean isTrack) {
        if(isTrack) {
            Intent intent = new Intent(activity, ActivityTodaysBookingOnMap.class);
            intent.putExtra("orderDetails", BaseUtils.getJSONFromOBJ(details));
            startActivityForResult(intent, 99);
        }else {
            Intent intent = new Intent(activity, ActivityBookingDetails.class);
            intent.putExtra("orderDetails", BaseUtils.getJSONFromOBJ(details));
            startActivityForResult(intent,55);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG,requestCode+":Result:"+resultCode);

        switch (resultCode){
            case 55:
            case 99:
                getMyBooking();
                break;
        }

    }
}
