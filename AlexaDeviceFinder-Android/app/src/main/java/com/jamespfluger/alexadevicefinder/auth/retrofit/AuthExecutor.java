package com.jamespfluger.alexadevicefinder.auth.retrofit;

import com.jamespfluger.alexadevicefinder.auth.retrofit.services.AddService;
import com.jamespfluger.alexadevicefinder.auth.retrofit.services.DeleteService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("ALL")
public class AuthExecutor {

    private AuthDefinition authApi;
    private ArrayList<AuthUserDevice> userDevices;

    public AuthExecutor(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://alexadevicefinderauth.conveyor.cloud/devicefinder/auth/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        authApi = retrofit.create(AuthDefinition.class);
    }

    public ArrayList<AuthUserDevice> getUserDevices(String userId){
        Call<ArrayList<AuthUserDevice>> userCall = authApi.getUserDevices(userId);

        userCall.enqueue(new Callback<ArrayList<AuthUserDevice>>() {
            @Override
            public void onResponse(Call<ArrayList<AuthUserDevice>> call, Response<ArrayList<AuthUserDevice>> response) {
                if(response.isSuccessful())
                    userDevices = response.body();
                else
                    userDevices = null;
            }

            @Override
            public void onFailure(Call<ArrayList<AuthUserDevice>> call, Throwable t) {
                userDevices = null;
            }
        });

        return userDevices;
    }

    public void addUserDevice(AuthUserDevice userDevice){
        AddService addService = new AddService();
        addService.addUserDevice(userDevice, authApi);
    }

    public void addUserDevice(String userId, String deviceId){
        AddService addService = new AddService();
        addService.addUserDevice(userId, deviceId, authApi);
    }

    public void deleteDevice(AuthUserDevice userDevice){
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
