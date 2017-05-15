package com.fast.van.common;

import com.fast.van.utils.Log;

public class DebugLog {

    private static String DEBUG_TAG = "SpVAN";

    public static void console(String str) {
		Log.i(DEBUG_TAG, str);
    }

    public static void console(Exception e, String msg) {
		Log.e(DEBUG_TAG, " - "+e.toString() + " : "+msg);

    }

}
