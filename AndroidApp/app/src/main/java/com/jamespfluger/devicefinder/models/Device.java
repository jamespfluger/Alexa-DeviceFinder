package com.jamespfluger.devicefinder.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.SerializedName;
import com.jamespfluger.devicefinder.BR;
import com.jamespfluger.devicefinder.settings.ConfigManager;
import com.jamespfluger.devicefinder.settings.SettingsManager;
import com.jamespfluger.devicefinder.utilities.DeviceManager;

import java.io.Serializable;

public class Device extends BaseObservable implements Serializable {
    // General device information for identification
    @SerializedName("alexaUserId")
    private String alexaUserId;
    @SerializedName("deviceId")
    private String deviceId;
    @SerializedName("loginUserId")
    private String loginUserId;
    @SerializedName("firebaseToken")
    private String firebaseToken;
    @SerializedName("deviceName")
    private String deviceName;

    // Settings
    @SerializedName("useFlashlight")
    private boolean useFlashlight;
    @SerializedName("useVibrate")
    private boolean useVibrate;
    @SerializedName("useOnWifiOnly")
    private boolean useOnWifiOnly;
    @SerializedName("wifiSsid")
    private String wifiSsid;
    @SerializedName("useVolumeOverride")
    private boolean useVolumeOverride;
    @SerializedName("volumeOverrideValue")
    private int volumeOverrideValue;

    public Device() {
        this.volumeOverrideValue = 100;
    }

    public String getAlexaUserId() {
        return alexaUserId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    @Bindable
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
        notifyPropertyChanged(BR.deviceName);
    }

    @Bindable
    public boolean getUseFlashlight() {
        return useFlashlight;
    }

    public void setUseFlashlight(boolean useFlashlight) {
        if (this.getDeviceId().equals(ConfigManager.getDeviceId())) {
            SettingsManager.setUseFlashlight(useFlashlight);
        }

        this.useFlashlight = useFlashlight;
        notifyPropertyChanged(BR.useFlashlight);
    }

    @Bindable
    public boolean getUseVibrate() {
        return useVibrate;
    }

    public void setUseVibrate(boolean useVibrate) {
        if (this.getDeviceId().equals(ConfigManager.getDeviceId())) {
            SettingsManager.setUseVibrate(useVibrate);
        }

        this.useVibrate = useVibrate;
        notifyPropertyChanged(BR.useVibrate);
    }

    @Bindable
    public boolean getUseOnWifiOnly() {
        return useOnWifiOnly;
    }

    public void setUseOnWifiOnly(boolean useOnWifiOnly) {
        if (this.getDeviceId().equals(ConfigManager.getDeviceId())) {
            SettingsManager.setUseOnWifiOnly(useOnWifiOnly);
        }

        this.useOnWifiOnly = useOnWifiOnly;
        notifyPropertyChanged(BR.useOnWifiOnly);
    }

    @Bindable
    public boolean getUseVolumeOverride() {
        return useVolumeOverride;
    }

    public void setUseVolumeOverride(boolean useVolumeOverride) {
        if (this.getDeviceId().equals(ConfigManager.getDeviceId())) {
            SettingsManager.setUseVolumeOverride(useVolumeOverride);
        }

        this.useVolumeOverride = useVolumeOverride;
        notifyPropertyChanged(BR.useVolumeOverride);
    }

    @Bindable
    public String getWifiSsid() {
        return wifiSsid;
    }

    public void setWifiSsid(String wifiSsid) {
        this.wifiSsid = wifiSsid;
        if (this.getDeviceId().equals(ConfigManager.getDeviceId())) {
            SettingsManager.setWifiSsid(wifiSsid);
        }
        notifyPropertyChanged(BR.wifiSsid);
    }

    @Bindable
    public int getVolumeOverrideValue() {
        return volumeOverrideValue;
    }

    public void setVolumeOverrideValue(int volumeOverrideValue) {
        this.volumeOverrideValue = volumeOverrideValue;
        if (this.getDeviceId().equals(ConfigManager.getDeviceId())) {
            SettingsManager.setVolumeOverrideValue(volumeOverrideValue);
        }
        notifyPropertyChanged(BR.volumeOverrideValue);
    }
}
