package com.jamespfluger.alexadevicefinder.activities;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.Listener;
import com.amazon.identity.auth.device.api.authorization.User;
import com.amazon.identity.auth.device.api.workflow.RequestContext;
import com.jamespfluger.alexadevicefinder.OtpEditText;
import com.jamespfluger.alexadevicefinder.R;
import com.jamespfluger.alexadevicefinder.auth.AuthInterface;
import com.jamespfluger.alexadevicefinder.auth.AuthService;
import com.jamespfluger.alexadevicefinder.auth.AuthUserDevice;
import com.jamespfluger.alexadevicefinder.notifications.FirebaseService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OtpActivity extends AppCompatActivity {
    private RequestContext requestContext;
    private String userId;
    private String deviceId;
    private Retrofit retrofitEntity = new Retrofit.Builder()
            .baseUrl("https://qsbrgmx8u1.execute-api.us-west-2.amazonaws.com/prd/devicefinder/auth/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private AuthInterface authApi = retrofitEntity.create(AuthInterface.class);
    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setUserId();
        setDeviceId();

        setContentView(R.layout.activity_otp);

        findViewById(R.id.otpControlsLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (getCurrentFocus() == null)
                    return false;

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                getCurrentFocus().clearFocus();

                return true;
            }
        });

        findViewById(R.id.otpVerifyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Disable and hide views
                setViewAndChildrenEnabled(findViewById(R.id.otpControlsLayout), false);
                findViewById(R.id.verificationPanel).setVisibility(View.VISIBLE);

                // Build OTP
                StringBuilder sb = new StringBuilder();
                ViewGroup viewGroup = (ViewGroup) findViewById(R.id.otpFieldRow);
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    View child = viewGroup.getChildAt(i);
                    if (child instanceof OtpEditText)
                        sb.append(((OtpEditText) child).getText());
                }


                // Execute authorization
                AuthUserDevice authUserDevices = new AuthUserDevice(userId, deviceId, sb.toString());
                Call<Void> userCall = authApi.addUserDevice(authUserDevices);

                userCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(OtpActivity.this, "Successfully connected with Alexa", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                Toast.makeText(OtpActivity.this, response.code() + " - " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        setViewAndChildrenEnabled(findViewById(R.id.otpControlsLayout), true);
                        findViewById(R.id.verificationPanel).setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Toast.makeText(OtpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                        setViewAndChildrenEnabled(findViewById(R.id.otpControlsLayout), true);
                        findViewById(R.id.verificationPanel).setVisibility(View.GONE);
                        DisplayUiErrors();
                    }
                });

                int a = 5;

            }
        });
    }

    private void DisplayUiErrors() {
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.otpFieldRow);

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof OtpEditText)
                ((OtpEditText) child).setErrorState();
        }

        final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
        View editTexts = findViewById(R.id.otpFieldRow);
        editTexts.startAnimation(animation);
    }

    private void setUserId() {
        User.fetch(this, new Listener<User, AuthError>() {
            @Override
            public void onSuccess(User user) {
                userId = user.getUserId();
            }

            @Override
            public void onError(AuthError ae) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error retrieving profile information.\nPlease log in again", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void setDeviceId() {
        FirebaseService firebaseService = new FirebaseService(getApplicationContext());
        deviceId = firebaseService.getToken();
    }

    private static void setViewAndChildrenEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setViewAndChildrenEnabled(child, enabled);
            }
        }
    }
}
