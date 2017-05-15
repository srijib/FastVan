package com.fast.van.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fast.van.R;
import com.fast.van.callbacks.NavigationLeftDrawerCallbacks;

import java.util.List;

/**
 * Created by Amandeep Singh Bagli on 05/10/15.
 */
public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

  //  private final TypedValue mTypedValue = new TypedValue();
//    private int mBackground;
    private List<String> mValues;
    private NavigationLeftDrawerCallbacks callbacks;
   // private View preView;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String mBoundString;

        public final View mView;


        //public final ImageView mImageView;
        //  public final TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            // mImageView = (ImageView) view.findViewById(R.id.avatar);
            //  mTextView = (TextView) view.findViewById(android.R.id.text1);
        }

        @Override
        public String toString() {
            return super.toString() ;//+ " '" + mTextView.getText();
        }
    }

    public String getValueAt(int position) {
        return mValues.get(position);
    }

    public PaymentAdapter(Context context, List<String> items) {
       // context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
       // mBackground = mTypedValue.resourceId;
        mValues = items;
        //preView=null;
        // pressTextColor= ContextCompat.getColor(context, R.color.appColor);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_row, parent, false);
      //  view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mBoundString = mValues.get(position);

//        if(preView==null){
//            holder.mView.setBackgroundColor(Color.BLACK);
//            preView=holder.mView;
//
//        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
Toast.makeText(context,"Under Constructon",Toast.LENGTH_LONG).show();
//        ((NavigationRightDrawerCallbacks) context).onNavigationRightDrawerItemSelected(position);

                //preView.setBackgroundColor(Color.TRANSPARENT);

           //     holder.mView.setBackgroundColor(Color.BLACK);

             //   preView=holder.mView;


            }
        });


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}