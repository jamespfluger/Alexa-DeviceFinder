package com.jamespfluger.devicefinder.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.jamespfluger.devicefinder.notifications.FirebaseService;

public class ConfigManager {
    private static final String CONFIG_FIREBASE_TOKEN = "PREFERENCE_FIREBASE_TOKEN";
    private static final String CONFIG_LOGIN_USER_ID = "PREFERENCE_LOGIN_USER_ID";
    private static final String CONFIG_ALEXA_USER_ID = "PREFERENCE_ALEXA_USER_ID";
    private static final String CONFIG_DEVICE_NAME = "PREFERENCE_DEVICE_NAME";

    private static SharedPreferences preferences = null;

    public static void setInstance(Context context) {
        preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static String getFirebaseTokenConfig() {
        String firebaseToken = preferences.getString(CONFIG_FIREBASE_TOKEN, null);

        if (firebaseToken != null) {
            return firebaseToken;
        } else {
            FirebaseService firebaseService = new FirebaseService();
            firebaseService.refreshToken();
        }

        return preferences.getString(CONFIG_FIREBASE_TOKEN, null);
    }

    public static void setFirebaseTokenConfig(String settingValue) {
        preferences.edit().putString(CONFIG_FIREBASE_TOKEN, settingValue).apply();
    }

    public static String getLoginUserIdConfig() {
        return preferences.getString(CONFIG_LOGIN_USER_ID, null);
    }

    public static void setLoginUserIdConfig(String settingValue) {
        preferences.edit().putString(CONFIG_LOGIN_USER_ID, settingValue).apply();
    }

    public static String getAlexaUserIdConfig() {
        return preferences.getString(CONFIG_ALEXA_USER_ID, null);
    }

    public static void setAlexaUserIdConfig(String settingValue) {
        preferences.edit().putString(CONFIG_ALEXA_USER_ID, settingValue).apply();
    }

    public static String getDeviceNameConfig() {
        return preferences.getString(CONFIG_DEVICE_NAME, null);
    }

    public static void setDeviceNameConfig(String settingValue) {
        preferences.edit().putString(CONFIG_DEVICE_NAME, settingValue).apply();
    }

    public static void refreshFirebaseToken() {
        FirebaseService firebaseService = new FirebaseService();
        firebaseService.refreshToken();
    }
}
