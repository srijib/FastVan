package com.fast.van.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fast.van.R;
import com.fast.van.callbacks.NavigationLeftDrawerCallbacks;

import java.util.List;

/**
 * Created by Amandeep Singh Bagli on 28/09/15.
 */
public class LeftNavMenuAdapter  extends RecyclerView.Adapter<LeftNavMenuAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<String> mValues;
    private NavigationLeftDrawerCallbacks callbacks;
    private View preView;
private int pressTextColor;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String mBoundString;

        public final View mView;


        //public final ImageView mImageView;
        public final TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
           // mImageView = (ImageView) view.findViewById(R.id.avatar);
            mTextView = (TextView) view.findViewById(android.R.id.text1);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText();
        }
    }

    public String getValueAt(int position) {
        return mValues.get(position);
    }

    public LeftNavMenuAdapter(Context context, List<String> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;
        preView=null;
        pressTextColor= ContextCompat.getColor(context,R.color.appColor);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.left_menu_row, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mBoundString = mValues.get(position);
        holder.mTextView.setText(mValues.get(position));
        if(preView==null){
            holder.mView.setBackgroundColor(Color.BLACK);
            preView=holder.mView;
            holder.mTextView.setTextColor(pressTextColor);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
//                Toast.makeText(context,"under construction",Toast.LENGTH_LONG).show();
//                if(callbacks!=null)
                    ((NavigationLeftDrawerCallbacks)context).onNavigationLeftDrawerItemSelected(position);
//                Intent intent = new Intent(context, CheeseDetailActivity.class);
//                intent.putExtra(CheeseDetailActivity.EXTRA_NAME, holder.mBoundString);
//
//                context.startActivity(intent);


                if(position==3)
                    return;
                preView.setBackgroundColor(Color.TRANSPARENT);
                ((TextView) preView.findViewById(android.R.id.text1)).setTextColor(Color.WHITE);
                holder.mView.setBackgroundColor(Color.BLACK);
                holder.mTextView.setTextColor(pressTextColor);
                preView=holder.mView;


            }
        });


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}