package com.fast.van.testing;

import android.support.v4.view.ViewPager;


import com.fast.van.activities.BaseActivity;

import net.fastvan.indicator.PageIndicator;

import java.util.Random;

public abstract class BaseSampleActivity extends BaseActivity {
    private static final Random RANDOM = new Random();

    TestFragmentAdapter mAdapter;
    ViewPager mPager;
    PageIndicator mIndicator;



}
