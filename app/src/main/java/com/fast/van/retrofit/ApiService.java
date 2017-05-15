package com.fast.van.retrofit;


import com.fast.van.models.Command;
import com.fast.van.models.ServiceReply;
import com.fast.van.models.login.Login;
import com.fast.van.models.notifications.Notification;
import com.fast.van.models.order.Order;
import com.fast.van.models.signup.CompanyList;
import com.fast.van.models.signup.UserData;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.PartMap;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedString;

/**
 * Define all server calls here
 */
public interface ApiService {


    /**
     *
     * @param firstname
     * @param lastname
     * @param email
     * @param phoneNumberObj
     * @param companyname
     * @param seviceObj
     * @param vehicleObj
     * @param addressLatLongObj
     * @param password
     * @param vehicleRegistrationNumber
     * @param deviceDetailsObj
     * @param appVersion
     * @param file
     * @param callback
     */
    @Multipart
    @POST("/api/driver/registerDriver")
    void signUpEmail(@Part("firstName") TypedString firstname,@Part("lastName") TypedString lastname, @Part("email") TypedString email, @Part("phone") TypedString phoneNumberObj,@Part("companyName") TypedString companyname,
                     @Part("service") TypedString seviceObj,@Part("vehicle") TypedString vehicleObj,@Part("truckType") TypedString truckObj,@Part("addressLatLong") TypedString addressLatLongObj,
                     @Part("password") TypedString password, @Part("vehicleRegistrationNumber") TypedString vehicleRegistrationNumber,
                     @Part("deviceDetails") TypedString deviceDetailsObj,@Part("appVersion") TypedString appVersion,@PartMap Map<String, TypedFile> file, Callback<UserData> callback);

    /**
     *
     * @param accessToken
     * @param verificationToken
     */
    @FormUrlEncoded
    @POST("/api/driver/verifySignUp")
    void verifySignUp(@Field("accessToken") String accessToken,@Field("verificationToken") String verificationToken,Callback<Login> callback);

      /**
     *
     * @param accessToken
     *
     */
    @FormUrlEncoded
    @POST("/api/driver/resendVerificationToken")
    void resendVerificationToken(@Field("accessToken") String accessToken,Callback<ServiceReply> callback);

    /**
     *
     * @param phoneNumber
     * @param password
     * @param deviceName
     * @param deviceToken
     * @param addressLatLongObj
     * @param callback
     */
    @FormUrlEncoded
    @POST("/api/driver/driverPhoneLogin")
    void driverPhoneLogin(@Field("phoneNumber") String phoneNumber,@Field("password") String password,@Field("deviceName") String deviceName,@Field("deviceType") String deviceType,@Field("deviceToken") String deviceToken,@Field("addressLatLong") String addressLatLongObj,@Field("appVersion") String appVersion,Callback<Login> callback);
/*  "accessToken": "",
  "deviceName": "",
  "deviceToken": "",
  "addressLatLong": {
    "latitude": 0,
    "longitude": 0
  }*/

    /**
     *
     * @param accessToken
     * @param deviceName
     * @param deviceToken
     * @param addressLatLongObj
     * @param callback
     */
    @FormUrlEncoded
    @POST("/api/driver/driverAccessTokenLogin")
    void driverAccessTokenLogin(@Field("accessToken") String accessToken,@Field("deviceName") String deviceName,@Field("deviceToken") String deviceToken,@Field("addressLatLong") String addressLatLongObj,@Field("appVersion") String appVersion,Callback<Login> callback);

    /**
     *
     * @param callback
     */
    @GET("/api/driver/getPartnerList")
    void getPartnerList(Callback<CompanyList> callback);

    /**
     *
     * @param accessToken
     * @param callback
     */
  @GET("/api/driver/getDriverInfo/{accessToken}")
    void getDriverInfo(@Path("accessToken") String accessToken,Callback<ServiceReply> callback);


    /**
     *
     * @param phoneNumber
     * @param callback
     */
    @FormUrlEncoded
    @POST("/api/driver/driverForgotPassword")
    void driverForgotPassword(@Field("phoneNumber") String phoneNumber, Callback<ServiceReply> callback);

    /**
     *
     * @param accessToken
     * @param oldPassword
     * @param newPassword
     * @param callback
     */
 @FormUrlEncoded
    @POST("/api/driver/driverResetPassword")
    void driverResetPassword(@Field("accessToken") String accessToken,@Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword, Callback<ServiceReply> callback);


