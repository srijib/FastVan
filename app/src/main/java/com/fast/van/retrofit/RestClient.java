package com.fast.van.retrofit;


import com.fast.van.config.Config;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;


/**
 * Rest client
 */
public class RestClient {

    /**
     * ApiService instance for string response
     *
     * @return
     */

    public static ApiService getApiService() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Config.getBaseURL())
                .setLogLevel(RestAdapter.LogLevel.NONE)
                .setClient(getOkHttpClient())
                .setConverter(new StringConverter())    //converter for response type
                .build();
        return restAdapter.create(ApiService.class);

    }


    /**
     * ApiService instance for POJO response
     *
     * @return
     */
    public static ApiService getApiServiceForPojo() {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Config.getBaseURL())
                .setLogLevel(RestAdapter.LogLevel.NONE)
                .setClient(getOkHttpClient())
                .build();


        return restAdapter.create(ApiService.class);

    }

    /**
     * ApiService instance with geocode api base URL
     *
     * @return
     */
    public static ApiService getApiServiceForAddress() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://maps.googleapis.com/maps/api/geocode")
                .setClient(getOkHttpClient())
                .setConverter(new StringConverter())    //converter for response type
                .build();
        return restAdapter.create(ApiService.class);

    }


    /**
     * ApiService instance with place api base URL
     *
     * @return
     */
    public static ApiService getPlaceSearchResult() {

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("https://maps.googleapis.com/maps/api/place")
                .setClient(getOkHttpClient())
                .setConverter(new StringConverter())  //converter for response type
                .build();
        return restAdapter.create(ApiService.class);

    }

    /**
     * get OkHttpClient
     *
     * @return OkClient
     */
    private static OkClient getOkHttpClient() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(15, TimeUnit.SECONDS); // connect timeout
        client.setReadTimeout(15, TimeUnit.SECONDS);
        return new OkClient(client);
    }

}
