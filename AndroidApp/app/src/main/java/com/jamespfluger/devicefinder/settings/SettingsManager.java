package com.jamespfluger.devicefinder.settings;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsManager {
    private static final String SETTINGS_USE_FLASHLIGHT = "SETTINGS_USE_FLASHLIGHT";
    private static final String SETTINGS_USE_VIBRATE = "SETTINGS_USE_VIBRATE";
    private static final String SETTINGS_USE_ON_WIFI_ONLY = "SETTINGS_USE_ON_WIFI_ONLY";
    private static final String SETTINGS_USE_VOLUME_OVERRIDE = "SETTINGS_USE_VOLUME_OVERRIDE";
    private static final String SETTINGS_CONFIGURED_WIFI_SSID = "SETTINGS_CONFIGURED_WIFI_SSID";
    private static final String SETTINGS_VOLUME_OVERRIDE_VALUE = "SETTINGS_VOLUME_OVERRIDE_VALUE";

    private static SharedPreferences preferences = null;

    public static void setInstance(Context context) {
        preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    public static boolean getUseFlashlightSetting() {
        return preferences.getBoolean(SETTINGS_USE_FLASHLIGHT, false);
    }

    public static void setUseFlashlightSetting(boolean settingValue) {
        preferences.edit().putBoolean(SETTINGS_USE_FLASHLIGHT, settingValue).apply();
    }

    public static boolean getUseVibrateSetting() {
        return preferences.getBoolean(SETTINGS_USE_VIBRATE, false);
    }

    public static void setUseVibrateSetting(boolean settingValue) {
        preferences.edit().putBoolean(SETTINGS_USE_VIBRATE, settingValue).apply();
    }

    public static boolean getUseOnWifiOnlySetting() {
        return preferences.getBoolean(SETTINGS_USE_ON_WIFI_ONLY, false);
    }

    public static void setUseOnWifiOnlySetting(boolean settingValue) {
        preferences.edit().putBoolean(SETTINGS_USE_ON_WIFI_ONLY, settingValue).apply();
    }

    public static String getWifiSsidSetting() {
        return preferences.getString(SETTINGS_CONFIGURED_WIFI_SSID, null);
    }

    public static void setWifiSsidSetting(String settingValue) {
        preferences.edit().putString(SETTINGS_CONFIGURED_WIFI_SSID, settingValue).apply();
    }

    public static boolean getUseVolumeOverrideSetting() {
        return preferences.getBoolean(SETTINGS_USE_VOLUME_OVERRIDE, false);
    }

    public static void setUseVolumeOverrideSetting(boolean settingValue) {
        preferences.edit().putBoolean(SETTINGS_USE_VOLUME_OVERRIDE, settingValue).apply();
    }

    public static int getVolumeOverrideValueSetting() {
        return preferences.getInt(SETTINGS_VOLUME_OVERRIDE_VALUE, 0);
    }

    public static void setVolumeOverrideValueSetting(int settingValue) {
        preferences.edit().putInt(SETTINGS_VOLUME_OVERRIDE_VALUE, settingValue).apply();
    }
}
