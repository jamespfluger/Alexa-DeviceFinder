package com.jamespfluger.alexadevicefinder.auth;


import com.jamespfluger.alexadevicefinder.models.AuthUserDevice;
import com.jamespfluger.alexadevicefinder.models.UserDevice;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AuthInterface {
    @POST("users")
    Call<UserDevice> addUserDevice(@Body AuthUserDevice userDevice);

    @PUT("users")
    Call<UserDevice> updateUserDevice(@Body AuthUserDevice userDevice);
}
