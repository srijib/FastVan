package com.fast.van.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.fast.van.fragments.FragmentInventoryPager;
import com.fast.van.models.order.Inventory;
import com.fast.van.models.order.InventoryName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amandeep Singh Bagli on 23/12/15.
 */
public class InventoryViewPagerAdapter extends FragmentStatePagerAdapter {
    SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    List<Inventory> list;

    public InventoryViewPagerAdapter(List<InventoryName> dataList, FragmentManager fm) {
        super(fm);
        this.list=new ArrayList<Inventory>();
        for(InventoryName inventoryName:dataList){
            for(Inventory inventory:inventoryName.getData()){

                inventory.setInventoryName(inventoryName.getName());

                this.list.add(inventory);
            }
        }



    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Fragment getItem(int position) {
        return  FragmentInventoryPager.newInstance(list.get(position));
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