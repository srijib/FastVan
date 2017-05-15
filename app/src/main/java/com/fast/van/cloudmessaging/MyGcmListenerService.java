/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.fast.van.cloudmessaging;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fast.van.R;
import com.fast.van.activities.ActivityNewRequest;
import com.fast.van.activities.ActivityOrderStateNotification;
import com.fast.van.activities.ActivitySplash;
import com.fast.van.activities.BaseActivity;
import com.fast.van.utils.BaseUtils;
import com.fast.van.utils.CommonData;
import com.google.android.gms.gcm.GcmListenerService;

import org.json.JSONException;
import org.json.JSONObject;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {

        String result = null;

        String message = null;
        String orderId = null;
        String notificationType = null;

        Log.e(TAG, "Extra: " + data.toString());
        try {
            result = data.getString("results");
            Log.d(TAG, "Results: " + result);
            Log.e(TAG, "Extra: " + data.toString());

            message = data.getString("message");
        }  catch (Exception e) {
            e.printStackTrace();
        }
        try {
            JSONObject jsonObject=new JSONObject(result);
            message = jsonObject.getString("message");
            orderId = jsonObject.getString("result");
            notificationType = jsonObject.getString("notificationType");
            Log.d(TAG, "JSONObject Message: " + message);
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }catch (Exception e){
            e.printStackTrace();
            return;
        }

        try {
            Log.e(TAG, "Extra: " + data.getString("message"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e(TAG, "FASTVAN APP FOREGROUND: " + BaseActivity.isForeGround());

        Log.d(TAG, "Message: " + message);
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Results: " + result);
        Log.e(TAG, "Extra: " + data.toString());
      /*  if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }
*/

        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        if(notificationType!=null&&notificationType.equals("DRIVER_ASSIGNED"))
        sendNotification(orderId,message,notificationType);
        else{
            sendNotification(message);
        }

        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String orderId,String message,String notificationType) {

/*Intent intent;
if(notificationType!=null&&notificationType.equals("DRIVER_ASSIGNED")){
    intent=new Intent(this,ActivityNewRequest.class);
}else{

}*/



        if(BaseActivity.isForeGround()){
            Intent intent=new Intent(this,ActivityNewRequest.class);
            intent.putExtra("message", message);
            intent.putExtra("orderId", orderId);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        BaseUtils.removeNotification(this);
        CommonData.saveNotificationData(this, orderId, message);
        Intent intent = new Intent(this, ActivitySplash.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("message", message);
        intent.putExtra("orderId", orderId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.notification)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(47 /* ID of notification */, notificationBuilder.build());
    }
    private void sendNotification(String message) {

/*Intent intent;
if(notificationType!=null&&notificationType.equals("DRIVER_ASSIGNED")){
    intent=new Intent(this,ActivityNewRequest.class);
}else{

}*/

        Intent   intent = new Intent(this, ActivityOrderStateNotification.class);
        intent.putExtra("message", message);
        if(BaseActivity.isForeGround()){


            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        BaseUtils.removeCancelNotification(this);




        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.notification)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(128 /* ID of notification */, notificationBuilder.build());
    }

}
