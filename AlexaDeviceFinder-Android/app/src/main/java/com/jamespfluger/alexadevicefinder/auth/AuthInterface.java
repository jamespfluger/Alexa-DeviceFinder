package com.jamespfluger.alexadevicefinder.auth;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AuthInterface {
    @GET("users/{userid}")
    Call<ArrayList<AuthUserDevice>> getUserDevices(@Path("userid") String userId);

    @GET("users/{userid}/devices/{deviceid}")
    Call<AuthUserDevice> getUserDevice(@Path("userid") String userId, @Path("deviceid") String deviceId);

    @POST("users")
    Call<Void> addUserDevice(@Body AuthUserDevice userDevice);

    @PUT("users")
    Call<Void> updateUserDevice(@Body AuthUserDevice userDevice);
}
