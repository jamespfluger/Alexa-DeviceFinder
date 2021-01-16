package com.jamespfluger.alexadevicefinder.models;

import com.google.gson.annotations.SerializedName;

public class DeviceSettings {

    @SerializedName("alexaUserId")
    private String alexaUserId;
    @SerializedName("deviceId")
    private String deviceId;
    @SerializedName("deviceName")
    private String deviceName;
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

    public DeviceSettings(String alexaUserId, String deviceId) {
        this.alexaUserId = alexaUserId;
        this.deviceId = deviceId;
    }

    public String getAlexaUserId() {
        return alexaUserId;
    }

    public void setAlexaUserId(String alexaUserId) {
        this.alexaUserId = alexaUserId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public boolean getUseFlashlight() {
        return useFlashlight;
    }

    public void setUseFlashlight(boolean useFlashlight) {
        this.useFlashlight = useFlashlight;
    }

    public boolean getUseVibrate() {
        return useVibrate;
    }

    public void setUseVibrate(boolean useVibrate) {
        this.useVibrate = useVibrate;
    }

    public boolean getShouldLimitToWifi() {
        return shouldLimitToWifi;
    }

    public void setShouldLimitToWifi(boolean shouldLimitToWifi) {
        this.shouldLimitToWifi = shouldLimitToWifi;
    }

    public String getConfiguredWifiSsid() {
        return configuredWifiSsid;
    }

    public void setConfiguredWifiSsid(String configuredWifiSsid) {
        this.configuredWifiSsid = configuredWifiSsid;
    }

    public boolean getUseVolumeOverride() {
        return useVolumeOverride;
    }

    public void setUseVolumeOverride(boolean useVolumeOverride) {
        this.useVolumeOverride = useVolumeOverride;
    }

    public int getOverriddenVolumeValue() {
        return overriddenVolumeValue;
    }

    public void setOverriddenVolumeValue(int overriddenVolumeValue) {
        this.overriddenVolumeValue = overriddenVolumeValue;
    }

}
