package com.fast.van.testing;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.View;


import com.fast.van.R;
import com.fast.van.adapters.DeliveryViewPagerAdapter;
import com.fast.van.fragments.FragmentDelivery;
import com.fast.van.models.order.ParcelDetail;
import com.fast.van.transformer.DepthPageTransformer;
import com.fast.van.utils.Log;

import net.fastvan.indicator.SimpleCirclePageIndicator;

import java.util.ArrayList;


public class SampleCirclesDefault extends BaseSampleActivity {
    float RATIO_SCALE=0.2f;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_circles);

//        mAdapter = new TestFragmentAdapter(getSupportFragmentManager());
//
      /*  ImageView mImageView = (ImageView) findViewById(R.id.imgAvatar);
        Glide.with(this)
                .load(R.drawable.avatar)
                .fitCenter()
                .into(mImageView);*/
        mPager = (ViewPager)findViewById(R.id.pager);
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10 * 2, getResources().getDisplayMetrics());
//       // mViewPager.setPageMargin(-margin);
//
//        mPager.setPageMargin(-margin);
//       // mPager.setHorizontalFadingEdgeEnabled(true);
//       // mPager.setVerticalFadingEdgeEnabled(true);
//        //mPager.setFadingEdgeLength(30);
//        mPager.setClipToPadding(false);
//
//        mPager.setPadding(48, 8, 48, 8);
//        mPager.setOffscreenPageLimit(3);
//
//        mPager.setAdapter(mAdapter);
//

DeliveryViewPagerAdapter adapter=new DeliveryViewPagerAdapter(new ArrayList<ParcelDetail>(),"164",getSupportFragmentManager());
        mPager.setAdapter(adapter);
        mPager.setClipToPadding(false);
        //mPager.setPageMargin(-margin);
        mPager.setPadding(margin, 0, margin, 0);
       // mPager.setPaddingRelative(margin,0,margin,0);
        mPager.setOffscreenPageLimit(3);
        mIndicator = (SimpleCirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);
       mPager.setPageTransformer(true,new DepthPageTransformer());
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i("", "positionOffset: " + positionOffset);
                Log.i("", "positionOffsetPixels: " + positionOffsetPixels);


                FragmentDelivery sampleFragment = (FragmentDelivery) ((DeliveryViewPagerAdapter) mPager.getAdapter()).getRegisteredFragment(position);


                float scale = 1 - (positionOffset * RATIO_SCALE);

                // Just a shortcut to findViewById(R.id.image).setScale(scale);
                sampleFragment.scaleImage(scale,positionOffset);


                if (position + 1 < mPager.getAdapter().getCount()) {
                    sampleFragment = (FragmentDelivery) ((DeliveryViewPagerAdapter) mPager.getAdapter()).getRegisteredFragment(position + 1);
                    scale = positionOffset * RATIO_SCALE + (1 - RATIO_SCALE);
                    sampleFragment.scaleImage(scale,positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
                Log.i("", "onPageSelected: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("", "onPageScrollStateChanged: " + state);
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    FragmentDelivery fragment = (FragmentDelivery) ((DeliveryViewPagerAdapter) mPager.getAdapter()).getRegisteredFragment(mPager.getCurrentItem());
                    fragment.scaleImage(1,0.0f);

                    if (mPager.getCurrentItem() > 0) {
                        fragment = (FragmentDelivery) ((DeliveryViewPagerAdapter) mPager.getAdapter()).getRegisteredFragment(mPager.getCurrentItem() - 1);
                        fragment.scaleImage(1 - RATIO_SCALE,0.0f);
                    }

                    if (mPager.getCurrentItem() + 1 < mPager.getAdapter().getCount()) {
                        fragment = (FragmentDelivery) ((DeliveryViewPagerAdapter) mPager.getAdapter()).getRegisteredFragment(mPager.getCurrentItem() + 1);
                        fragment.scaleImage(1 - RATIO_SCALE,0.0f);
                    }
                }

            }
        });






    }

    @Override
    public void onClickView(View v) {

    }
}