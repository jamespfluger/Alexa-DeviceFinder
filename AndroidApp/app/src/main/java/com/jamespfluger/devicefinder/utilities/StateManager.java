package com.jamespfluger.devicefinder.utilities;

import com.jamespfluger.devicefinder.models.Device;

import java.util.Dictionary;
import java.util.Hashtable;

public class StateManager {
    // Dictionary<DeviceId, DeivceShallowCopy>
    private final Dictionary<String, Device> cachedDevices = new Hashtable<>();

    public void addCachedDevice(Device originalDevice) {
        Device clonedDevice = new Device(originalDevice);
        cachedDevices.put(clonedDevice.getDeviceId(), clonedDevice);
    }

    public boolean hasDeviceChanged(Device device) {
        Device cachedDevice = cachedDevices.get(device.getDeviceId());

        return !cachedDevice.getDeviceName().equals(device.getDeviceName()) ||
                cachedDevice.getUseFlashlight() != device.getUseFlashlight() ||
                cachedDevice.getUseVibrate() != device.getUseVibrate() ||
                cachedDevice.getUseOnWifiOnly() != device.getUseOnWifiOnly() ||
                !cachedDevice.getWifiSsid().equals(device.getWifiSsid()) ||
                cachedDevice.getVolumeOverrideValue() != device.getVolumeOverrideValue();
    }
}
