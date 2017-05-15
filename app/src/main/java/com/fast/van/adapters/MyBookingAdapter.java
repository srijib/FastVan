package com.fast.van.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.fast.van.R;
import com.fast.van.activities.ActivityBookingDetails;
import com.fast.van.activities.ActivityHome;
import com.fast.van.callbacks.MyBookingCallBack;
import com.fast.van.callbacks.MyBookingClickListener;
import com.fast.van.callbacks.NavigationLeftDrawerCallbacks;
import com.fast.van.fragments.FragmentMyBooking;
import com.fast.van.models.Command;
import com.fast.van.models.order.OrderDetails;
import com.fast.van.models.signup.ServiceType;
import com.fast.van.models.signup.VehicalType;
import com.fast.van.utils.BaseUtils;

import java.util.List;

/**
 * Created by Amandeep Singh Bagli on 05/10/15.
 */
public class MyBookingAdapter extends RecyclerView.Adapter<MyBookingAdapter.ViewHolder> {


    private List<OrderDetails> mValues;
    private MyBookingClickListener callbacks;
    private boolean isTracking;

    // private View preView;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        //public String mBoundString;

        public final View mView;


        //public final ImageView mImageView;
        public   TextView textViewOrderId;
        public   TextView textViewServiceType;
        public   TextView textViewScheduledTime;
        public ProgressBar progressBar;
        public TextView vehicleName;
        public ImageView vehicleicon;
        public TextView orderStatus;
        public View rlbookingProgressLayout;
        public View viewDetails;
        public View trackDriver;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            // mImageView = (ImageView) view.findViewById(R.id.avatar);
            textViewOrderId = (TextView) view.findViewById(R.id.orderID);
            textViewServiceType = (TextView) view.findViewById(R.id.servicetype);
            textViewScheduledTime = (TextView) view.findViewById(R.id.scheduledtime);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
            vehicleName= (TextView) view.findViewById(R.id.textVehicle);
            orderStatus= (TextView) view.findViewById(R.id.orderStatus);
            vehicleicon= (ImageView) view.findViewById(R.id.iconVehicle);
            rlbookingProgressLayout= view.findViewById(R.id.rlBookingProgress);
            viewDetails= view.findViewById(R.id.tvViewDetails);
            trackDriver= view.findViewById(R.id.tvTrackDriver);
        }


    }



    public MyBookingAdapter(Context context, List<OrderDetails> items,MyBookingClickListener callbacks,boolean isTracking) {
        // context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        // mBackground = mTypedValue.resourceId;
        mValues = items;
        this.callbacks=callbacks;
        this.isTracking=isTracking;
        //preView=null;
        // pressTextColor= ContextCompat.getColor(context, R.color.appColor);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mybooking_row, parent, false);
        //  view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final OrderDetails details=mValues.get(position);

        holder.textViewOrderId.setText("" + details.getOrderId());

        holder.textViewServiceType.setText(details.getServiceType());
        holder.textViewScheduledTime.setText(BaseUtils.stringToDate(details.getScheduledTime()));
holder.progressBar.setProgress(updateProgress(details.getRequestStatus()));

        if(details.getServiceType().equals(ServiceType.COURIER.name()))
            vehicleType(details.getServiceScope(),holder.vehicleicon,holder.vehicleName);
        else
            vehicleType(details.getVehicleType(),holder.vehicleicon,holder.vehicleName);
        holder.orderStatus.setText(details.getRequestStatus());
        holder.orderStatus.setVisibility(isOrderCanncelledOrRefunded(details.getRequestStatus()) ? View.VISIBLE : View.GONE);
        holder.rlbookingProgressLayout.setVisibility(isOrderCanncelledOrRefunded(details.getRequestStatus()) ? View.GONE : View.VISIBLE);

        holder.trackDriver.setVisibility(isTracking?View.VISIBLE:View.GONE);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Context context = v.getContext();
//                Toast.makeText(context, "Under Constructon", Toast.LENGTH_LONG).show();
////

                callbacks.onBookingClick(position, details, false);


            }
        });
        holder.viewDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Context context = v.getContext();
//                Toast.makeText(context, "Under Constructon", Toast.LENGTH_LONG).show();
////

                callbacks.onBookingClick(position,details,false);


            }
        });
        holder.trackDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Context context = v.getContext();
//                Toast.makeText(context, "Under Constructon", Toast.LENGTH_LONG).show();
////

                callbacks.onBookingClick(position,details,true);


            }
        });


    }
    
   private boolean  isOrderCanncelledOrRefunded(String status){
       
       if(status!=null&&(status.equals(Command.CANCELLED.name())||status.equals(Command.REFUND.name()))){
           return true;
       }
       
       
       return false;
       
   }

    private String serviceTypeDescription(){

        return null;

    }

    @Override
    public int getItemCount() {
         return mValues.size();
       // return 5;
    }



    private int updateProgress(String status){



        if(status.equals("DRIVER_ASSIGNED")){

            return 5;
        }
        if(status.equals("DRIVER_ACCEPTED")){


            return 10;
        }
        if(status.equals("REACHED_PICKUP_POINT")){

            return 15;
        }
        if(status.equals("PICKED_UP")){

            return 33;
        }
        if(status.equals("REACHED_DELIVERY_POINT")){

            return 66;
        }
        if(status.equals("ORDER_DELIVERED")){

            return 100;
        }

return 0;
    }

    private void vehicleType(VehicalType vehicalType,ImageView vehicleicon,TextView vehicleName){
        vehicleicon.setVisibility(View.VISIBLE);
        vehicleName.setVisibility(View.VISIBLE);

        if(vehicalType!=null)
            switch (vehicalType){
                case ONE_TON_TRUCK:
                    vehicleicon.setImageResource(R.drawable.icon_3_ton_truck);
                    vehicleName.setText(vehicleName.getContext().getString(R.string.oneton));
                    return;
                case TWO_TON_TRUCK:
                    vehicleicon.setImageResource(R.drawable.icon_5_ton_truck);
                    vehicleName.setText(vehicleName.getContext().getString(R.string.twoton));
                    return;
                case FOUR_TON_TRUCK:
                    vehicleicon.setImageResource(R.drawable.icon_5_ton_truck);
                    vehicleName.setText(vehicleName.getContext().getString(R.string.fourton));
                    return;
                case BIKE:
                    vehicleicon.setImageResource(R.drawable.icon_bike);
                    vehicleName.setText(vehicleName.getContext().getString(R.string.bike));
                    return;
                case VAN:
                    vehicleicon.setImageResource(R.drawable.icon_van);
                    vehicleName.setText(vehicleName.getContext().getString(R.string.van));
                    return;
                case NATIONAL:
                    vehicleicon.setImageResource(R.drawable.icon_national);
                    vehicleName.setText(vehicleName.getContext().getString(R.string.national));
                    return;
                case INTERNATIONAL:
                    vehicleicon.setImageResource(R.drawable.icon_bikeinternational);
                    vehicleName.setText(vehicleName.getContext().getString(R.string.international));
                    return;

            }
        vehicleicon.setVisibility(View.INVISIBLE);
        vehicleName.setVisibility(View.INVISIBLE);

    }
}