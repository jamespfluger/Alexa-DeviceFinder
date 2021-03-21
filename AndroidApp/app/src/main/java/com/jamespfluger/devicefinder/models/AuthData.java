package com.jamespfluger.devicefinder.models;

import com.google.gson.annotations.SerializedName;

public class AuthData {
    @SerializedName("OneTimePasscode")
    private String otp;
    @SerializedName("FirebaseToken")
    private String firebaseToken;
    @SerializedName("LoginUserId")
    private String loginUserId;
    @SerializedName("DeviceName")
    private String deviceName;

    public AuthData() {
    }

    public String getOtp() {
        return this.otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getFirebaseToken() {
        return this.firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public String getLoginUserId() {
        return this.loginUserId;
    }

    public void setLoginUserId(String loginUserId) {
        this.loginUserId = loginUserId;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
