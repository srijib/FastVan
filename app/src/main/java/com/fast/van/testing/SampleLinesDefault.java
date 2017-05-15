package com.fast.van.testing;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.fast.van.R;

import net.fastvan.indicator.LinePageIndicator;


public class SampleLinesDefault extends BaseSampleActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_lines);

        mAdapter = new TestFragmentAdapter(getSupportFragmentManager());

        mPager = (ViewPager)findViewById(R.id.pager);
        mPager.setAdapter(mAdapter);

       // mIndicator = (LinePageIndicator)findViewById(R.id.indicator);
      //  mIndicator.setViewPager(mPager);
    }

    @Override
    public void onClickView(View v) {

    }
}