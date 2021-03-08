package com.jamespfluger.devicefinder.utilities;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.Listener;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.amazon.identity.auth.device.api.authorization.User;

public class AmazonLoginHelper {
    public static void setUserId(final Context context) {
        User.fetch(context, new Listener<User, AuthError>() {
            @Override
            public void onSuccess(User user) {
                PreferencesManager preferencesManager = new PreferencesManager(context);
                preferencesManager.setAmazonUserId(user.getUserId());
            }

            @Override
            public void onError(AuthError ae) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Error retrieving profile information.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public static void signOut(final Context context) {
        AuthorizationManager.signOut(context, new Listener<Void, AuthError>() {
            @Override
            public void onSuccess(Void response) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        //Toast.makeText(context, "Signed out", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onError(AuthError authError) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Failed to sign out", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
