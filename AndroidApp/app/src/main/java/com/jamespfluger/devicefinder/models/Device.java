package com.jamespfluger.devicefinder.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.SerializedName;
import com.jamespfluger.devicefinder.BR;

public class Device extends BaseObservable {
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

    @Bindable
    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
        notifyPropertyChanged(BR.deviceName);
    }

    @Bindable
    public DeviceSettings getDeviceSettings() {
        return deviceSettings;
    }

    public void setDeviceSettings(DeviceSettings deviceSettings) {
        this.deviceSettings = deviceSettings;
        notifyPropertyChanged(BR.deviceSettings);
    }
}
