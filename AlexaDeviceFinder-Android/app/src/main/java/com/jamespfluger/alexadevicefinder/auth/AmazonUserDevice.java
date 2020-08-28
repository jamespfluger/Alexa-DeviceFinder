package com.jamespfluger.alexadevicefinder.auth;

public class AmazonUserDevice {
    private String userId;
    private String deviceId;
    private String deviceOs = "Android";

    public AmazonUserDevice(String userId, String deviceId) {
        this.userId = userId;
        this.deviceId = deviceId;
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

    public String getDeviceOs() { return this.deviceOs; }

    public void setDeviceOs(String deviceOs) { this.deviceOs = deviceOs; }

    @Override
    public String toString() {
        return userId + ":" + deviceId;
    }
}
