package com.jamespfluger.alexadevicefinder.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.authorization.AuthCancellation;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.amazon.identity.auth.device.api.authorization.AuthorizeListener;
import com.amazon.identity.auth.device.api.authorization.AuthorizeRequest;
import com.amazon.identity.auth.device.api.authorization.AuthorizeResult;
import com.amazon.identity.auth.device.api.authorization.ProfileScope;
import com.amazon.identity.auth.device.api.workflow.RequestContext;
import com.jamespfluger.alexadevicefinder.R;

public class LoginActivity extends Activity {
    private RequestContext requestContext;
    private View loginButton;
    private View alexaConnectButton;
    private TextView loginText;
    private ProgressBar loginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestContext = RequestContext.create(getApplicationContext());

        requestContext.registerListener(new AuthorizeListener() {
            @Override
            public void onSuccess(AuthorizeResult authorizeResult) {
                Log.d("DEVICEFINDER", LoginActivity.class.getName() + ":" + "LOGIN SUCCESS -> userid:" + authorizeResult.getUser().getUserId());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Successfully logged into Amazon", Toast.LENGTH_SHORT).show();
                    }
                });
                switchToConfigActivity();
            }

            @Override
            public void onError(AuthError authError) {
                Log.w("DEVICEFINDER", LoginActivity.class.getName() + ":" + authError.getMessage());
                Log.w("DEVICEFINDER", LoginActivity.class.getName() + ":" + authError.getStackTrace().toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Error logging in. Please try again.", Toast.LENGTH_SHORT).show();
                        setLoggingInState(false);
                    }
                });
            }

            @Override
            public void onCancel(AuthCancellation authCancellation) {
                Log.i("DEVICEFINDER", LoginActivity.class.getName() + ":" + "login cancelled => " + authCancellation.getDescription());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Login cancelled.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        setContentView(R.layout.activity_login);
        initializeUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestContext.onResume();
    }

    private void initializeUI() {
        loginProgressBar = findViewById(R.id.loginProgressBar);
        loginButton = findViewById(R.id.loginButton);
        loginText = (TextView) findViewById(R.id.loginText);
        alexaConnectButton = findViewById(R.id.alexaConnectButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthorizationManager.authorize(
                        new AuthorizeRequest.Builder(requestContext)
                                .addScopes(ProfileScope.userId())
                                .build()
                );

                setLoggingInState(true);
            }
        });

        alexaConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToOtpActivity();
            }
        });
    }

    private void setLoggingInState(final boolean loggingIn) {
        if (loggingIn) {
            loginButton.setVisibility(Button.GONE);
            loginText.setVisibility(TextView.GONE);
            loginProgressBar.setVisibility(ProgressBar.VISIBLE);
        } else {
            loginButton.setVisibility(Button.VISIBLE);
            loginText.setVisibility(TextView.VISIBLE);
            loginProgressBar.setVisibility(ProgressBar.GONE);
        }
    }

    private void switchToConfigActivity() {
        Intent configIntent = new Intent(this, ConfigActivity.class);
        this.overridePendingTransition(R.transition.slide_out_left, R.transition.slide_in_right);
        startActivity(configIntent);
        finish();
    }

    private void switchToOtpActivity(){
        Intent otpIntent = new Intent(this, OtpActivity.class);
        this.overridePendingTransition(R.transition.slide_out_left,R.transition.slide_in_right);
        startActivity(otpIntent);
        finish();
    }
}
