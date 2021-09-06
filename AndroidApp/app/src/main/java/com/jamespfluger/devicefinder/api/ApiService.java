package com.jamespfluger.devicefinder.api;

import com.jamespfluger.devicefinder.models.AuthData;
import com.jamespfluger.devicefinder.models.Device;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private static final String BASE_URL = "https://qsbrgmx8u1.execute-api.us-west-2.amazonaws.com/prd/devicefinder/";

    private static final ManagementInterface managementInterface = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ManagementInterface.class);

    public static Call<Device> createDevice(AuthData deviceInfo) {
        return managementInterface.createDevice(deviceInfo);
    }

    public static Call<Void> updateDevice(Device device, String alexaUserId, String deviceId) {
        return managementInterface.updateDevice(device, alexaUserId, deviceId);
    }

    public static Call<ArrayList<Device>> getAllDevices(String alexaUserId) {
        return managementInterface.getAllDevices(alexaUserId);
    }

    public static Call<Void> deleteDevice(String alexaUserId, String deviceId) {
        return managementInterface.deleteDevice(alexaUserId, deviceId);
    }
}
