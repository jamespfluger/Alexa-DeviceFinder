package com.jamespfluger.devicefinder.models;

import com.google.gson.annotations.SerializedName;

public class Device {
    @SerializedName("alexaUserId")
    private String alexaUserId;
    @SerializedName("deviceId")
    private String deviceId;
    @SerializedName("amazonUserId")
    private String amazonUserId;
    @SerializedName("firebaseToken")
    private String firebaseToken;
    @SerializedName("deviceName")
    private String deviceName;
    @SerializedName("deviceSettings")
    private DeviceSettings deviceSettings;

    public Device() {
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

    public String getFirebaseToken() {
        return amazonUserId;
    }

    public void setFirebaseToken(String amazonUserId) {
        this.amazonUserId = amazonUserId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public DeviceSettings getDeviceSettings() {
        return deviceSettings;
    }

    public void setDeviceSettings(DeviceSettings deviceSettings) {
        this.deviceSettings = deviceSettings;
    }
}
