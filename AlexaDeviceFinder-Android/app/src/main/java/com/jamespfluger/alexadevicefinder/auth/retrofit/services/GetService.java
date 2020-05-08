package com.jamespfluger.alexadevicefinder.auth.retrofit.services;

import android.util.Log;

import com.jamespfluger.alexadevicefinder.auth.retrofit.AuthDefinition;
import com.jamespfluger.alexadevicefinder.auth.retrofit.AuthUserDevice;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetService {

    private ArrayList<AuthUserDevice> userDevices;

    public ArrayList<AuthUserDevice> getUserDevices(String userId, AuthDefinition authApi){
        Call<ArrayList<AuthUserDevice>> userCall = authApi.getUserDevices(userId);

        userCall.enqueue(new Callback<ArrayList<AuthUserDevice>>() {
            @Override
            public void onResponse(Call<ArrayList<AuthUserDevice>> call, Response<ArrayList<AuthUserDevice>> response) {
                if(response.isSuccessful()){
                    userDevices = response.body();
                    Log.d("DEVICEFINDER",GetService.class.getName() + ":" +  "Response AND Success=" + ((Boolean)response.isSuccessful()).toString());
                }
                else{
                    userDevices = null;
                    Log.d("DEVICEFINDER",GetService.class.getName() + ":" +  "Response AND Failure=" + ((Boolean)response.isSuccessful()).toString());
                    Log.d("DEVICEFINDER",GetService.class.getName() + ":" +  response.message());
                    Log.d("DEVICEFINDER",GetService.class.getName() + ":" +  response.toString());
                    try {
                        Log.d("DEVICEFINDER",GetService.class.getName() + ":" +  response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<AuthUserDevice>> call, Throwable t) {
                Log.d("DEVICEFINDER",GetService.class.getName() + ":" +  "Complete Failure");
                Log.d("DEVICEFINDER",GetService.class.getName() + ":" +  "Call.isExecuted=" + call.isExecuted());
                Log.d("DEVICEFINDER",GetService.class.getName() + ":" +  "Call.isExecuted=" + t.getCause());
                Log.d("DEVICEFINDER",GetService.class.getName() + ":" +  "Throwable.localizedMessage)=" + t.getLocalizedMessage());
                Log.d("DEVICEFINDER",GetService.class.getName() + ":" +  "Throwable.toString=" + t.toString());
                userDevices = null;
            }
        });

        return userDevices;
    }
}
