package com.jamespfluger.alexadevicefinder.auth.services;

import com.jamespfluger.alexadevicefinder.auth.AuthInterface;
import com.jamespfluger.alexadevicefinder.auth.AuthUserDevice;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetService {

    private ArrayList<AuthUserDevice> userDevices = null;
    private AuthUserDevice userDevice = null;

    public ArrayList<AuthUserDevice> getUserDevices(String userId, AuthInterface authApi) {
        Call<ArrayList<AuthUserDevice>> userCall = authApi.getUserDevices(userId);

        userCall.enqueue(new Callback<ArrayList<AuthUserDevice>>() {
            @Override
            public void onResponse(Call<ArrayList<AuthUserDevice>> call, Response<ArrayList<AuthUserDevice>> response) {
                if (response.isSuccessful()) {
                    userDevices = response.body();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AuthUserDevice>> call, Throwable t) {
                userDevices = null;
            }
        });

        return userDevices;
    }

    public AuthUserDevice getUserDevice(String userId, String deviceId, AuthInterface authApi) {
        Call<AuthUserDevice> userCall = authApi.getUserDevice(userId, deviceId);

        userCall.enqueue(new Callback<AuthUserDevice>() {
            @Override
            public void onResponse(Call<AuthUserDevice> call, Response<AuthUserDevice> response) {
                if (response.isSuccessful()) {
                    userDevice = response.body();
                }
            }

            @Override
            public void onFailure(Call<AuthUserDevice> call, Throwable t) {
                userDevice = null;
            }
        });

        return userDevice;
    }
}
