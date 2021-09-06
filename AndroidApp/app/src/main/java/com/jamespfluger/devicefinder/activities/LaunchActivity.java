package com.jamespfluger.devicefinder.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.Listener;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.amazon.identity.auth.device.api.authorization.AuthorizeResult;
import com.amazon.identity.auth.device.api.authorization.ProfileScope;
import com.amazon.identity.auth.device.api.authorization.Scope;
import com.jamespfluger.devicefinder.R;
import com.jamespfluger.devicefinder.api.ApiService;
import com.jamespfluger.devicefinder.models.Device;
import com.jamespfluger.devicefinder.notifications.NotificationForge;
import com.jamespfluger.devicefinder.settings.ConfigManager;
import com.jamespfluger.devicefinder.settings.SettingsManager;
import com.jamespfluger.devicefinder.utilities.AmazonLoginHelper;
import com.jamespfluger.devicefinder.utilities.DeviceCache;
import com.jamespfluger.devicefinder.utilities.DeviceManager;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsManager.setInstance(getApplicationContext());
        ConfigManager.setInstance(getApplicationContext());
        NotificationForge.initializeNotificationChannel(getApplicationContext());

        ConfigManager.refreshFirebaseToken();

        setContentView(R.layout.activity_launch);
        selectActivityToLaunch();
    }

    private void selectActivityToLaunch() {
        Scope[] scopes = {ProfileScope.userId()};

        /* Require the following to be true to login:
         * 1. All ConfigManager fields to be fully set
         * 2. The Amazon login to still be valid
         * 3. The returned devices
         */
        if (ConfigManager.hasSetupFinished()) {
            AuthorizationManager.getToken(this, scopes, new Listener<AuthorizeResult, AuthError>() {
                @Override
                public void onSuccess(AuthorizeResult result) {
                    if (result.getAccessToken() != null) {
                        AmazonLoginHelper.setUserId(getApplicationContext());

                        Call<ArrayList<Device>> getDeviesCall = ApiService.getAllDevices(ConfigManager.getAlexaUserId());
                        getDeviesCall.enqueue(new Callback<>() {
                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) {
                                ArrayList<Device> devices = (ArrayList<Device>) response.body();

                                for (Device device : devices) {
                                    if (device.getDeviceId().equals(ConfigManager.getDeviceId())) {
                                        switchToActivity(DevicesConfigActivity.class);
                                        return;
                                    }
                                }

                                ConfigManager.reset();
                                switchToActivity(LoginActivity.class);
                            }

                            @Override
                            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                                ConfigManager.reset();
                                switchToActivity(LoginActivity.class);
                            }
                        });

                    } else {
                        ConfigManager.reset();
                        switchToActivity(LoginActivity.class);
                    }
                }

                @Override
                public void onError(AuthError ae) {
                    ConfigManager.reset();
                    switchToActivity(LoginActivity.class);
                }
            });
        } else {
            ConfigManager.reset();
            switchToActivity(LoginActivity.class);
        }

    }

    private void switchToActivity(Class<?> newActivity) {
        Intent newIntent = new Intent(this, newActivity);
        startActivity(newIntent);
        finish();
    }
}
