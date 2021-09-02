package com.jamespfluger.devicefinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.jamespfluger.devicefinder.R;
import com.jamespfluger.devicefinder.settings.SettingsManager;
import com.jamespfluger.devicefinder.utilities.Dialog;

public class NameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_name);
        initializeUi();
    }

    private void initializeUi() {
        final EditText deviceNameField = findViewById(R.id.device_name_field);
        final Button deviceNameContinueButton = findViewById(R.id.device_name_continue_button);
        final ImageView helpIcon = findViewById(R.id.device_name_help_icon);

        deviceNameField.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(NameActivity.INPUT_METHOD_SERVICE);

                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            } else {
                TextView deviceNameErrorDescription = findViewById(R.id.device_name_error_description);
                deviceNameErrorDescription.setVisibility(View.INVISIBLE);
            }
        });

        deviceNameContinueButton.setOnClickListener(v -> {
            if (deviceNameField.getText().length() == 0) {
                TextView deviceNameErrorDescription = findViewById(R.id.device_name_error_description);
                deviceNameErrorDescription.setVisibility(View.VISIBLE);

                final Animation errorAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                deviceNameField.startAnimation(errorAnimation);
            } else {
                SettingsManager.setDeviceName(deviceNameField.getText().toString());
                switchToOtpActivity();
            }
        });

        helpIcon.setOnClickListener(view -> Dialog.ShowInformation(this, R.string.device_name, R.string.help_icon_device_name));
    }

    private void switchToOtpActivity() {
        Intent newIntent = new Intent(this, OtpActivity.class);
        startActivity(newIntent);
    }
}
