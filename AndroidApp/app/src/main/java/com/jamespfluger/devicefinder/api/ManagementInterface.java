package com.jamespfluger.devicefinder.api;

import com.jamespfluger.devicefinder.models.AuthData;
import com.jamespfluger.devicefinder.models.Device;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ManagementInterface {
    @POST("management/users")
    Call<Device> createDevice(@Body AuthData deviceInfo);

    @PUT("management/users/{alexauserid}/devices/{deviceid}")
    Call<Void> updateDevice(@Body Device device, @Path("alexauserid") String alexaUserId, @Path("deviceid") String deviceId);

    @GET("management/users/{alexauserid}")
    Call<ArrayList<Device>> getAllDevices(@Path("alexauserid") String alexaUserId);

    @DELETE("management/users/{alexauserid}/devices/{deviceid}")
    Call<ArrayList<Device>> deleteDevice(@Path("alexauserid") String alexaUserId, @Path("deviceid") String deviceId);
}
