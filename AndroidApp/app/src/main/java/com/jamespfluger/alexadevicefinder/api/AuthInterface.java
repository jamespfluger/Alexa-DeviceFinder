package com.jamespfluger.alexadevicefinder.api;


import com.jamespfluger.alexadevicefinder.models.AuthUserDevice;
import com.jamespfluger.alexadevicefinder.models.UserDevice;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface AuthInterface {
    @POST("users")
    Call<UserDevice> addUserDevice(@Body AuthUserDevice userDevice);

    @PUT("users")
    Call<UserDevice> updateUserDevice(@Body AuthUserDevice userDevice);
}
