package com.fast.van.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.DisplayMetrics;


import com.fast.van.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

/**
 * Created by ramangoyal on 22/06/15.
 */
public class CommonUtils {


    public static String getDeviceType() {
        return "ANDROID";
    }

    public static DecimalFormat decimalFormat = new DecimalFormat("#.00");

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return (model.toUpperCase());
        } else {
            return (manufacturer + " " + model).toUpperCase();
        }
    }


    /**
     * @param mContext
     * @return
     */
    public static String getAppVersion(Context mContext) {
        int versionCode = 100;
        try {
            versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode + "";
    }


    /**
     * @param mContext
     * @return
     */
    public static int getAppVersionInt(Context mContext) {
        int versionCode = 100;
        try {
            versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * @param mContext
     * @return
     */
    public static String getDeviceToken(Context mContext) {
         return CommonData.getDeviceToken(mContext);
     }


    /**
     * @param target
     * @return
     */
    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    /**
     * @param dp
     * @param context
     * @return
     */
    public static float convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }

    /**
     * @param pContext
     */
    public static void printHashKey(Context pContext) {
        // Add code to print out the key hash
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(
                    "com.selfdrive",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    /**
     * @param context
     * @param link
     */
//    public static void OpenBrowserWithLink(Activity context, String link) {
//        Intent i = new Intent(context, WebViewActivity.class);
//        i.putExtra("link", link);
//        context.startActivity(i);
//        context.overridePendingTransition(R.anim.animation_from_right, R.anim.animation_to_left);
//    }


    public static Spannable GetBookingUpdatedText(Context context, String bookingId) {
        String cmsg = "Booking " + bookingId + " updated";
        int boookingIdLength = bookingId.length();
        Spannable wordtoSpan = new SpannableString(cmsg);
        wordtoSpan.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.step_in_text_color)), "Booking ".length(), "Booking ".length() + boookingIdLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return wordtoSpan;
    }

}
