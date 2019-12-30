package com.jamespfluger.alexadevicefinder.auth.services;

import com.jamespfluger.alexadevicefinder.auth.AuthInterface;
import com.jamespfluger.alexadevicefinder.auth.UserDevice;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeleteService {
    private Boolean isSuccessful;

    public Boolean deleteDevice(UserDevice userDevice, AuthInterface authApi){
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

    public Boolean deleteDevice(String userId, String deviceId, AuthInterface authApi){
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

    public Boolean deleteUser(String userId, AuthInterface authApi){
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
