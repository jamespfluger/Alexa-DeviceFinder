package com.jamespfluger.devicefinder.settings;

import android.content.Context;
import android.content.SharedPreferences;

import com.jamespfluger.devicefinder.notifications.FirebaseService;

import java.util.HashMap;
import java.util.Map;

public class PreferencesManager {
    private static final String SETTINGS_USE_FLASHLIGHT = "SETTINGS_USE_FLASHLIGHT";
    private static final String SETTINGS_USE_VIBRATE = "SETTINGS_USE_VIBRATE";
    private static final String SETTINGS_USE_ON_WIFI_ONLY = "SETTINGS_USE_ON_WIFI_ONLY";
    private static final String SETTINGS_USE_VOLUME_OVERRIDE = "SETTINGS_USE_VOLUME_OVERRIDE";
    private static final String SETTINGS_CONFIGURED_WIFI_SSID = "SETTINGS_CONFIGURED_WIFI_SSID";
    private static final String SETTINGS_VOLUME_OVERRIDE_VALUE = "SETTINGS_VOLUME_OVERRIDE_VALUE";
    private static final String CONFIG_FIREBASE_TOKEN = "PREFERENCE_FIREBASE_TOKEN";
    private static final String CONFIG_LOGIN_USER_ID = "PREFERENCE_LOGIN_USER_ID";
    private static final String CONFIG_ALEXA_USER_ID = "PREFERENCE_ALEXA_USER_ID";
    private static final String CONFIG_DEVICE_NAME = "PREFERENCE_DEVICE_NAME";
    private static final Map<SettingType, String> settingTypes = new HashMap<SettingType, String>() {{
        put(SettingType.UseFlashlight, SETTINGS_USE_FLASHLIGHT);
        put(SettingType.UseVibrate, SETTINGS_USE_VIBRATE);
        put(SettingType.UseOnWifiOnly, SETTINGS_USE_ON_WIFI_ONLY);
        put(SettingType.UseVolumeOverride, SETTINGS_USE_VOLUME_OVERRIDE);
        put(SettingType.ConfiguredWifiSsid, SETTINGS_CONFIGURED_WIFI_SSID);
        put(SettingType.VolumeOverrideValue, SETTINGS_VOLUME_OVERRIDE_VALUE);
    }};
    private static final Map<ConfigType, String> configTypes = new HashMap<ConfigType, String>() {{
        put(ConfigType.FirebaseToken, CONFIG_FIREBASE_TOKEN);
        put(ConfigType.LoginUserId, CONFIG_LOGIN_USER_ID);
        put(ConfigType.AlexaUserId, CONFIG_ALEXA_USER_ID);
        put(ConfigType.DeviceName, CONFIG_DEVICE_NAME);
    }};
    private static SharedPreferences preferences = null;

    public static void setInstance(Context context) {
        preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getSetting(SettingType settingType) {
        String settingKey = settingTypes.get(settingType);

        switch (settingType) {
            case UseFlashlight:
            case UseVibrate:
            case UseOnWifiOnly:
            case UseVolumeOverride:
                return (T) Boolean.valueOf(preferences.getBoolean(settingKey, false));
            case ConfiguredWifiSsid:
                return (T) preferences.getString(settingKey, null);
            case VolumeOverrideValue:
                return (T) Integer.valueOf(preferences.getInt(settingKey, 0));
            default:
                return null;
        }
    }

    public static void setSetting(SettingType settingType, boolean settingValue) {
        String settingKey = settingTypes.get(settingType);
        preferences.edit().putBoolean(settingKey, settingValue).apply();
    }

    public static <T> void setSetting(SettingType settingType, String settingValue) {
        String settingKey = settingTypes.get(settingType);
        preferences.edit().putString(settingKey, settingValue).apply();
    }

    public static void setSetting(SettingType settingType, int settingValue) {
        String settingKey = settingTypes.get(settingType);
        preferences.edit().putInt(settingKey, settingValue).apply();
    }

    public static String getConfig(ConfigType settingType) {
        String settingKey = configTypes.get(settingType);
        return preferences.getString(settingKey, null);
    }

    public static void setConfig(ConfigType settingType, String settingValue) {
        String settingKey = configTypes.get(settingType);
        preferences.edit().putString(settingKey, settingValue).apply();
    }

    public static void refreshFirebaseToken() {
        FirebaseService firebaseService = new FirebaseService();
        firebaseService.refreshToken();
    }

    public static String getFirebaseToken() {
        String firebaseToken = getConfig(ConfigType.FirebaseToken);

        if (firebaseToken != null) {
            return firebaseToken;
        } else {
            FirebaseService firebaseService = new FirebaseService();
            firebaseService.refreshToken();
        }

        return getConfig(ConfigType.FirebaseToken);
    }
}
