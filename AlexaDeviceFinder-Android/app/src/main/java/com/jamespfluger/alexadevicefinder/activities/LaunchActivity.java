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
import com.jamespfluger.alexadevicefinder.PermissionsRequester;
import com.jamespfluger.alexadevicefinder.R;
import com.jamespfluger.alexadevicefinder.notifications.FirebaseService;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseService firebaseService = new FirebaseService(getApplicationContext());
        firebaseService.refreshToken();

        setContentView(R.layout.activity_launch);
        selectActivityToLaunch();
    }

    private void selectActivityToLaunch() {
        Scope[] scopes = {ProfileScope.userId()};

        AuthorizationManager.getToken(this, scopes, new Listener<AuthorizeResult, AuthError>() {
            Intent intentToLaunch;

            @Override
            public void onSuccess(AuthorizeResult result) {

                if (result.getAccessToken() != null) {
                    intentToLaunch = new Intent(getApplicationContext(), OtpActivity.class);
                } else {
                    intentToLaunch = new Intent(getApplicationContext(), LoginActivity.class);
                }

                PermissionsRequester permissionsRequester = new PermissionsRequester();
                permissionsRequester.requestPermissions(LaunchActivity.this);

                startActivity(intentToLaunch);
                finish();
            }

            @Override
            public void onError(AuthError ae) {
                intentToLaunch = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intentToLaunch);
                finish();
            }
        });
    }
}
