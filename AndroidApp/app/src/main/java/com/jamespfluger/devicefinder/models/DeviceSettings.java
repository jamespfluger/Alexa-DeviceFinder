package com.jamespfluger.devicefinder.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.SerializedName;
import com.jamespfluger.devicefinder.BR;

public class DeviceSettings extends BaseObservable {
    @SerializedName("useFlashlight")
    private boolean useFlashlight;
    @SerializedName("useVibrate")
    private boolean useVibrate;
    @SerializedName("shouldLimitToWifi")
    private boolean shouldLimitToWifi;
    @SerializedName("configuredWifiSsid")
    private String configuredWifiSsid;
    @SerializedName("useVolumeOverride")
    private boolean useVolumeOverride;
    @SerializedName("overriddenVolumeValue")
    private int overriddenVolumeValue;

    public DeviceSettings() {
    }

    @Bindable
    public boolean getUseFlashlight() {
        return useFlashlight;
    }

    public void setUseFlashlight(boolean useFlashlight) {
        this.useFlashlight = useFlashlight;
        notifyPropertyChanged(BR.useFlashlight);
    }

    @Bindable
    public boolean getUseVibrate() {
        return useVibrate;
    }

    public void setUseVibrate(boolean useVibrate) {
        this.useVibrate = useVibrate;
        notifyPropertyChanged(BR.useVibrate);
    }

    @Bindable
    public boolean getShouldLimitToWifi() {
        return shouldLimitToWifi;
    }

    public void setShouldLimitToWifi(boolean shouldLimitToWifi) {
        this.shouldLimitToWifi = shouldLimitToWifi;
        notifyPropertyChanged(BR.shouldLimitToWifi);
    }

    @Bindable
    public String getConfiguredWifiSsid() {
        return configuredWifiSsid;
    }

    public void setConfiguredWifiSsid(String configuredWifiSsid) {
        this.configuredWifiSsid = configuredWifiSsid;
        notifyPropertyChanged(BR.configuredWifiSsid);
    }

    @Bindable
    public boolean getUseVolumeOverride() {
        return useVolumeOverride;
    }

    public void setUseVolumeOverride(boolean useVolumeOverride) {
        this.useVolumeOverride = useVolumeOverride;
        notifyPropertyChanged(BR.useVolumeOverride);
    }

    @Bindable
    public int getOverriddenVolumeValue() {
        return overriddenVolumeValue;
    }

    public void setOverriddenVolumeValue(int overriddenVolumeValue) {
        this.overriddenVolumeValue = overriddenVolumeValue;
        notifyPropertyChanged(BR.overriddenVolumeValue);
    }
}
