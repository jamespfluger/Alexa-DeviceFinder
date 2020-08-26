package com.jamespfluger.alexadevicefinder.auth.services;

import android.util.Log;

import com.jamespfluger.alexadevicefinder.auth.AuthInterface;
import com.jamespfluger.alexadevicefinder.auth.UserDevice;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetService {

    private ArrayList<UserDevice> userDevices = null;
    private UserDevice userDevice = null;

    public ArrayList<UserDevice> getUserDevices(String userId, AuthInterface authApi){
        Call<ArrayList<UserDevice>> userCall = authApi.getUserDevices(userId);

        userCall.enqueue(new Callback<ArrayList<UserDevice>>() {
            @Override
            public void onResponse(Call<ArrayList<UserDevice>> call, Response<ArrayList<UserDevice>> response) {
                if(response.isSuccessful()){
                    userDevices = response.body();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserDevice>> call, Throwable t) {
                userDevices = null;
            }
        });

        return userDevices;
    }

    public UserDevice getUserDevice(String userId, String deviceId, AuthInterface authApi){
        Call<UserDevice> userCall = authApi.getUserDevice(userId, deviceId);

        userCall.enqueue(new Callback<UserDevice>() {
            @Override
            public void onResponse(Call<UserDevice> call, Response<UserDevice> response) {
                if(response.isSuccessful()){
                    userDevice = response.body();
                }
            }

            @Override
            public void onFailure(Call<UserDevice> call, Throwable t) {
                userDevice = null;
            }
        });

        return userDevice;
    }
}
