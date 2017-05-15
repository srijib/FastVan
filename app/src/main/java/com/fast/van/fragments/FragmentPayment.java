package com.fast.van.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fast.van.R;
import com.fast.van.adapters.PaymentAdapter;

import java.util.ArrayList;

/**
 * Created by Amandeep Singh Bagli on 05/10/15.
 */
public class FragmentPayment extends BaseFragment {

    RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fastvan_fragment_payments,container,false);
        recyclerView=(RecyclerView) view.findViewById(R.id.recyclerViewPayment);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);




        ArrayList<String> list = new ArrayList<>();
        list.add("My Bookings");
        list.add("My Payments");
        list.add("Fare Estimator");
        list.add("Support");
        list.add("Logout");

        PaymentAdapter adapter=new PaymentAdapter(getContext(),list);

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onClickView(View v) {

    }
}
