package com.jamespfluger.alexadevicefinder.api;

import com.jamespfluger.alexadevicefinder.models.DeviceSettings;
import com.jamespfluger.alexadevicefinder.models.UserDevice;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ManagementInterface {
    @GET("management/users/{userid}")
    Call<ArrayList<UserDevice>> getAllUserDevices(@Path("userid") String userId);

    @PUT("management/users/{userid}/devices/{deviceid}/settings")
    Call<Void> saveDeviceSettings(@Body DeviceSettings deviceSettings);
}
