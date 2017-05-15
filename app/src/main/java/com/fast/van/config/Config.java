package com.fast.van.config;


/**
 * Created by Amandeep Singh Bagli on 21/09/15.
 */
public class Config {

    private static String GCM_PROJECT_NUMBER = "";
    static String BASE_URL = "";
    static String FLURRY_KEY = "";
    static String BROWSER_KEY = "";
    static AppMode appMode = AppMode.LIVE;


    /**
     * Base URL
     *
     * @return
     */
    static public String getBaseURL() {
        init(appMode);
        return BASE_URL;
    }


    /**
     * FlurryKey
     *
     * @return
     */
    static public String getFlurryKey() {
        init(appMode);

        return FLURRY_KEY;
    }


    /**
     * GCM project number
     *
     * @return
     */
    static public String getGCMProjectNumber() {
        init(appMode);
        return GCM_PROJECT_NUMBER;
    }


    /**
     * Browser Key
     *
     * @return
     */
    static public String getBrowserKey() {
        init(appMode);
        return BROWSER_KEY;
    }


    /**
     * Initialize all the variable in this method
     *
     * @param appMode
     */
    public static void init(AppMode appMode) {

        switch (appMode) {
            case DEV:
//                BASE_URL = "http://54.186.74.153:8000";
                BASE_URL = "http://192.168.0.123:8002";//"http://54.186.74.153:8000";//"
                FLURRY_KEY = "";
                GCM_PROJECT_NUMBER = "799492082381";
                BROWSER_KEY = "AIzaSyC0g4ehb2KQK_ODMS2DYpG2teesM_mYJQ8";
                break;

            case TEST:
                BASE_URL = "http://fastvans.clicklabs.in:8000";
                FLURRY_KEY = "";
                GCM_PROJECT_NUMBER = "799492082381";
                BROWSER_KEY = "AIzaSyC0g4ehb2KQK_ODMS2DYpG2teesM_mYJQ8";
                break;
            case LIVE:
                BASE_URL = "http://api.fastvan.com:8000";
                FLURRY_KEY = "";
                GCM_PROJECT_NUMBER = "799492082381";
                BROWSER_KEY = "AIzaSyC0g4ehb2KQK_ODMS2DYpG2teesM_mYJQ8";
                break;

        }


    }


    public enum AppMode {
        DEV, TEST, LIVE
    }

}