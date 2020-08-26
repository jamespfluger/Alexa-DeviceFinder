package com.jamespfluger.alexadevicefinder.auth;

import android.util.Log;

import com.jamespfluger.alexadevicefinder.auth.services.AddService;
import com.jamespfluger.alexadevicefinder.auth.services.GetService;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("ALL")
public class AuthService {

    private AuthInterface authApi;
    private ArrayList<UserDevice> userDevices;

    public AuthService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://qsbrgmx8u1.execute-api.us-west-2.amazonaws.com/Prod/devicefinder/auth/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        authApi = retrofit.create(AuthInterface.class);
    }

    public ArrayList<UserDevice> getUserDevices(String userId) {
        GetService getService = new GetService();
        return getService.getUserDevices(userId, authApi);
    }

    public UserDevice getUserDevice(String userId, String deviceId) {
        GetService getService = new GetService();
        return getService.getUserDevice(userId, deviceId, authApi);
    }

    public void addUserDevice(UserDevice userDevice) {
        AddService addService = new AddService();
        addService.addUserDevice(userDevice, authApi);
    }

    public void addUserDevice(String userId, String deviceId) {
        AddService addService = new AddService();
        addService.addUserDevice(userId, deviceId, authApi);
    }
}
