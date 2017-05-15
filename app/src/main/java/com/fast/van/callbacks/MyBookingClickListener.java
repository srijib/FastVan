package com.fast.van.callbacks;

import com.fast.van.models.order.OrderDetails;

/**
 * Created by Amandeep Singh Bagli on 11/01/16.
 */
public interface MyBookingClickListener {
    void onBookingClick(int position,OrderDetails details,boolean isTrack);
}
