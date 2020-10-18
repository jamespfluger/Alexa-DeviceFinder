package com.jamespfluger.alexadevicefinder.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.jamespfluger.alexadevicefinder.utilities.AmazonLoginHelper;

import java.util.Random;

public class LoginActivity extends Activity {
    private RequestContext requestContext;
    private Button loginButton;
    private TextView loginText;
    private ProgressBar loginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: remove before release
        AmazonLoginHelper.signOut(getApplicationContext());
        requestContext = RequestContext.create(getApplicationContext());

        requestContext.registerListener(new AuthorizeListener() {
            @Override
            public void onSuccess(AuthorizeResult authorizeResult) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Successfully logged into Amazon.", Toast.LENGTH_SHORT).show();
                    }
                });
                switchToNameActivity();
            }

            @Override
            public void onError(AuthError authError) {
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Login cancelled.", Toast.LENGTH_SHORT).show();
                        setLoggingInState(false);
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

        // TODO: implement way to pick between these
        Random r = new Random();
        if (r.nextBoolean()) {
            loginButton.setText("LOGIN WITH AMAZON");
            loginButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.amazon_logo, 0, 0, 0);
        } else {
            loginButton.setText("CONNECT WITH ALEXA");
            loginButton.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.amazon_alexa_logo, 0, 0, 0);
        }

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

    private void switchToNameActivity() {
        Intent otpIntent = new Intent(this, NameActivity.class);
        startActivity(otpIntent);
        finish();
    }
}
