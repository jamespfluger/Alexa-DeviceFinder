package com.jamespfluger.devicefinder.utilities;

import android.content.Context;
import android.content.res.Resources;

import com.jamespfluger.devicefinder.models.Device;

import java.util.ArrayList;

public class DeviceManager {
    private static ArrayList<Device> devices;
    private static Resources resources;

    public DeviceManager(Context context) {
        resources = context.getResources();
    }

    public static ArrayList<Device> getDevices() {
        return devices;
    }

    public static void setDevices(ArrayList<Device> newDevices) {
        devices = newDevices;
    }

    public static Device findDeviceById(String id) {
        for (Device device : devices) {
            if (device.getDeviceId().equals(id)) {
                return device;
            }
        }

        return null;
    }
}
