package com.jamespfluger.devicefinder.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.jamespfluger.devicefinder.notifications.FirebaseService;

public class ConfigManager {
    private static final String CONFIG_FIREBASE_TOKEN = "CONFIG_FIREBASE_TOKEN";
    private static final String CONFIG_LOGIN_USER_ID = "CONFIG_LOGIN_USER_ID";
    private static final String CONFIG_ALEXA_USER_ID = "CONFIG_ALEXA_USER_ID";
    private static final String CONFIG_DEVICE_ID = "CONFIG_DEVICE_ID";

    private static SharedPreferences preferences = null;

    public static void setInstance(Context context) {
        preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static String getFirebaseToken() {
        String firebaseToken = preferences.getString(CONFIG_FIREBASE_TOKEN, null);

        if (firebaseToken != null) {
            return firebaseToken;
        } else {
            FirebaseService firebaseService = new FirebaseService();
            firebaseService.refreshToken();
        }

        return preferences.getString(CONFIG_FIREBASE_TOKEN, null);
    }

    public static void setFirebaseToken(String settingValue) {
        preferences.edit().putString(CONFIG_FIREBASE_TOKEN, settingValue).apply();
    }

    public static String getLoginUserId() {
        return preferences.getString(CONFIG_LOGIN_USER_ID, null);
    }

    public static void setLoginUserId(String settingValue) {
        preferences.edit().putString(CONFIG_LOGIN_USER_ID, settingValue).apply();
    }

    public static String getAlexaUserId() {
        return preferences.getString(CONFIG_ALEXA_USER_ID, null);
    }

    public static void setAlexaUserId(String settingValue) {
        preferences.edit().putString(CONFIG_ALEXA_USER_ID, settingValue).apply();
    }

    public static String getDeviceId() {
        return preferences.getString(CONFIG_DEVICE_ID, null);
    }

    public static void setDeviceId(String settingValue) {
        preferences.edit().putString(CONFIG_DEVICE_ID, settingValue).apply();
    }

    public static void refreshFirebaseToken() {
        FirebaseService firebaseService = new FirebaseService();
        firebaseService.refreshToken();
    }

    public static void reset() {
        preferences.edit().clear().apply();
        refreshFirebaseToken();
    }

    public static boolean hasSetupFinished() {
        return getAlexaUserId() != null &&
                getLoginUserId() != null &&
                getDeviceId() != null &&
                getAlexaUserId() != null;
    }
}
