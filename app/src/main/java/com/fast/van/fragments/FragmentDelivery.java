package com.fast.van.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fast.van.R;
import com.fast.van.activities.BaseActivity;
import com.fast.van.dialogs.CommonDialog;
import com.fast.van.loadingindicator.LoadingBox;
import com.fast.van.models.Command;
import com.fast.van.models.ServiceReply;
import com.fast.van.models.login.Login;
import com.fast.van.models.order.ParcelDetail;
import com.fast.van.retrofit.RestClient;
import com.fast.van.utils.BaseUtils;
import com.fast.van.utils.CommonData;
import com.fast.van.utils.ErrorCodes;
import com.fast.van.utils.Log;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Amandeep Singh Bagli on 28/09/15.
 */
public class FragmentDelivery extends BaseFragment{
    ImageView imageView;
    RelativeLayout parentlayout;
Button statusButton;

    private static final String DETAILKEY="details";
    private static final String ORDERIDKEY="orderId";
    private ParcelDetail parcelDetail;
    private View layoutView;
private  Command requestStatus;
private  Command updateStatus;
    private String orderid;

    public static FragmentDelivery newInstance(ParcelDetail details,String orderId) {
        FragmentDelivery f = new FragmentDelivery();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(DETAILKEY, BaseUtils.getJSONFromOBJ(details));
        args.putString(ORDERIDKEY, orderId);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            if(getArguments()!=null) {
                orderid = getArguments().getString(ORDERIDKEY);
                parcelDetail = BaseUtils.getOBJFromJSON(getArguments().getString(DETAILKEY), ParcelDetail.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void updateStatus(String status){



        if(status.equals("DRIVER_ASSIGNED")){
    requestStatus=Command.DRIVER_ASSIGNED;
        return;
        }
        if(status.equals("DRIVER_ACCEPTED")){
            requestStatus=Command.ACCEPT;

        return;
        }
        if(status.equals("REACHED_PICKUP_POINT")){
            requestStatus=Command.REACHED_PICKUP_POINT;
        return;
        }
        if(status.equals("PICKED_UP")){
            requestStatus=Command.PICKED_UP;
        return;
        }
        if(status.equals("REACHED_DELIVERY_POINT")){
            requestStatus=Command.REACHED_DELIVERY_POINT;
        return;
        }
     if(status.equals("ORDER_DELIVERED")){
            requestStatus=Command.ORDER_DELIVERED;
        return;
        }



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView=inflater.inflate(R.layout.delivery_location_fragment_row,null);
        imageView=(ImageView)layoutView.findViewById(R.id.networkImage);
        parentlayout= (RelativeLayout) layoutView.findViewById(R.id.parentLayoutD);
//        statusButton=(Button) layoutView.findViewById(R.id.status);
//        statusButton.setOnClickListener(this);

/*if(requestStatus==null||requestStatus.equals(Command.DRIVER_ASSIGNED)||requestStatus.equals(Command.ORDER_DELIVERED)){
    statusButton.setVisibility(View.GONE);
}else{
    setStatus();
    statusButton.setVisibility(View.VISIBLE);
}*/
        return layoutView;

    }
    //int margin=10;
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //  margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10 * 2, getResources().getDisplayMetrics());

        initForm();
    }
private void initForm(){
if(parcelDetail!=null) {
  //  TextView deliveryloaction = (TextView) layoutView.findViewById(R.id.deliveryAddress);
    //TextView viewDetails = (TextView) layoutView.findViewById(R.id.viewDetails);
    TextView typeOfGood = (TextView) layoutView.findViewById(R.id.typeOfGood);
    TextView quantity = (TextView) layoutView.findViewById(R.id.quantity);
    TextView description = (TextView) layoutView.findViewById(R.id.parcelDescription);
   // layoutView.findViewById(R.id.viewDetails).setOnClickListener(this);
    //deliveryloaction.setText(parcelDetail.getFullAddress());
    typeOfGood.setText(parcelDetail.getTypeOfGood());
    quantity.setText("" + parcelDetail.getQuantity());
   description.setText(parcelDetail.getDescriptions());
    imageView.setImageBitmap(null);
        Glide.with(this).load(parcelDetail.getImagePath())
                .fitCenter()
                .placeholder(R.drawable.parcelplaceholder)
                .into(imageView);



}

}
    public void scaleImage(float scale,float positionOffset){
       // imageView.setScaleX(scale);
        Log.i("", "positionOffset: " + positionOffset*100);
//setLayoutPadding(scale);
     //   imageView.setScaleY(scale);

    //    textView1.setText(""+positionOffset);
    //    textView2.setText(""+positionOffset);
    }

    @Override
    public void onClickView(View v) {

        int id=v.getId();
        switch (id){
            case R.id.viewDetails:
          /*      Intent intent=new Intent(activity, ActivityViewDetailsMap.class);
                intent.putExtra(ActivityViewDetailsMap.TITLEKEY,getString(R.string.dropofflocation));
                intent.putExtra(ActivityViewDetailsMap.ISPICKUP,false);
                if(dropLocationDetail!=null){
                    intent.putExtra(ActivityViewDetailsMap.SCREENDATA,""+BaseUtils.getJSONFromOBJ(dropLocationDetail.getDropLocationDetail()));
                }


                startActivity(intent);
                Transformer.rightToLeft(activity);*/
                break;
            case R.id.status:
                updateStatus();
                break;
        }


    }

    private void setStatus(){


        switch (requestStatus){
            case DRIVER_ASSIGNED:
                break;
            case ACCEPT:
                statusButton.setText(getString(R.string.arrived));
                updateStatus=Command.REACHED_PICKUP_POINT;
                break;
            case REACHED_PICKUP_POINT:
                statusButton.setText(getString(R.string.pickup));
                updateStatus=Command.PICKED_UP;
                break;
            case PICKED_UP:
                statusButton.setText(getString(R.string.delivering));
                updateStatus=Command.REACHED_DELIVERY_POINT;
                break;
            case REACHED_DELIVERY_POINT:
                statusButton.setText(getString(R.string.delivered));
                updateStatus=Command.ORDER_DELIVERED;
                break;
            case ORDER_DELIVERED:
                statusButton.setVisibility(View.GONE);
                break;
            default:
                statusButton.setVisibility(View.GONE);
        }

    }

    private void updateStatus(){
        Log.e(TAG, "FASTVAN APP FOREGROUND: " + BaseActivity.isForeGround());
        Login user = CommonData.getSessionData(activity);

        if(user!=null) {
            LoadingBox.showLoadingDialog(activity, "Loading...");
            RestClient.getApiServiceForPojo().updateOrderStatus(user.getAccessToken(), orderid, updateStatus, new Callback<ServiceReply>() {
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



}
