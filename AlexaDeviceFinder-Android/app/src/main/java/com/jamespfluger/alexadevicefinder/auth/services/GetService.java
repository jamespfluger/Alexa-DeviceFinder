package com.jamespfluger.alexadevicefinder.auth.services;

import com.jamespfluger.alexadevicefinder.auth.AuthInterface;
import com.jamespfluger.alexadevicefinder.auth.AmazonUserDevice;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetService {

    private ArrayList<AmazonUserDevice> userDevices = null;
    private AmazonUserDevice userDevice = null;

    public ArrayList<AmazonUserDevice> getUserDevices(String userId, AuthInterface authApi){
        Call<ArrayList<AmazonUserDevice>> userCall = authApi.getUserDevices(userId);

        userCall.enqueue(new Callback<ArrayList<AmazonUserDevice>>() {
            @Override
            public void onResponse(Call<ArrayList<AmazonUserDevice>> call, Response<ArrayList<AmazonUserDevice>> response) {
                if(response.isSuccessful()){
                    userDevices = response.body();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AmazonUserDevice>> call, Throwable t) {
                userDevices = null;
            }
        });

        return userDevices;
    }

    public AmazonUserDevice getUserDevice(String userId, String deviceId, AuthInterface authApi){
        Call<AmazonUserDevice> userCall = authApi.getUserDevice(userId, deviceId);

        userCall.enqueue(new Callback<AmazonUserDevice>() {
            @Override
            public void onResponse(Call<AmazonUserDevice> call, Response<AmazonUserDevice> response) {
                if(response.isSuccessful()){
                    userDevice = response.body();
                }
            }

            @Override
            public void onFailure(Call<AmazonUserDevice> call, Throwable t) {
                userDevice = null;
            }
        });

        return userDevice;
    }
}
