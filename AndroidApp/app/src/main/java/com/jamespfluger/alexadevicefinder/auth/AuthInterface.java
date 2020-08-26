package com.jamespfluger.alexadevicefinder.auth;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Path;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthInterface {
    @GET("users/{userid}")
    Call<ArrayList<AmazonUserDevice>> getUserDevices(@Path("userid") String userId);

    @GET("users/{userid}/devices/{deviceid}")
    Call<AmazonUserDevice> getUserDevice(@Path("userid") String userId, @Path("deviceid") String deviceId);

    @POST("users")
    Call<Void> addUserDevice(@Body AmazonUserDevice userDevice);
}
