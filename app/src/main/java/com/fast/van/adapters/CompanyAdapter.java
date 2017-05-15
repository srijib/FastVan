package com.fast.van.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.fast.van.R;

import java.util.List;

/**
 * Created by Amandeep Singh Bagli on 29/09/15.
 */
public class CompanyAdapter  extends RecyclerView.Adapter<CompanyAdapter.ViewHolder> {

    private final TypedValue mTypedValue = new TypedValue();
    private int mBackground;
    private List<String> mValues;
    private int previousSelection=0;
    private  String companyName;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public String mBoundString;

        public final View mView;


        //public final ImageView mImageView;
        public RadioButton mTextView;



        public ViewHolder(View view) {
            super(view);
            mView = view;
            // mImageView = (ImageView) view.findViewById(R.id.avatar);
            mTextView = (RadioButton) view.findViewById(R.id.radioButton);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTextView.getText();
        }
    }

    public String getValueAt(int position) {
        return mValues.get(position);
    }

    public CompanyAdapter(Context context, List<String> items) {
        context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, true);
        mBackground = mTypedValue.resourceId;
        mValues = items;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.company_name_row, parent, false);
        view.setBackgroundResource(mBackground);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mBoundString = mValues.get(position);
        holder.mTextView.setText(mValues.get(position));


        if(previousSelection==position){
            holder.mTextView.setChecked(true);
            companyName=holder.mTextView.getText().toString();
        }else{
            holder.mTextView.setChecked(false);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Context context = v.getContext();
                //Toast.makeText(context, "under construction", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(context, CheeseDetailActivity.class);
//                intent.putExtra(CheeseDetailActivity.EXTRA_NAME, holder.mBoundString);
//
//                context.startActivity(intent);

//                if(previousSelection!=null){
//                    previousSelection.setChecked(false);
//                    previousSelection=holder.mTextView;
//                    previousSelection.setChecked(true);
//                }else{
//                    previousSelection=holder.mTextView;
//                    previousSelection.setChecked(true);
//                }
                previousSelection=position;
                companyName=holder.mTextView.getText().toString();
                notifyDataSetChanged();
            }
        });


    }

   public String getSelection(){

return companyName;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
}