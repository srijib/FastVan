package com.fast.van.transformer;

import android.app.Activity;

import com.fast.van.R;

/**
 * Created by Amandeep Singh Bagli on 20/10/15.
 */
public class Transformer {

    public static void rightToLeft(Activity activity){
        activity.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
    }
    public static void leftToRight(Activity activity){
        activity.overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);
    }
}
