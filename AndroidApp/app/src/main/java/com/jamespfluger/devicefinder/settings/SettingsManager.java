package com.jamespfluger.devicefinder.settings;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {
    private static final String SETTINGS_DEVICE_NAME = "SETTINGS_DEVICE_NAME";
    private static final String SETTINGS_USE_FLASHLIGHT = "SETTINGS_USE_FLASHLIGHT";
    private static final String SETTINGS_USE_VIBRATE = "SETTINGS_USE_VIBRATE";
    private static final String SETTINGS_USE_ON_WIFI_ONLY = "SETTINGS_USE_ON_WIFI_ONLY";
    private static final String SETTINGS_USE_VOLUME_OVERRIDE = "SETTINGS_USE_VOLUME_OVERRIDE";
    private static final String SETTINGS_CONFIGURED_WIFI_SSID = "SETTINGS_CONFIGURED_WIFI_SSID";
    private static final String SETTINGS_VOLUME_OVERRIDE_VALUE = "SETTINGS_VOLUME_OVERRIDE_VALUE";
    private static final String SETTINGS_CRASHLYTICS = "SETTINGS_CRASHLYTICS";

    private static SharedPreferences preferences = null;

    public static void setInstance(Context context) {
        if (preferences == null) {
            preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        }
    }

    public static String getDeviceName() {
        return preferences.getString(SETTINGS_DEVICE_NAME, null);
    }

    public static void setDeviceName(String settingValue) {
        preferences.edit().putString(SETTINGS_DEVICE_NAME, settingValue).apply();
    }

    public static boolean getUseFlashlight() {
        return preferences.getBoolean(SETTINGS_USE_FLASHLIGHT, false);
    }

    public static void setUseFlashlight(boolean settingValue) {
        preferences.edit().putBoolean(SETTINGS_USE_FLASHLIGHT, settingValue).apply();
    }

    public static boolean getUseVibrate() {
        return preferences.getBoolean(SETTINGS_USE_VIBRATE, false);
    }

    public static void setUseVibrate(boolean settingValue) {
        preferences.edit().putBoolean(SETTINGS_USE_VIBRATE, settingValue).apply();
    }

    public static boolean getUseOnWifiOnly() {
        return preferences.getBoolean(SETTINGS_USE_ON_WIFI_ONLY, false);
    }

    public static void setUseOnWifiOnly(boolean settingValue) {
        preferences.edit().putBoolean(SETTINGS_USE_ON_WIFI_ONLY, settingValue).apply();
    }

    public static String getWifiSsid() {
        return preferences.getString(SETTINGS_CONFIGURED_WIFI_SSID, null);
    }

    public static void setWifiSsid(String settingValue) {
        preferences.edit().putString(SETTINGS_CONFIGURED_WIFI_SSID, settingValue).apply();
    }

    public static int getVolumeOverrideValue() {
        return preferences.getInt(SETTINGS_VOLUME_OVERRIDE_VALUE, 0);
    }

    public static void setVolumeOverrideValue(int settingValue) {
        preferences.edit().putInt(SETTINGS_VOLUME_OVERRIDE_VALUE, settingValue).apply();
    }

    public static boolean getCrashlyticsEnabled() {
        return preferences.getBoolean(SETTINGS_CRASHLYTICS, true);
    }

    public static void setCrashlyticsEnabled(boolean settingValue) {
        preferences.edit().putBoolean(SETTINGS_CRASHLYTICS, settingValue).apply();
    }

    public static void reset() {
        preferences.edit().clear().apply();
    }
}
