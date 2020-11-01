package com.jamespfluger.alexadevicefinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jamespfluger.alexadevicefinder.OtpEditText;
import com.jamespfluger.alexadevicefinder.R;
import com.jamespfluger.alexadevicefinder.api.ApiService;
import com.jamespfluger.alexadevicefinder.api.AuthInterface;
import com.jamespfluger.alexadevicefinder.models.AuthUserDevice;
import com.jamespfluger.alexadevicefinder.models.UserDevice;
import com.jamespfluger.alexadevicefinder.utilities.PreferencesManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class OtpActivity extends AppCompatActivity {
    private PreferencesManager preferencesManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesManager = new PreferencesManager(getApplicationContext());

        setContentView(R.layout.activity_otp);
        findViewById(R.id.otpControlsLayout).setOnTouchListener(createControlsTouchListener());
        findViewById(R.id.otpVerifyButton).setOnClickListener(createVerifyClickListener());
    }

    private View.OnClickListener createVerifyClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Disable and hide views
                setViewAndChildrenEnabled(findViewById(R.id.otpControlsLayout), false);
                findViewById(R.id.otpVerificationPanel).setVisibility(View.VISIBLE);

                // Build OTP
                StringBuilder otpBuilder = new StringBuilder();
                ViewGroup viewGroup = findViewById(R.id.otpFieldRow);
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    View child = viewGroup.getChildAt(i);
                    if (child instanceof OtpEditText)
                        otpBuilder.append(((OtpEditText) child).getText());
                }

                if (otpBuilder.length() != 6) {
                    displayUiErrors();
                    setViewAndChildrenEnabled(findViewById(R.id.otpControlsLayout), true);
                    findViewById(R.id.otpVerificationPanel).setVisibility(View.GONE);
                    return;
                }

                // Build auth device
                AuthUserDevice authUserDevices = new AuthUserDevice();
                authUserDevices.setUserId(preferencesManager.getAmazonUserId());
                authUserDevices.setDeviceId(preferencesManager.getDeviceId());
                authUserDevices.setDeviceName(preferencesManager.getDeviceName());
                authUserDevices.setOtp(otpBuilder.toString());

                // Execute authorization
                AuthInterface authApi = ApiService.createAuthInstance();
                Call<UserDevice> userCall = authApi.addUserDevice(authUserDevices);
                userCall.enqueue(new Callback<UserDevice>() {
                    @Override
                    @EverythingIsNonNull
                    public void onResponse(Call call, Response response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(OtpActivity.this, "Successfully connected with Alexa", Toast.LENGTH_SHORT).show();
                            UserDevice newDevice = (UserDevice) response.body();
                            preferencesManager.setUserId(newDevice.getAlexaUserId());
                            switchToConfigActivity();
                        } else {
                            try {
                                String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                                Toast.makeText(OtpActivity.this, response.code() + " - " + errorMessage, Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        setViewAndChildrenEnabled(findViewById(R.id.otpControlsLayout), true);
                        findViewById(R.id.otpVerificationPanel).setVisibility(View.GONE);
                    }

                    @Override
                    @EverythingIsNonNull
                    public void onFailure(Call call, Throwable t) {
                        Toast.makeText(OtpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        setViewAndChildrenEnabled(findViewById(R.id.otpControlsLayout), true);
                        findViewById(R.id.otpVerificationPanel).setVisibility(View.GONE);
                        displayUiErrors();
                    }
                });
            }
        };
    }

    private View.OnTouchListener createControlsTouchListener() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (getCurrentFocus() == null)
                    return false;

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

                if (imm != null) {
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    getCurrentFocus().clearFocus();
                }

                return v.performClick();
            }
        };
    }

    private void displayUiErrors() {
        ViewGroup viewGroup = findViewById(R.id.otpFieldRow);

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof OtpEditText)
                ((OtpEditText) child).setErrorState();
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
        Intent otpIntent = new Intent(this, DevicesConfigActivity.class);
        startActivity(otpIntent);
        finish();
    }
}
