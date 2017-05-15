package com.fast.van.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.fast.van.fragments.FragmentDelivery;
import com.fast.van.models.order.ParcelDetail;

import java.util.List;

/**
 * Created by Amandeep Singh Bagli on 28/09/15.
 */
public class DeliveryViewPagerAdapter extends FragmentStatePagerAdapter {
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    List<ParcelDetail> list;
    private String orderId;

    public DeliveryViewPagerAdapter(List<ParcelDetail> list,String orderId,FragmentManager fm) {
        super(fm);

        this.list=list;
        this.orderId=orderId;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int position) {
        return  FragmentDelivery.newInstance(list.get(position),orderId);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}