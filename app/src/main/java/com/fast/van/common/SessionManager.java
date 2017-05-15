package com.fast.van.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.fast.van.config.Constants;
import com.fast.van.sharedpreferences.SharedPreferencesName;
import com.fast.van.utils.BaseUtils;
import com.fast.van.utils.file.AppFileUtils;
import com.fast.van.utils.file.ISO8601DateParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Amandeep Singh Bagli on 08/10/15.
 */
public class SessionManager {

    // Instance of the class
    public static SessionManager instance = new SessionManager();


    private Boolean sessionPopulated;

    private Boolean hasNetwork;

    private String[] categories;

    private ArrayList<String> reportTeamsToRemove;
    private Boolean sessionInitted;
    private HashMap<Constants.DataRequest, Calendar> lastRequested;

    public SessionManager() {
        this.sessionPopulated = false;
        this.sessionInitted = false;
        this.lastRequested = new HashMap<Constants.DataRequest, Calendar>();
    }



    public void setLastRequested(HashMap<Constants.DataRequest, Calendar> lastRequested) {
        SessionManager.instance.lastRequested = lastRequested;
    }

    public void addSpecificLastRequested(Context context, Constants.DataRequest task, Calendar reqTime) {
        SessionManager.instance.lastRequested.put(task, reqTime);
        if (reqTime.equals(null)) {
            SessionManager.instance.saveLastRequested(context, task, "");
        } else {
            SessionManager.instance.saveLastRequested(context, task, ISO8601DateParser.format(reqTime.getTime()));
        }
    }

    public void removeSpecificLastRequested(Context context, String task) {
        SessionManager.instance.lastRequested.remove(task);
        SessionManager.instance.removeLastRequested(context, task);
    }

    public Calendar getSpecificLastRequested(Context context, Constants.DataRequest task) {
        Calendar lastRequestDate = SessionManager.instance.lastRequested.get(task);
        try {
            if (lastRequestDate == null) {
                lastRequestDate = ISO8601DateParser.parseCal(SessionManager.instance.getLastRequested(context, task));
            }
            if (lastRequestDate != null) {
                SessionManager.instance.addSpecificLastRequested(context, task, lastRequestDate);
                return lastRequestDate;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /* Helper Methods */
    public void saveAuthToken(Context context, String authToken) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesName.DRIVER_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getUserEmail(context), authToken);
        editor.commit();
    }

    public String getAuthToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesName.DRIVER_PREFS, Context.MODE_PRIVATE);
        return prefs.getString(getUserEmail(context), "");
    }

    public void saveUserEmail(Context context, String email) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesName.DRIVER_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("Email", email);
        editor.commit();
    }

    public String getUserEmail(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesName.DRIVER_PREFS, Context.MODE_PRIVATE);
        return prefs.getString("Email", "");
    }

    public void saveUserAuthStatus(Context context, Boolean authStatus) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesName.DRIVER_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("AuthStatus", authStatus.toString());
        editor.commit();
    }

    public Boolean getUserAuthStatus(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesName.DRIVER_PREFS, Context.MODE_PRIVATE);
        String authStatusStr = prefs.getString("AuthStatus", "");
        return authStatusStr != null && authStatusStr.length() > 0 ? authStatusStr.equals("true") : false;
    }

    public void saveGcmIdSentStatus(Context context, Boolean gcmStatus) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesName.DRIVER_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("GcmIdStatus", gcmStatus.toString());
        editor.commit();
    }
    public Boolean getGcmIdSentStatus(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesName.DRIVER_PREFS, Context.MODE_PRIVATE);
        String gcmIdStatusStr = prefs.getString("GcmIdStatus", "");
        return gcmIdStatusStr != null && gcmIdStatusStr.length() > 0 ? gcmIdStatusStr.equals("true") : false;
    }
    public void saveLastRequested(Context context, Constants.DataRequest task, String calTime) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesName.DRIVER_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(task.name(), calTime);
        editor.commit();
    }

    public void removeLastRequested(Context context, String task) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesName.DRIVER_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(task);
        editor.commit();
    }

    public String getLastRequested(Context context, Constants.DataRequest task) {
        SharedPreferences prefs = context.getSharedPreferences(SharedPreferencesName.DRIVER_PREFS, Context.MODE_PRIVATE);
        return prefs.getString(task.name(), "");
    }


    public void saveSession(Activity parent) {
        Gson gson = new Gson();
        AppFileUtils.saveDataFile("latestAuthSessionInstance.json", gson.toJson(this), parent);
    }

    public boolean loadSession(Activity parent) {
        Gson gson = new Gson();
        String json = AppFileUtils.readDataFile("latestAuthSessionInstance.json", parent);
        if (!BaseUtils.isNullOrEmpty(json)) {
            try {
                SessionManager.instance = gson.fromJson(json, SessionManager.class);
                return true;
            } catch (JsonSyntaxException jse) {
                jse.printStackTrace();
            }
        }
        return false;
    }

    public boolean lastUpdatedExpired(Context context, Constants.DataRequest task) {
        Calendar lastUpdated = SessionManager.instance.getSpecificLastRequested(context, task);
        Calendar now = Calendar.getInstance();

        Integer taskTimeout = Constants.GET_TIMEOUT_PER_TASK().get(task);
        if (lastUpdated != null && taskTimeout != null) {
            int taskMillis = taskTimeout * 60 * 1000;
            if ((now.getTimeInMillis() - lastUpdated.getTimeInMillis()) < taskMillis) {
                return false;
            }
        }
        return true;
    }


}
