package com.fast.van.cloudmessaging;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.fast.van.R;
import com.fast.van.callbacks.GCMTokenCallback;
import com.fast.van.utils.CommonData;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by Amandeep Singh Bagli on 09/10/15.
 */
public class RegistrationAsyncTask extends AsyncTask<Void,Void,String>{
    private GCMTokenCallback callback;
    private Context context;
    private static final String TAG = "RegistrationAsyncTask";
    private static final String[] TOPICS = {"global"};
    public RegistrationAsyncTask(){

    }
    public RegistrationAsyncTask(Context context){

        this.context=context;
        try {
            this.callback=(GCMTokenCallback)context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String token=null;
        try {
            // [START register_for_gcm]
            // Initially this call goes out to the network to retrieve the token, subsequent calls
            // are local.
            // [START get_token]
            InstanceID instanceID = InstanceID.getInstance(context);
              token = instanceID.getToken(context.getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            // [END get_token]810573835767
            Log.i(TAG, "GCM Registration Token: " + token);

            // TODO: Implement this method to send any registration to your app's servers.
            //sendRegistrationToServer(token);

            // Subscribe to topic channels
            subscribeTopics(token);

            // You should store a boolean that indicates whether the generated token has been
            // sent to your server. If the boolean is false, send the token to your server,
            // otherwise your server should have already received the token.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
            sharedPreferences.edit().putString(QuickstartPreferences.REGISTRATION_TOKEN, token).apply();
            CommonData.saveDeviceToken(context, token);
            // ((GCMTokenCallback) this).onGCMSuccess(token);
            // [END register_for_gcm]
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
         /*   try {
                ((GCMTokenCallback) this).onGCMFailure();
            } catch (Exception e1) {
                e1.printStackTrace();
                Log.d(TAG, "GCMTokenCallback Failed", e);
            }*/

        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(context).sendBroadcast(registrationComplete);
        return null;
    }

    @Override
    protected void onPostExecute(String id) {

        if(id!=null){
            if(callback!=null)
            callback.onGCMSuccess(id);

        }else{
            if(callback!=null)
                callback.onGCMFailure();

        }

    }



    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(context);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]
}
