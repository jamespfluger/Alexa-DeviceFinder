package com.jamespfluger.devicefinder.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.jamespfluger.devicefinder.R;
import com.jamespfluger.devicefinder.activities.DevicesConfigActivity;
import com.jamespfluger.devicefinder.api.ApiService;
import com.jamespfluger.devicefinder.api.ManagementInterface;
import com.jamespfluger.devicefinder.models.Device;
import com.jamespfluger.devicefinder.settings.ConfigManager;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceManager {
    private static ArrayList<Device> devices;
    private static Resources resources;

    public DeviceManager(Context context) {
        resources = context.getResources();
    }

    public static ArrayList<Device> getDevices() {
        return devices;
    }

    public static Device findDeviceById(String id) {
        for (Device device : devices) {
            if (device.getDeviceId().equals(id)) {
                return device;
            }
        }

        return null;
    }

    public static void setDevices(ArrayList<Device> devices) {
        this.devices = devices;
    }

    public static void refreshDevices(Context context) {
        this.devices.clear();

        ManagementInterface managementApi = ApiService.createInstance();
        Call<ArrayList<Device>> userCall = managementApi.getAllDevices(ConfigManager.getAlexaUserIdConfig());
        userCall.enqueue(new Callback<ArrayList<Device>>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful() && response.body() instanceof ArrayList) {
                    devices = (ArrayList<Device>) response.body();
                } else {
                    try {
                        String errorMessage = response.errorBody() != null ? response.errorBody().string() : String.format(context.getResources().getString(R.string.unknown_error_http_message), response.code());
                        Toast.makeText(context, context.getResources().getString(R.string.unable_to_load_devices) + errorMessage, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Toast.makeText(context, context.getResources().getString(R.string.unable_to_load_devices) + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
