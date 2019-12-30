package com.jamespfluger.alexadevicefinder.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.Listener;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.amazon.identity.auth.device.api.authorization.User;
import com.amazon.identity.auth.device.api.workflow.RequestContext;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.jamespfluger.alexadevicefinder.CommonTools;
import com.jamespfluger.alexadevicefinder.R;
import com.jamespfluger.alexadevicefinder.auth.AuthService;
import com.jamespfluger.alexadevicefinder.auth.UserDevice;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConfigActivity extends AppCompatActivity {
    private RequestContext requestContext;
    private ProgressBar logoutProgressBar;
    private AuthService authService;
    private String userId;
    private String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestContext = RequestContext.create(getApplicationContext());
        setContentView(R.layout.activity_config);
        initializeUI();

        // Establish REST service
        authService = new AuthService();
    
        establishUserDevicePair();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestContext.onResume();
    }

    /**
     * Initializes all of the UI elements in the activity
     */
    private void initializeUI() {
        View logoutButton = findViewById(R.id.logoutButton);
        Switch overrideVolumeControls = findViewById(R.id.overrideVolumeControls);
        EditText deviceNameField = findViewById(R.id.deviceNameField);
        SeekBar volumeSlider = findViewById(R.id.volumeSlider);
        logoutProgressBar = findViewById(R.id.logoutProgressBar);

        volumeSlider.setEnabled(false);

        deviceNameField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AuthorizationManager.signOut(getApplicationContext(), new Listener<Void, AuthError>() {
                    @Override
                    public void onSuccess(Void response) {
                        authService.deleteDevice(userId, deviceId);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(ConfigActivity.class.getName(), "LOGOUT SUCCESS");
                                setLoggingOutState(true);
                                switchToLoginActivity();
                            }
                        });
                    }
                    @Override
                    public void onError(AuthError authError) {
                        Log.w(ConfigActivity.class.getName(), "LOGOUT ERROR -> " + authError.toString());
                        setLoggingOutState(false);
                    }
                });
            }
        });

        overrideVolumeControls.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            SeekBar volumeSlider = findViewById(R.id.volumeSlider);

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                volumeSlider.setEnabled(isChecked);
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(ConfigActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void setLoggingOutState(boolean isLoggingOut){
        if (isLoggingOut){
            logoutProgressBar.setVisibility(ProgressBar.VISIBLE);
        }
        else{
            logoutProgressBar.setVisibility(ProgressBar.GONE);
        }
    }

    private void switchToLoginActivity(){
        Intent myIntent = new Intent(this, LoginActivity.class);
        overridePendingTransition(R.transition.slide_out_left,R.transition.slide_in_right);
        startActivity(myIntent);
        finish();
    }

    private void establishUserDevicePair(){
        setUserId();
        setDeviceId();
    }

    private void setUserId(){
        User.fetch(this, new Listener<User, AuthError>() {
            @Override
            public void onSuccess(User user) {
                userId = user.getUserId();
                updateUserDevice();
            }

            @Override
            public void onError(AuthError ae) {
                userId = "[ERROR]";
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    CommonTools.ShowToast(getApplicationContext(), "Error retrieving profile information.\nPlease log in again");
                    }
                });
            }
        });
    }

    private void setDeviceId(){
        FirebaseInstanceId.getInstance().getInstanceId()
            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                @Override
                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                deviceId = task.getResult().getToken();
                updateUserDevice();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    CommonTools.ShowToast(getApplicationContext(), "Error receiving Firebase token.\nPlease log in again");
                }
            });
    }

    private void updateUserDevice(){
        if(userId!=null && deviceId!=null){
            authService.addUserDevice(userId, deviceId);
        }
    }
}
