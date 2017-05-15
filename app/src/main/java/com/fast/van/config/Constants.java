package com.fast.van.config;

import java.util.HashMap;

/**
 * Created by Amandeep Singh Bagli on 08/10/15.
 */
public class Constants {
    private static final HashMap<DataRequest, Integer> TIMEOUT_PER_REQUEST = new HashMap<DataRequest, Integer>();
    public static final String APP_VERSION="101";
    public static final HashMap<DataRequest, Integer> GET_TIMEOUT_PER_TASK() {
        if (TIMEOUT_PER_REQUEST.isEmpty()) {
            TIMEOUT_PER_REQUEST.put(DataRequest.SESSION, 2);
            TIMEOUT_PER_REQUEST.put(DataRequest.PARTNERS,20);
            TIMEOUT_PER_REQUEST.put(DataRequest.ORDERS, 0);
            TIMEOUT_PER_REQUEST.put(DataRequest.NOTIFICATIONS, 2);

        }
        return TIMEOUT_PER_REQUEST;
    }


    public enum DataRequest {
        SESSION,
        PARTNERS,
        ORDERS,
        NOTIFICATIONS
    }
}
