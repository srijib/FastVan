package com.fast.van.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fast.van.R;
import com.fast.van.activities.ActivityNewRequest;
import com.fast.van.callbacks.NavigationLeftDrawerCallbacks;
import com.fast.van.callbacks.NavigationRightDrawerCallbacks;
import com.fast.van.models.notifications.Datum;
import com.fast.van.utils.BaseUtils;
import com.fast.van.utils.file.ISO8601DateParser;

import java.util.List;

/**
 * Created by Amandeep Singh Bagli on 01/10/15.
 */
public class RightNavMenuAdapter extends RecyclerView.Adapter<RightNavMenuAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    //private int mBackground;
    private List<Datum> mValues;
    private NavigationLeftDrawerCallbacks callbacks;
    // private View preView;
    int selectview=-1;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String mBoundString;

        public final View mView;
        public   TextView orderId;
        public   TextView orderDetail;
        public   TextView scheduledtime;




        //public final ImageView mImageView;
        //  public final TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            orderId = (TextView) view.findViewById(R.id.orderID);
            orderDetail = (TextView) view.findViewById(R.id.orderDetail);
            scheduledtime = (TextView) view.findViewById(R.id.scheduledtime);
            //  mTextView = (TextView) view.findViewById(android.R.id.text1);
        }

        @Override
        public String toString() {
            return super.toString() ;//+ " '" + mTextView.getText();
        }
    }

   /* public String getValueAt(int position) {
        return mValues.get(position);
    }*/

    public RightNavMenuAdapter(Context context, List<Datum> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        //mBackground = mTypedValue.resourceId;
        mValues = items;
        //  preView=null;
        // pressTextColor= ContextCompat.getColor(context, R.color.appColor);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_row, parent, false);
        //  view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        holder.mBoundString = mValues.get(position);

        /*if(preView==null){
            holder.mView.setBackgroundColor(Color.BLACK);
            preView=holder.mView;

        }*/

        holder.mView.setBackgroundColor(selectview==position?Color.BLACK:Color.TRANSPARENT);

holder.orderId.setText("Order "+mValues.get(position).getOrderId());
holder.orderDetail.setText(mValues.get(position).getBody());
        try {
            holder.scheduledtime.setText(BaseUtils.dateToStringTimeOnly(ISO8601DateParser.parse(mValues.get(position).getCreatedAt())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                selectview=position;
                ((NavigationRightDrawerCallbacks)context).onNavigationRightDrawerItemSelected(position, mValues.get(position));

                //   preView.setBackgroundColor(Color.TRANSPARENT);

                holder.mView.setBackgroundColor(Color.BLACK);
                notifyDataSetChanged();



                //         preView=holder.mView;
//

            }
        });


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}