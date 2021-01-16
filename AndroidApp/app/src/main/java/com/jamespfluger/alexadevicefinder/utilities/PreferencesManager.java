package com.jamespfluger.alexadevicefinder.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.jamespfluger.alexadevicefinder.notifications.FirebaseService;

public class PreferencesManager {
    private final String PREFERENCES_NAME = "com.jamespfluger.alexadevicefinder.SHARED_PREFERENCES";
    private final String PREFERENCE_NAME_FIREBASE = "firebasetoken";
    private final String PREFERENCE_NAME_AMAZON_USER_ID = "amazonuserid";
    private final String PREFERENCE_NAME_ALEXA_USER_ID = "alexauserid";
    private final String PREFERENCE_NAME_DEVICE_NAME = "devicename";
    private final SharedPreferences preferences;
    private final Context context;

    public PreferencesManager(Context context) {
        this.preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        this.context = context;
    }

    public String getDeviceId() {
        String firebaseToken = preferences.getString(PREFERENCE_NAME_FIREBASE, null);

        if (firebaseToken != null) {
            return firebaseToken;
        } else {
            FirebaseService firebaseService = new FirebaseService();
            firebaseService.refreshToken();
        }

        return preferences.getString(PREFERENCE_NAME_FIREBASE, null);
    }

    public void setDeviceId(String deviceId) {
        preferences.edit().putString(PREFERENCE_NAME_FIREBASE, deviceId).apply();
    }

    public void refreshDeviceId() {
        FirebaseService firebaseService = new FirebaseService(context);
        firebaseService.refreshToken();
    }

    public String getAmazonUserId() {
        return preferences.getString(PREFERENCE_NAME_AMAZON_USER_ID, null);
    }

    public void setAmazonUserId(String userId) {
        preferences.edit().putString(PREFERENCE_NAME_AMAZON_USER_ID, userId).apply();
    }

    public String getDeviceName() {
        return preferences.getString(PREFERENCE_NAME_DEVICE_NAME, null);
    }

    public void setDeviceName(String deviceName) {
        preferences.edit().putString(PREFERENCE_NAME_DEVICE_NAME, deviceName).apply();
    }

    public String getUserId() {
        return preferences.getString(PREFERENCE_NAME_ALEXA_USER_ID, null);
    }

    public void setUserId(String userId) {
        preferences.edit().putString(PREFERENCE_NAME_ALEXA_USER_ID, userId).apply();
    }
}
