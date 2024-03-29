package com.jamespfluger.devicefinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jamespfluger.devicefinder.R;
import com.jamespfluger.devicefinder.api.ApiService;
import com.jamespfluger.devicefinder.controls.OtpEditText;
import com.jamespfluger.devicefinder.models.AuthData;
import com.jamespfluger.devicefinder.models.Device;
import com.jamespfluger.devicefinder.settings.ConfigManager;
import com.jamespfluger.devicefinder.settings.SettingsManager;
import com.jamespfluger.devicefinder.utilities.Dialog;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class OtpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_otp);
        findViewById(R.id.otp_controls_layout).setOnTouchListener(createControlsTouchListener());
        findViewById(R.id.otp_verify_button).setOnClickListener(createVerifyClickListener());
        findViewById(R.id.otp_help_icon).setOnClickListener(view -> Dialog.ShowInformation(this, R.string.verification, R.string.help_icon_verification));
    }

    private View.OnClickListener createVerifyClickListener() {
        return v -> {
            // Disable and hide views
            setViewAndChildrenEnabled(findViewById(R.id.otp_controls_layout), false);
            findViewById(R.id.otp_verification_panel).setVisibility(View.VISIBLE);

            // Build OTP
            StringBuilder otpBuilder = new StringBuilder();
            ViewGroup viewGroup = findViewById(R.id.otp_field_row);
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                if (child instanceof OtpEditText) {
                    otpBuilder.append(((OtpEditText) child).getText());
                }
            }

            if (otpBuilder.length() != 6) {
                displayUiErrors();
                setViewAndChildrenEnabled(findViewById(R.id.otp_controls_layout), true);
                findViewById(R.id.otp_verification_panel).setVisibility(View.GONE);
                return;
            }

            // Build auth device
            AuthData authUserDevices = new AuthData();

            authUserDevices.setLoginUserId(ConfigManager.getLoginUserId());
            authUserDevices.setLoginUserId(ConfigManager.getLoginUserId());
            authUserDevices.setFirebaseToken(ConfigManager.getFirebaseToken());
            authUserDevices.setDeviceName(SettingsManager.getDeviceName());
            authUserDevices.setOtp(otpBuilder.toString());

            // Execute authorization
            Call<Device> userCall = ApiService.createDevice(authUserDevices);
            userCall.enqueue(new Callback<Device>() {
                @Override
                @EverythingIsNonNull
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(OtpActivity.this, getString(R.string.alexa_successfully_connected), Toast.LENGTH_SHORT).show();
                        Device newDevice = (Device) response.body();
                        ConfigManager.setAlexaUserId(newDevice.getAlexaUserId());
                        ConfigManager.setDeviceId(newDevice.getDeviceId());
                        switchToConfigActivity();
                    } else {
                        try {
                            String errorMessage = response.errorBody() != null ? response.errorBody().string() : getString(R.string.unknown_error);
                            Toast.makeText(OtpActivity.this, response.code() + " - " + errorMessage, Toast.LENGTH_SHORT).show();
                            displayUiErrors();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    setViewAndChildrenEnabled(findViewById(R.id.otp_controls_layout), true);
                    findViewById(R.id.otp_verification_panel).setVisibility(View.GONE);
                }

                @Override
                @EverythingIsNonNull
                public void onFailure(Call call, Throwable t) {
                    Toast.makeText(OtpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    setViewAndChildrenEnabled(findViewById(R.id.otp_controls_layout), true);
                    findViewById(R.id.otp_verification_panel).setVisibility(View.GONE);
                    displayUiErrors();
                }
            });
        };
    }

    private View.OnTouchListener createControlsTouchListener() {
        return (v, event) -> {
            if (getCurrentFocus() == null) {
                return false;
            }

            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

            if (imm != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                getCurrentFocus().clearFocus();
            }

            return v.performClick();
        };
    }

    private void displayUiErrors() {
        ViewGroup viewGroup = findViewById(R.id.otp_field_row);

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof OtpEditText) {
                ((OtpEditText) child).setErrorState();
            }
        }

        final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        viewGroup.startAnimation(animation);
    }

    private void setViewAndChildrenEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);

        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;

            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setViewAndChildrenEnabled(child, enabled);
            }
        }
    }

    private void switchToConfigActivity() {
        Intent configIntent = new Intent(this, DevicesConfigActivity.class);
        configIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(configIntent);
        finish();
    }
}
