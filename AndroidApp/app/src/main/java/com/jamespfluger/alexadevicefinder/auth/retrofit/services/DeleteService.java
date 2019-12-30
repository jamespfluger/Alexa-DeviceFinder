package com.jamespfluger.alexadevicefinder.auth.retrofit.services;

import com.jamespfluger.alexadevicefinder.auth.retrofit.AuthDefinition;
import com.jamespfluger.alexadevicefinder.auth.retrofit.AuthUserDevice;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteService {
    private Boolean isSuccessful;

    public Boolean deleteDevice(AuthUserDevice userDevice, AuthDefinition authApi){
        Call<Void> userCall = authApi.deleteDevice(userDevice.getUserId(), userDevice.getDeviceId());

        userCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call call, Response response) {
                isSuccessful = response.isSuccessful();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                isSuccessful = false;
            }
        });

        return isSuccessful;
    }

    public Boolean deleteDevice(String userId, String deviceId, AuthDefinition authApi){
        Call<Void> userCall = authApi.deleteDevice(userId, deviceId);

        userCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call call, Response response) {
                isSuccessful = response.isSuccessful();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                isSuccessful = false;
            }
        });

        return isSuccessful;
    }

    public Boolean deleteUser(String userId, AuthDefinition authApi){
        Call<Void> userCall = authApi.deleteUser(userId);

        userCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call call, Response response) {
                isSuccessful = response.isSuccessful();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                isSuccessful = false;
            }
        });

        return isSuccessful;
    }
}
