package com.jamespfluger.alexadevicefinder.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.Listener;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.amazon.identity.auth.device.api.authorization.AuthorizeResult;
import com.amazon.identity.auth.device.api.authorization.ProfileScope;
import com.amazon.identity.auth.device.api.authorization.Scope;
import com.jamespfluger.alexadevicefinder.R;
import com.jamespfluger.alexadevicefinder.utilities.AmazonLoginHelper;
import com.jamespfluger.alexadevicefinder.utilities.PermissionsRequester;
import com.jamespfluger.alexadevicefinder.utilities.PreferencesManager;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferencesManager preferencesManager = new PreferencesManager(getApplicationContext());
        preferencesManager.refreshDeviceId();

        setContentView(R.layout.activity_launch);
        selectActivityToLaunch();
    }

    private void selectActivityToLaunch() {
        Scope[] scopes = {ProfileScope.userId()};

        AuthorizationManager.getToken(this, scopes, new Listener<AuthorizeResult, AuthError>() {
            Intent intentToLaunch;

            @Override
            public void onSuccess(AuthorizeResult result) {
                PermissionsRequester permissionsRequester = new PermissionsRequester();
                permissionsRequester.requestPermissions(LaunchActivity.this);

                Class<?> activityToLaunch;

                // TODO: before release, update activities swapped to
                if (result.getAccessToken() != null) {
                    activityToLaunch = DevicesConfigActivity.class;
                    AmazonLoginHelper.setUserId(getApplicationContext());
                } else {
                    activityToLaunch = LoginActivity.class;
                }

                switchToActivity(activityToLaunch);
            }

            @Override
            public void onError(AuthError ae) {
                switchToActivity(LoginActivity.class);
            }
        });
    }

    private void switchToActivity(Class<?> newActivity) {
        Intent otpIntent = new Intent(this, newActivity);
        startActivity(otpIntent);
        finish();
    }
}
