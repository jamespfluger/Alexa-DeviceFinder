package com.jamespfluger.devicefinder.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.SerializedName;
import com.jamespfluger.devicefinder.BR;
import com.jamespfluger.devicefinder.settings.SettingsManager;

public class DeviceSettings extends BaseObservable {
    @SerializedName("useFlashlight")
    private boolean useFlashlight;
    @SerializedName("useVibrate")
    private boolean useVibrate;
    @SerializedName("useOnWifiOnly")
    private boolean useOnWifiOnly;
    @SerializedName("useVolumeOverride")
    private boolean useVolumeOverride;
    @SerializedName("wifiSsid")
    private String wifiSsid;
    @SerializedName("volumeOverrideValue")
    private int volumeOverrideValue;

    public DeviceSettings() {
    }

    @Bindable
    public boolean getUseFlashlight() {
        return useFlashlight;
    }

    public void setUseFlashlight(boolean useFlashlight) {
        this.useFlashlight = useFlashlight;
        SettingsManager.setUseFlashlightSetting(useFlashlight);
        notifyPropertyChanged(BR.useFlashlight);
    }

    @Bindable
    public boolean getUseVibrate() {
        return useVibrate;
    }

    public void setUseVibrate(boolean useVibrate) {
        this.useVibrate = useVibrate;
        SettingsManager.setUseVibrateSetting(useVibrate);
        notifyPropertyChanged(BR.useVibrate);
    }

    @Bindable
    public boolean getUseOnWifiOnly() {
        return useOnWifiOnly;
    }

    public void setUseOnWifiOnly(boolean useOnWifiOnly) {
        this.useOnWifiOnly = useOnWifiOnly;
        SettingsManager.setUseOnWifiOnlySetting(useOnWifiOnly);
        notifyPropertyChanged(BR.useOnWifiOnly);
    }

    @Bindable
    public boolean getUseVolumeOverride() {
        return useVolumeOverride;
    }

    public void setUseVolumeOverride(boolean useVolumeOverride) {
        this.useVolumeOverride = useVolumeOverride;
        SettingsManager.setUseVolumeOverrideSetting(useVolumeOverride);
        notifyPropertyChanged(BR.useVolumeOverride);
    }

    @Bindable
    public String getWifiSsid() {
        return wifiSsid;
    }

    public void setWifiSsid(String wifiSsid) {
        this.wifiSsid = wifiSsid;
        SettingsManager.setWifiSsidSetting(wifiSsid);
        notifyPropertyChanged(BR.wifiSsid);
    }

    @Bindable
    public int getVolumeOverrideValue() {
        return volumeOverrideValue;
    }

    public void setVolumeOverrideValue(int volumeOverrideValue) {
        this.volumeOverrideValue = volumeOverrideValue;
        SettingsManager.setVolumeOverrideValueSetting(volumeOverrideValue);
        notifyPropertyChanged(BR.volumeOverrideValue);
    }
}
