package com.fast.van.utils;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.fast.van.utils.file.ISO8601DateParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Amandeep Singh Bagli on 08/10/15.
 */
public class BaseUtils {
    private static Gson gson = new Gson();

    public static boolean isNullOrEmpty(String theString) {
        return (theString == null || theString.isEmpty());
    }

    public static <T> T getOBJFromJSON(String json, Class<T> objClass) {

        T obj = null;
        try {
            Log.e("JSON", "FROM:" + json);
            obj = gson.fromJson(json, objClass);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return obj;

    }

    public static <T> String getJSONFromOBJ(T obj) {
        String json = null;
        try {

            json = gson.toJson(obj);
            Log.e("JSON", "TO:" + json);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return json;

    }

    public static String stringToDate(String dateString) {

//        String scheduleTime=null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a, EEE MMMM dd", Locale.US);
            Date date = ISO8601DateParser.parse(dateString);
//            scheduleTime=sdf.format(date);
            return sdf.format(date);
        } catch (ParseException e) {
            Log.e("BaseUtil", "getScheduledTime:ParseException", e);
        } catch (Exception e) {
            Log.e("BaseUtil", "getScheduledTime:NULL", e);
        }
        return null;
    }

    public static String dateToStringTimeOnly(Date date) {

//        String scheduleTime=null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
            // Date date= ISO8601DateParser.parse(dateString);
//            scheduleTime=sdf.format(date);
            return sdf.format(date);

        } catch (Exception e) {
            Log.e("BaseUtil", "getScheduledDATETime:NULL", e);
        }
        return null;
    }


    public static void removeNotification(Context context) {
        NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(47);
    }

    public static void removeCancelNotification(Context context) {
        NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(128);
    }

    public static void makeCall(String number, Activity activity) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:+27" + number));
            activity.startActivity(callIntent);
        } catch (Exception e) {
            Log.e("makeCall", "Exception:", e);
        }


    }
}
