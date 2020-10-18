package com.jamespfluger.alexadevicefinder.auth.services;

import com.jamespfluger.alexadevicefinder.auth.AuthInterface;
import com.jamespfluger.alexadevicefinder.auth.AuthUserDevice;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class AddService {
    private Boolean isSuccessful;

    public Boolean addUserDevice(AuthUserDevice userDevice, AuthInterface authApi) {
        Call<Void> userCall = authApi.addUserDevice(userDevice);

        userCall.enqueue(new Callback<Void>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call call, Response response) {
                isSuccessful = response.isSuccessful();
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call call, Throwable t) {
                isSuccessful = false;
            }
        });

        return isSuccessful;
    }

    public Boolean addUserDevice(String userId, String deviceId, AuthInterface authApi) {
        Call<Void> userCall = authApi.addUserDevice(new AuthUserDevice(userId, deviceId));

        userCall.enqueue(new Callback<Void>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call call, Response response) {
                isSuccessful = response.isSuccessful();
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call call, Throwable t) {
                isSuccessful = false;
            }
        });

        return isSuccessful;
    }
}
