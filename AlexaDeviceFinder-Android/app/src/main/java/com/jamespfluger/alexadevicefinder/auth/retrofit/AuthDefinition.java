package com.jamespfluger.alexadevicefinder.auth.retrofit;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Path;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthDefinition {
    @GET("users/{userid}")
    Call<ArrayList<AuthUserDevice>> getUserDevices(@Path("userid") String userId);

    @POST("users")
    Call<Void> addUserDevice(@Body AuthUserDevice userDevice);

    @DELETE("users/{userid}/devices/{deviceid}")
    Call<Void> deleteDevice(@Path("userid") String userId, @Path("deviceid") String deviceId);

    @DELETE("users/{userid}")
    Call<Void> deleteUser(@Path("userid") String userId);
}
