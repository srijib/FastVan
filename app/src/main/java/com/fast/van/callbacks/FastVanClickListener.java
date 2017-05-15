package com.fast.van.callbacks;

import android.view.View;

/**
 * Created by Amandeep Singh Bagli on 16/10/15.
 */
public interface FastVanClickListener {

    //Onclick listener to avoid multi clicks
    void onClickView(View v);
}
