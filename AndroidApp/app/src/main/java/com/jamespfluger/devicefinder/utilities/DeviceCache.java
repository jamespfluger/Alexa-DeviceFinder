package com.jamespfluger.devicefinder.utilities;

import android.view.View;

import androidx.databinding.library.baseAdapters.BR;

import com.jamespfluger.devicefinder.models.Device;

import java.util.Hashtable;

public class DeviceCache {
    private static final Hashtable<String, Device> cachedDevices = new Hashtable<>();

    public static void addDevice(Device originalDevice) {
        if (!cachedDevices.containsKey(originalDevice.getDeviceId())) {
            Device newCachedDevice = new Device(originalDevice);
            cachedDevices.put(newCachedDevice.getDeviceId(), newCachedDevice);
        }
    }

    public static void refreshDevice(Device originalDevice) {
        cachedDevices.remove(originalDevice.getDeviceId());
        originalDevice.setPendingChangesVisiblity(View.INVISIBLE);
        originalDevice.notifyPropertyChanged(BR.pendingChangesVisiblity);
        addDevice(originalDevice);
    }

    public static int hasPendingChanges(Device currentDevice) {
        Device cachedDevice = cachedDevices.get(currentDevice.getDeviceId());

        if (cachedDevice == null || compareDeviceSettings(currentDevice, cachedDevice)) {
            return View.VISIBLE;
        } else {
            return View.INVISIBLE;
        }
    }

    public static boolean compareDeviceSettings(Device currentDevice, Device cachedDevice) {
        return !currentDevice.getDeviceName().equals(cachedDevice.getDeviceName()) ||
                currentDevice.getUseFlashlight() != cachedDevice.getUseFlashlight() ||
                currentDevice.getUseVibrate() != cachedDevice.getUseVibrate() ||
                currentDevice.getUseOnWifiOnly() != cachedDevice.getUseOnWifiOnly() ||
                !currentDevice.getWifiSsid().equals(cachedDevice.getWifiSsid()) ||
                currentDevice.getUseVolumeOverride() != cachedDevice.getUseVolumeOverride() ||
                currentDevice.getVolumeOverrideValue() != cachedDevice.getVolumeOverrideValue();
    }
}
