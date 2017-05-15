package com.fast.van;

import android.app.Application;
import android.content.Context;


import com.crashlytics.android.Crashlytics;
//import com.crashlytics.android.ndk.CrashlyticsNdk;
import com.fast.van.fonts.Typekit;

import com.fast.van.utils.Log;
import io.fabric.sdk.android.Fabric;


/**
 * Created by Amandeep Singh Bagli on 21/09/15.
 */
public class FastVanApplication  extends Application {



    @Override
    public void onCreate() {
        super.onCreate();

       // Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
     Fabric.with(this, new Crashlytics());
        try {
            Typekit.getInstance()
                    .addBlack(Typekit.createFromAsset(this, "TitilliumWeb-Black.ttf"))
                    .addBold(Typekit.createFromAsset(this, "TitilliumWeb-Bold.ttf"))
                    .addBoldItalic(Typekit.createFromAsset(this, "TitilliumWeb-BoldItalic.ttf"))
                    .addExtraLight(Typekit.createFromAsset(this, "TitilliumWeb-ExtraLight.ttf"))
                    .addExtraLightItalic(Typekit.createFromAsset(this, "TitilliumWeb-ExtraLightItalic.ttf"))
                    .addItalic(Typekit.createFromAsset(this, "TitilliumWeb-Italic.ttf"))
                    .addLight(Typekit.createFromAsset(this, "TitilliumWeb-Light.ttf"))
                    .addLightItalic(Typekit.createFromAsset(this, "TitilliumWeb-LightItalic.ttf"))
                     .addRegular(Typekit.createFromAsset(this, "TitilliumWeb-Regular.ttf"))
                    //  .addRegular(Typekit.createFromAsset(this, "test-font.ttf"))
                    .addSemiBold(Typekit.createFromAsset(this, "TitilliumWeb-SemiBold.ttf"))
                    .addSemiBoldItalic(Typekit.createFromAsset(this, "TitilliumWeb-SemiBoldItalic.ttf"));
        } catch (Exception e) {
            Log.e("Application","Font fail to initialize",e);
            System.exit(0);
        }

        //Black,Bold,BoldItalic,ExtraLight,ExtraLightItalic,Italic,Light,LightItalic,Regular,SemiBold,SemiBoldItalic
       // Prefs.with(getApplicationContext()).save(SharedPreferencesName.UPDATE_POPUP_SHOWN, false);
    }



}
