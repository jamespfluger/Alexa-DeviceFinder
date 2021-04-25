package com.jamespfluger.devicefinder.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.jamespfluger.devicefinder.notifications.FirebaseService;

public class UserManager {
    private final String PREFERENCE_NAME_FIREBASE = "firebasetoken";
    private final String PREFERENCE_NAME_AMAZON_USER_ID = "amazonuserid";
    private final String PREFERENCE_NAME_ALEXA_USER_ID = "alexauserid";
    private final String PREFERENCE_NAME_DEVICE_NAME = "devicename";

    private final SharedPreferences preferences;
    private final Context context;

    public UserManager(Context context) {
        this.preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        this.context = context;
    }

    public void refreshFirebaseToken() {
        FirebaseService firebaseService = new FirebaseService(context);
        firebaseService.refreshToken();
    }

    public String getFirebaseToken() {
        String firebaseToken = getPreference(PREFERENCE_NAME_FIREBASE);

        if (firebaseToken != null) {
            return firebaseToken;
        } else {
            FirebaseService firebaseService = new FirebaseService();
            firebaseService.refreshToken();
        }

        return getPreference(PREFERENCE_NAME_FIREBASE);
    }

    public void setFirebaseToken(String deviceId) {
        preferences.edit().putString(PREFERENCE_NAME_FIREBASE, deviceId).apply();
    }

    public String getLoginUserId() {
        return getPreference(PREFERENCE_NAME_AMAZON_USER_ID);
    }

    public void setLoginUserId(String userId) {
        setPreference(PREFERENCE_NAME_AMAZON_USER_ID, userId);
    }

    public String getDeviceName() {
        return getPreference(PREFERENCE_NAME_DEVICE_NAME);
    }

    public void setDeviceName(String deviceName) {
        setPreference(PREFERENCE_NAME_DEVICE_NAME, deviceName);
    }

    public String getAlexaUserId() {
        return getPreference(PREFERENCE_NAME_ALEXA_USER_ID);
    }

    public void setAlexaUserId(String alexaUserId) {
        setPreference(PREFERENCE_NAME_ALEXA_USER_ID, alexaUserId);
    }
    
    private void setPreference(String preferenceName, String preferenceValue){
        preferences.edit().putString(preferenceName, preferenceValue).apply();
    }
    
    private String getPreference(String preferenceName){
        return preferences.getString(preferenceName, null);
    }
}
