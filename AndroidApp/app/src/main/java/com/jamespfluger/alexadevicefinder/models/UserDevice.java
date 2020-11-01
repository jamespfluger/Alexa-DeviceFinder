package com.jamespfluger.alexadevicefinder.models;

import com.google.gson.annotations.SerializedName;

public class UserDevice {

    @SerializedName("alexaUserId")
    private String alexaUserId;
    @SerializedName("deviceId")
    private String deviceId;
    @SerializedName("amazonUserId")
    private String amazonUserId;
    @SerializedName("alexaDeviceId")
    private String alexaDeviceId;
    @SerializedName("deviceName")
    private String deviceName;
    @SerializedName("deviceOs")
    private String deviceOs;
    @SerializedName("modifiedDate")
    private String modifiedDate;
    @SerializedName("deviceSettings")
    private DeviceSettings deviceSettings;

    public UserDevice() {
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

    public String getAmazonUserId() {
        return amazonUserId;
    }

    public void setAmazonUserId(String amazonUserId) {
        this.amazonUserId = amazonUserId;
    }

    public String getAlexaDeviceId() {
        return alexaDeviceId;
    }

    public void setAlexaDeviceId(String alexaDeviceId) {
        this.alexaDeviceId = alexaDeviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceOs() {
        return deviceOs;
    }

    public void setDeviceOs(String deviceOs) {
        this.deviceOs = deviceOs;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public DeviceSettings getDeviceSettings() {
        return deviceSettings;
    }

    public void setDeviceSettings(DeviceSettings deviceSettings) {
        this.deviceSettings = deviceSettings;
    }
}