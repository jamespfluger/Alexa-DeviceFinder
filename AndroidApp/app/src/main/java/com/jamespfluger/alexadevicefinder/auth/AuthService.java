package com.jamespfluger.alexadevicefinder.auth;

import com.jamespfluger.alexadevicefinder.models.AuthUserDevice;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AuthService {

    private AuthInterface authApi;
    private ArrayList<AuthUserDevice> userDevices;

    public AuthService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://qsbrgmx8u1.execute-api.us-west-2.amazonaws.com/prod/devicefinder/auth/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        authApi = retrofit.create(AuthInterface.class);
    }

    /*
    public ArrayList<AuthUserDevice> getUserDevices(String userId) {
        GetService getService = new GetService();
        return getService.getUserDevices(userId, authApi);
    }

    public AuthUserDevice getUserDevice(String userId, String deviceId) {
        GetService getService = new GetService();
        return getService.getUserDevice(userId, deviceId, authApi);
    }

    public void addUserDevice(AuthUserDevice userDevice) {
        AddService addService = new AddService();
        addService.addUserDevice(userDevice, authApi);
    }

    public void addUserDevice(String userId, String deviceId) {
        AddService addService = new AddService();
        addService.addUserDevice(userId, deviceId, authApi);
    }

    public void updateUserDevice(AuthUserDevice userDevice) {
        UpdateService updateService = new UpdateService();
        updateService.updateUserDevice(userDevice, authApi);
    }

    public void updateUserDevice(String userId, String deviceId) {
        UpdateService updateService = new UpdateService();
        updateService.updateUserDevice(userId, deviceId, authApi);
    }

    public void updateUserDevice(String userId, String deviceId, String otp) {
        UpdateService updateService = new UpdateService();
        updateService.updateUserDevice(userId, deviceId, otp, authApi);
    }*/
}
