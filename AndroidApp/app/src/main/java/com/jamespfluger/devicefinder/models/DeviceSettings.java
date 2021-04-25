package com.jamespfluger.devicefinder.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.SerializedName;
import com.jamespfluger.devicefinder.BR;
import com.jamespfluger.devicefinder.settings.PreferencesManager;
import com.jamespfluger.devicefinder.settings.SettingType;

public class DeviceSettings extends BaseObservable {
    @SerializedName("useFlashlight")
    private boolean useFlashlight;
    @SerializedName("useVibrate")
    private boolean useVibrate;
    @SerializedName("useOnWifiOnly")
    private boolean useOnWifiOnly;
    @SerializedName("useVolumeOverride")
    private boolean useVolumeOverride;
    @SerializedName("configuredWifiSsid")
    private String configuredWifiSsid;
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
        PreferencesManager.setSetting(SettingType.UseFlashlight, useFlashlight);
        notifyPropertyChanged(BR.useFlashlight);
    }

    @Bindable
    public boolean getUseVibrate() {
        return useVibrate;
    }

    public void setUseVibrate(boolean useVibrate) {
        this.useVibrate = useVibrate;
        PreferencesManager.setSetting(SettingType.UseVibrate, useVibrate);
        notifyPropertyChanged(BR.useVibrate);
    }

    @Bindable
    public boolean getUseOnWifiOnly() {
        return useOnWifiOnly;
    }

    public void setUseOnWifiOnly(boolean useOnWifiOnly) {
        this.useOnWifiOnly = useOnWifiOnly;
        PreferencesManager.setSetting(SettingType.UseOnWifiOnly, useOnWifiOnly);
        notifyPropertyChanged(BR.useOnWifiOnly);
    }

    @Bindable
    public boolean getUseVolumeOverride() {
        return useVolumeOverride;
    }

    public void setUseVolumeOverride(boolean useVolumeOverride) {
        this.useVolumeOverride = useVolumeOverride;
        PreferencesManager.setSetting(SettingType.UseVolumeOverride, useVolumeOverride);
        notifyPropertyChanged(BR.useVolumeOverride);
    }

    @Bindable
    public String getConfiguredWifiSsid() {
        return configuredWifiSsid;
    }

    public void setConfiguredWifiSsid(String configuredWifiSsid) {
        this.configuredWifiSsid = configuredWifiSsid;
        PreferencesManager.setSetting(SettingType.ConfiguredWifiSsid, configuredWifiSsid);
        notifyPropertyChanged(BR.configuredWifiSsid);
    }

    @Bindable
    public int getVolumeOverrideValue() {
        return volumeOverrideValue;
    }

    public void setVolumeOverrideValue(int volumeOverrideValue) {
        this.volumeOverrideValue = volumeOverrideValue;
        PreferencesManager.setSetting(SettingType.VolumeOverrideValue, volumeOverrideValue);
        notifyPropertyChanged(BR.volumeOverrideValue);
    }
}
