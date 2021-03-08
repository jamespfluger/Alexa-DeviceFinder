package com.jamespfluger.devicefinder.api;


import com.jamespfluger.devicefinder.models.AuthUserDevice;
import com.jamespfluger.devicefinder.models.UserDevice;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthInterface {
    @POST("auth/users")
    Call<UserDevice> addAuthUserDevice(@Body AuthUserDevice userDevice);
}
