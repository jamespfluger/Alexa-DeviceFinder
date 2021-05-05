package com.jamespfluger.devicefinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jamespfluger.devicefinder.R;
import com.jamespfluger.devicefinder.settings.ConfigManager;

public class NameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_name);
        initializeUi();
    }

    private void initializeUi() {
        final EditText deviceNameField = findViewById(R.id.deviceNameField);
        final Button deviceNameContinueButton = findViewById(R.id.deviceNameContinueButton);

        deviceNameField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(NameActivity.INPUT_METHOD_SERVICE);

                    if (inputMethodManager != null) {
                        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                } else {
                    TextView deviceNameErrorDescription = findViewById(R.id.deviceNameErrorDescription);
                    deviceNameErrorDescription.setVisibility(View.INVISIBLE);
                }
            }
        });

        deviceNameContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deviceNameField.getText().length() == 0) {
                    TextView deviceNameErrorDescription = findViewById(R.id.deviceNameErrorDescription);
                    deviceNameErrorDescription.setVisibility(View.VISIBLE);

                    final Animation errorAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    deviceNameField.startAnimation(errorAnimation);
                } else {
                    ConfigManager.setDeviceNameConfig(deviceNameField.getText().toString());
                    switchToOtpActivity();
                }
            }
        });
    }

    private void switchToOtpActivity() {
        Intent newIntent = new Intent(this, OtpActivity.class);
        startActivity(newIntent);
    }
}
