package com.jamespfluger.alexadevicefinder.auth.services;

import com.jamespfluger.alexadevicefinder.auth.AuthInterface;
import com.jamespfluger.alexadevicefinder.auth.AuthUserDevice;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateService {
    private Boolean isSuccessful;

    public Boolean updateUserDevice(AuthUserDevice userDevice, AuthInterface authApi) {
        Call<Void> userCall = authApi.updateUserDevice(userDevice);

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

    public Boolean updateUserDevice(String userId, String deviceId, AuthInterface authApi) {
        Call<Void> userCall = authApi.updateUserDevice(new AuthUserDevice(userId, deviceId));

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

    public Boolean updateUserDevice(String userId, String deviceId, String otp, AuthInterface authApi) {
        Call<Void> userCall = authApi.updateUserDevice(new AuthUserDevice(userId, deviceId, otp));

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
