package com.jamespfluger.alexadevicefinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.Listener;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.amazon.identity.auth.device.api.workflow.RequestContext;
import com.jamespfluger.alexadevicefinder.R;

public class ConfigActivity extends AppCompatActivity {
    private RequestContext requestContext;
    private ProgressBar logoutProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestContext = RequestContext.create(getApplicationContext());
        setContentView(R.layout.activity_config);
        initializeUI();
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
        logoutProgressBar = findViewById(R.id.logoutProgressBar);

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
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setLoggingOutState(true);
                                switchToLoginActivity();
                            }
                        });
                    }
                    @Override
                    public void onError(AuthError authError) {
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
}
