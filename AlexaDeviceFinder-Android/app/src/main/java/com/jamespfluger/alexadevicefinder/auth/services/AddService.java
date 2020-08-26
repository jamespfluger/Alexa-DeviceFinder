package com.jamespfluger.alexadevicefinder.auth.services;

import com.jamespfluger.alexadevicefinder.auth.AuthInterface;
import com.jamespfluger.alexadevicefinder.auth.AmazonUserDevice;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddService {

    private Boolean isSuccessful;

    public Boolean addUserDevice(AmazonUserDevice userDevice, AuthInterface authApi){
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

    public Boolean addUserDevice(String userId, String deviceId, AuthInterface authApi){
        Call<Void> userCall = authApi.addUserDevice(new AmazonUserDevice(userId, deviceId));

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
