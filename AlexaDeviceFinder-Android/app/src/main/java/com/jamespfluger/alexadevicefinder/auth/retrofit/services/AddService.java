package com.jamespfluger.alexadevicefinder.auth.retrofit.services;

import com.jamespfluger.alexadevicefinder.auth.retrofit.AuthDefinition;
import com.jamespfluger.alexadevicefinder.auth.retrofit.AuthUserDevice;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddService {

    private Boolean isSuccessful;

    public Boolean addUserDevice(AuthUserDevice userDevice, AuthDefinition authApi){
        Call<Void> userCall = authApi.addUserDevice(userDevice);

        userCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call call, Response response) {
                isSuccessful = response.isSuccessful();
;            }

            @Override
            public void onFailure(Call call, Throwable t) {
                isSuccessful = false;
            }
        });

        return isSuccessful;
    }

    public Boolean addUserDevice(String userId, String deviceId, AuthDefinition authApi){
        Call<Void> userCall = authApi.addUserDevice(new AuthUserDevice(userId, deviceId));

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
