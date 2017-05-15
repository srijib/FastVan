package com.fast.van.utils;

import android.os.Build;

import com.fast.van.BuildConfig;

/**
 * Custom log class overrides Android Log
 *
 * @author Amandeep Singh Bagli
 */
public class Log {

    private static final boolean DEBUG = BuildConfig.DEBUG; // true for printing and false
    // for not


    public Log() {
    }

    /**
     * @param tag
     * @param message
     */
    public static void i(String tag, String message) {
        if (DEBUG) {
            android.util.Log.i(tag, message);

        }
    }


    /**
     * @param tag
     * @param message
     */
    public static void d(String tag, String message) {
        if (DEBUG) {
            android.util.Log.d(tag, message);

        }
    }


    /**
     * @param tag
     * @param message
     */
    public static void e(String tag, String message) {
        if (DEBUG) {
            android.util.Log.e(tag, message);
        }
    }

    /**
     * @param tag
     * @param message
     */
    public static void v(String tag, String message) {
        if (DEBUG) {
            android.util.Log.v(tag, message);

        }
    }


    /**
     * @param tag
     * @param message
     */
    public static void w(String tag, String message) {
        if (DEBUG) {
            android.util.Log.w(tag, message);

        }
    }

    /**
     *
     * @param tag
     * @param message
     * @param throwable
     */
    public static void  e(String tag, String message,Throwable throwable){

        if (DEBUG) {
            android.util.Log.e(tag, message, throwable);
        }

    }
    /**
     *
     * @param tag
     * @param message
     * @param throwable
     */
    public static void  d(String tag, String message,Throwable throwable){

        if (DEBUG) {
            android.util.Log.d(tag, message,throwable);
        }

    }
}
