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
import com.jamespfluger.devicefinder.utilities.PreferencesManager;

public class NameActivity extends AppCompatActivity {
    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesManager = new PreferencesManager(getApplicationContext());

        setContentView(R.layout.activity_name);
        initializeUi();
    }

    private void initializeUi() {
        final EditText deviceNameField = findViewById(R.id.deviceNameField);
        final Button deviceNameContinueButton = findViewById(R.id.deviceNameContinueButton);
        // TODO: remove this before going live
        deviceNameField.setText("James");

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
                    deviceNameErrorDescription.setText("You must provide a device name before continuing.");
                    deviceNameErrorDescription.setVisibility(View.VISIBLE);

                    final Animation errorAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                    deviceNameField.startAnimation(errorAnimation);
                } else {
                    preferencesManager.setDeviceName(deviceNameField.getText().toString());
                    switchToActivity(OtpActivity.class);
                }
            }
        });
    }

    private void switchToActivity(Class<?> newActivity) {
        Intent newIntent = new Intent(this, newActivity);
        startActivity(newIntent);
        finish();
    }
}
