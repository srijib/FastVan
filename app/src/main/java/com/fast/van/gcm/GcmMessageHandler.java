package com.fast.van.gcm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.fast.van.R;
import com.fast.van.activities.ActivitySplash;
import com.fast.van.sharedpreferences.Prefs;
import com.fast.van.sharedpreferences.SharedPreferencesName;
import com.google.android.gms.gcm.GoogleCloudMessaging;



public class GcmMessageHandler extends IntentService {
    public static final int MESSAGE_NOTIFICATION_ID = 435345;
    private NotificationManager mNotificationManager;

    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    public static void ClearNotification(Context c) {
        Log.v("ClearNotification", "ClearNotification");

        NotificationManager notifManager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.cancelAll();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Retrieve data extras from push notification
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);
        // Keys in the data are shown as extras
        try {
            String msg = extras.getString("message");
            String bookingId = extras.getString("bookingID");
            int flag = Integer.parseInt(extras.getString("flag"));

            sendNotification(msg);
//            PushInfo pushData = new PushInfo(bookingId, flag, msg);
//            Prefs.with(getApplicationContext()).save(SharedPreferencesName.PUSHDATA, pushData);
//            EventBus.getDefault().post(pushData);
//            // Create notification or otherwise manage incoming push

        } catch (Exception e) {
            sendNotification("");
        }
        // Log receiving message
       // Bundle[{message=Driver is on the way., android.support.content.wakelockid=1, collapse_key=demo, from=799492082381, bookingID=367672}]
        //    Bundle[{message=Checklist has been sent to you. Please verify., android.support.content.wakelockid=1, flag=1, collapse_key=demo, from=799492082381, bookingID=949646}]

        Log.i("GCM", "Received : (" + messageType + ")  " + extras.toString());
        // Notify receiver the intent is completed
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        final Intent notificationIntent = new Intent(this, ActivitySplash.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher).setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))
                        .setContentTitle(getResources().getString(R.string.app_name))
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg)).setAutoCancel(true)
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(MESSAGE_NOTIFICATION_ID, mBuilder.build());
    }


}