package com.jamespfluger.alexadevicefinder.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.authorization.AuthCancellation;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.amazon.identity.auth.device.api.authorization.AuthorizeListener;
import com.amazon.identity.auth.device.api.authorization.AuthorizeRequest;
import com.amazon.identity.auth.device.api.authorization.AuthorizeResult;
import com.amazon.identity.auth.device.api.authorization.ProfileScope;
import com.amazon.identity.auth.device.api.workflow.RequestContext;
import com.jamespfluger.alexadevicefinder.CommonTools;
import com.jamespfluger.alexadevicefinder.R;

public class LoginActivity extends Activity {
    private RequestContext requestContext;
    private View loginButton;
    private TextView loginText;
    private ProgressBar loginProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestContext = RequestContext.create(getApplicationContext());

        requestContext.registerListener(new AuthorizeListener() {
            @Override
            public void onSuccess(AuthorizeResult authorizeResult) {
                Log.d("DEVICEFINDER",LoginActivity.class.getName() + ":" + "LOGIN SUCCESS -> userid:" + authorizeResult.getUser().getUserId());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    CommonTools.ShowToast(getApplicationContext(), "Successfully logged into Amazon");
                    }
                });
                switchToConfigActivity();
            }
            @Override
            public void onError(AuthError authError) {
                Log.w("DEVICEFINDER",LoginActivity.class.getName() + ":" +  authError.getMessage());
                Log.w("DEVICEFINDER",LoginActivity.class.getName() + ":" +  authError.getStackTrace().toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    CommonTools.ShowToast(getApplicationContext(), "Error during authorization.  Please try again.");
                    setLoggingInState(false);
                    }
                });
            }
            @Override
            public void onCancel(AuthCancellation authCancellation) {
                Log.i("DEVICEFINDER",LoginActivity.class.getName() + ":" +  "login cancelled => " + authCancellation.getDescription());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    CommonTools.ShowToast(getApplicationContext(), "Authorization cancelled");
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

    private void switchToConfigActivity(){
        Intent configIntent = new Intent(this, ConfigActivity.class);
        this.overridePendingTransition(R.transition.slide_out_left,R.transition.slide_in_right);
        startActivity(configIntent);
        finish();
    }
}
