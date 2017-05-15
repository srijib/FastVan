package com.fast.van.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fast.van.R;

/**
 * Created by Amandeep Singh Bagli on 06/10/15.
 */
public class FragmentEstimate extends BaseFragment{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fastvan_fragment_fare_calculator,container,false);

        return view;
    }

    @Override
    public void onClickView(View v) {

    }
}
