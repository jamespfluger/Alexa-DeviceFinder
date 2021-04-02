package com.jamespfluger.devicefinder.api;

import com.jamespfluger.devicefinder.models.AuthData;
import com.jamespfluger.devicefinder.models.Device;
import com.jamespfluger.devicefinder.models.DeviceSettings;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ManagementInterface {
    @POST("management/users")
    Call<Device> addNewDevice(@Body AuthData deviceInfo);

    @GET("management/users/{userid}")
    Call<ArrayList<Device>> getAllDevices(@Path("userid") String userId);

    @PUT("management/users/{userid}/devices/{deviceid}/settings")
    Call<Void> saveDeviceSettings(@Body DeviceSettings deviceSettings);
}
