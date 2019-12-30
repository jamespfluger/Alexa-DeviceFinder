package com.jamespfluger.alexadevicefinder.auth.retrofit;

public class AuthUserDevice {
    private String userId;
    private String deviceId;

    public AuthUserDevice(String userId, String deviceId){
        this.userId = userId;
        this.deviceId = deviceId;
    }

    public String getUserId(){
        return this.userId;
    }
    public void setUserId(String userId){
        this.userId = userId;
    }

    public String getDeviceId(){
        return this.deviceId;
    }
    public void setDeviceId(String deviceId){
        this.deviceId = deviceId;
    }

    @Override
    public String toString(){ return userId + ":" + deviceId; }
}
