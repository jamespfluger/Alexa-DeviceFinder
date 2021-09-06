package com.jamespfluger.devicefinder.utilities;

import com.jamespfluger.devicefinder.models.Device;

import java.util.ArrayList;

public class DeviceManager {
    private static ArrayList<Device> devices = new ArrayList<>();

    public static ArrayList<Device> getDevices() {
        return devices;
    }

    public static void setDevices(ArrayList<Device> newDevices) {
        devices.clear();
        devices = newDevices;
    }

    public static void removeDevice(Device deviceToRemove) {
        devices.remove(deviceToRemove);
    }

    public static void removeDevice(String deviceIdToRemove) {
        Device deviceToRemove = null;

        for (Device device : devices) {
            if (device.getDeviceId().equals(deviceIdToRemove)) {
                deviceToRemove = device;
                break;
            }
        }

        devices.remove(deviceToRemove);
    }

    public static boolean isEmpty() {
        return devices.size() == 0;
    }

    public static Device findDeviceById(String id) {
        for (Device device : devices) {
            if (device.getDeviceId().equals(id)) {
                return device;
            }
        }

        return null;
    }

    public static boolean containsDevice(String id) {
        for (Device device : devices) {
            if (device.getDeviceId().equals(id)) {
                return true;
            }
        }

        return false;
    }
}
