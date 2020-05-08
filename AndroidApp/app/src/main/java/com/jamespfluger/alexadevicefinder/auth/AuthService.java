package com.jamespfluger.alexadevicefinder.auth;

import android.util.Log;

import com.jamespfluger.alexadevicefinder.auth.services.AddService;
import com.jamespfluger.alexadevicefinder.auth.services.DeleteService;
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

    public AuthService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://qsbrgmx8u1.execute-api.us-west-2.amazonaws.com/Prod/devicefinder/auth/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        authApi = retrofit.create(AuthInterface.class);
    }

    public ArrayList<UserDevice> getUserDevices(String userId){
        Call<ArrayList<UserDevice>> userCall = authApi.getUserDevices(userId);

        userCall.enqueue(new Callback<ArrayList<UserDevice>>() {
            @Override
            public void onResponse(Call<ArrayList<UserDevice>> call, Response<ArrayList<UserDevice>> response) {
                if(response.isSuccessful())
                    userDevices = response.body();
                else
                    userDevices = null;
            }

            @Override
            public void onFailure(Call<ArrayList<UserDevice>> call, Throwable t) {
                userDevices = null;
            }
        });

        return userDevices;
    }

    public void addUserDevice(UserDevice userDevice){
        AddService addService = new AddService();
        addService.addUserDevice(userDevice, authApi);
    }

    public void addUserDevice(String userId, String deviceId){
        AddService addService = new AddService();
        addService.addUserDevice(userId, deviceId, authApi);
    }

    public void deleteDevice(UserDevice userDevice){
        DeleteService deleteService = new DeleteService();
        deleteService.deleteDevice(userDevice, authApi);
    }

    public void deleteDevice(String userId, String deviceId){
        DeleteService deleteService = new DeleteService();
        deleteService.deleteDevice(userId, deviceId, authApi);
    }

    public void deleteUser(String userId){
        DeleteService deleteService = new DeleteService();
        deleteService.deleteUser(userId, authApi);
    }
}