    /**
     *
     * @param accessToken
     * @param callback
     */
    @GET("/api/driver/getMyBooking/{accessToken}")
    void getMyBooking(@Path("accessToken") String accessToken,Callback<Order> callback);

    /**
     *
     * @param accessToken
     * @param orderId
     * @param callback
     */
    @GET("/api/driver/getBookinginfo/{accessToken}/orderId/{orderId}")
    void getBookinginfo(@Path("accessToken") String accessToken,@Path("orderId") String orderId,Callback<Order> callback);


    @Multipart
    @PUT("/api/driver/editDriverInfo")
    void editDriverInfo(@Part("accessToken") TypedString accessToken,@Part("firstName") TypedString firstname,@Part("lastName") TypedString lastname,  @Part("service") TypedString seviceObj,@Part("vehicle") TypedString vehicleObj,@Part("addressLatLong") TypedString addressLatLongObj,@PartMap Map<String, TypedFile> file,  Callback<Login> callback);


    /**
     *
     * @param accessToken
     * @param orderId
     * @param status
     * @param callback
     */
    @FormUrlEncoded
    @POST("/api/driver/acceptRequestByPartner")
    void acceptRequestByPartner(@Field("accessToken") String accessToken,@Field("orderId") String orderId, @Field("status") Command status, Callback<ServiceReply> callback);

    /**
     *
     * @param accessToken
     * @param orderId
     * @param orderStatus
     * @param callback
     */
    @FormUrlEncoded
    @PUT("/api/driver/updateOrderStatus")
    void updateOrderStatus(@Field("accessToken") String accessToken,@Field("orderId") String orderId, @Field("orderStatus") Command orderStatus, Callback<ServiceReply> callback);


    /**
     *
     * @param accessToken
     * @param callback
     */
    @GET("/api/driver/getTodayBooking/{accessToken}")
    void getTodayBooking(@Path("accessToken") String accessToken,Callback<Order> callback);

    /**
     *
     * @param accessToken
     * @param addressLatLongObj
     * @param callback
     */
    @FormUrlEncoded
    @PUT("/api/driver/updateDriverLocation")
    void updateDriverLocation(@Field("accessToken") String accessToken,@Field("addressLatLong") String addressLatLongObj,Callback<ServiceReply> callback);



    /**
     *
     * @param accessToken
     * @param callback
     */
    @GET("/api/driver/getNotification/{accessToken}")
    void getNotification(@Path("accessToken") String accessToken,Callback<Notification> callback);

 /**
     *
     * @param accessToken
     * @param callback
     */
 @FormUrlEncoded
    @POST("/api/driver/clearNotification")
    void clearNotification(@Field("accessToken") String accessToken,Callback<ServiceReply> callback);


    /**
     *
     * @param accessToken
     *
     * * @param callback
     */
    @FormUrlEncoded
    @PUT("/api/driver/driverLogout")
    void driverLogout(@Field("accessToken") String accessToken,Callback<ServiceReply> callback);







    /**
     * @param email
     * @param password
     * @param deviceType
     * @param deviceName
     * @param deviceToken
     * @param appVersion
     * @param callback
     */
    @FormUrlEncoded
    @POST("/api/customer/login")
    void LoginWithEmail(@Field("email") String email, @Field("password") String password, @Field("deviceType") String deviceType, @Field("deviceName") String deviceName, @Field("deviceToken") String deviceToken, @Field("appVersion") String appVersion, Callback<UserData> callback);





    /**
     * @param customerID
     * @param accessToken
     * @param callback
     */
    @FormUrlEncoded
    @PUT("/api/customer/logout/{customerID}")
    void Logout(@Path("customerID") String customerID, @Field("accessToken") String accessToken, Callback<String> callback);


    /**
     * @param customerID
     * @param accessToken
     * @param oldPassword
     * @param newPassword
     * @param callback
     */
    @FormUrlEncoded
    @POST("/api/customer/changePassword")
    void ChangePassword(@Field("customerID") String customerID, @Field("accessToken") String accessToken, @Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword, Callback<String> callback);


    /**
     * @param email
     * @param callback
     */
    @FormUrlEncoded
    @POST("/api/customer/forgotPassword")
    void ForgotPassword(@Field("email") String email, Callback<String> callback);



}