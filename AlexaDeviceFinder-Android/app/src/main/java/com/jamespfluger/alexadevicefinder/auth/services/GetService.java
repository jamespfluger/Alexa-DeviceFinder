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

    private ArrayList<UserDevice> userDevices;

    public ArrayList<UserDevice> getUserDevices(String userId, AuthInterface authApi){
        Call<ArrayList<UserDevice>> userCall = authApi.getUserDevices(userId);

        userCall.enqueue(new Callback<ArrayList<UserDevice>>() {
            @Override
            public void onResponse(Call<ArrayList<UserDevice>> call, Response<ArrayList<UserDevice>> response) {
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
            public void onFailure(Call<ArrayList<UserDevice>> call, Throwable t) {
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
