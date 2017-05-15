package com.fast.van.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fast.van.R;
import com.fast.van.models.order.Inventory;
import com.fast.van.utils.BaseUtils;

import java.io.File;

/**
 * Created by Amandeep Singh Bagli on 23/12/15.
 */
public class FragmentInventoryPager extends BaseFragment {
    private ImageView imageView;
    private RelativeLayout parentlayout;


    private static final String DETAILKEY="inventory";
    private Inventory inventory;
    private View layoutView;

    public static FragmentInventoryPager newInstance(Inventory inventory) {
        FragmentInventoryPager f = new FragmentInventoryPager();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString(DETAILKEY, BaseUtils.getJSONFromOBJ(inventory));
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            inventory = BaseUtils.getOBJFromJSON(getArguments().getString(DETAILKEY), Inventory.class) ;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutView=inflater.inflate(R.layout.delivery_location_fragment_row,null);
        imageView=(ImageView)layoutView.findViewById(R.id.networkImage);
        parentlayout= (RelativeLayout) layoutView.findViewById(R.id.parentLayoutD);


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
        if(inventory!=null) {

            TextView inventoryType = (TextView) layoutView.findViewById(R.id.textParcel);
            inventoryType.setText("Inventory");
            TextView typeOfGood = (TextView) layoutView.findViewById(R.id.typeOfGood);
            TextView quantity = (TextView) layoutView.findViewById(R.id.quantity);
            TextView description = (TextView) layoutView.findViewById(R.id.parcelDescription);
//    ImageView imageView = (ImageView) layoutView.findViewById(R.id.parcelDescription);
            layoutView.findViewById(R.id.viewDetails).setOnClickListener(this);
            // deliveryloaction.setText(parcelDetail.getFullAddress());
            typeOfGood.setText(inventory.getInventoryName());
            quantity.setText("" + inventory.getQuantity());
            description.setText(inventory.getName());
            imageView.setImageBitmap(null);


            if(inventory.getImagePath()!=null&&inventory.getImagePath().startsWith("http")){
                Glide.with(this).load(inventory.getImagePath())
                        .fitCenter()
                        .placeholder(R.drawable.parcelplaceholder)
                        .into(imageView);
            }else {


                Uri myUri = null;
                try {


                    // myUri = Uri.parse(parcelDetail.getImagePath());
                    myUri = Uri.fromFile(new File(inventory.getImagePath()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Glide.with(this).load(myUri)
                        .fitCenter()
                        .placeholder(R.drawable.parcelplaceholder)
                        .into(imageView);
            }
        }

    }

    @Override
    public void onClickView(View v) {

    }

}
