package com.jamespfluger.alexadevicefinder.models;

import com.google.gson.annotations.SerializedName;

public class AuthUserDevice {
    @SerializedName("AmazonUserID")
    private String userId;
    @SerializedName("DeviceID")
    private String deviceId;
    @SerializedName("DeviceName")
    private String deviceName;
    @SerializedName("DeviceOS")
    private String deviceOs = "Android";
    @SerializedName("OneTimePassword")
    private String otp;

    public AuthUserDevice() {
    }

    public AuthUserDevice(String userId, String deviceId) {
        this.userId = userId;
        this.deviceId = deviceId;
    }

    public AuthUserDevice(String userId, String deviceId, String otp) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.otp = otp;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceOs() {
        return this.deviceOs;
    }

    public void setDeviceOs(String deviceOs) {
        this.deviceOs = deviceOs;
    }

    public String getOtp() {
        return this.otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
