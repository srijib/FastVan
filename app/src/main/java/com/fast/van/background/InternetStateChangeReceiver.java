package com.fast.van.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fast.van.utils.InternetDetector;
import com.fast.van.utils.Log;

/**
 * Created by Amandeep Singh Bagli on 28/10/15.
 */
public class InternetStateChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Boolean isConnected=InternetDetector.isInternetAvailable(context);
        Log.e("InternetStateChangeReceiver","InternetStateChangeReceiver:"+isConnected);
        if(isConnected){
            Intent tracking = new Intent(context, UpdateDriverLocation.class);
            context.startService(tracking);
        }else
        {
            Intent tracking = new Intent(context, UpdateDriverLocation.class);
            context.stopService(tracking);
        }

    }
}
