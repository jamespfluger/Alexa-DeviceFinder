package com.jamespfluger.devicefinder.notifications;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jamespfluger.devicefinder.R;
import com.jamespfluger.devicefinder.settings.ConfigManager;
import com.jamespfluger.devicefinder.utilities.Logger;

public class FirebaseService extends FirebaseMessagingService {
    private NotificationForge notificationForge;

    public FirebaseService() {
    }

    @Override
    public void onNewToken(@NonNull String newToken) {
        super.onNewToken(newToken);
        ConfigManager.setFirebaseToken(newToken);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (notificationForge == null) {
            notificationForge = new NotificationForge(getApplicationContext());
        }

        notificationForge.issueNotification(remoteMessage);
    }

    public void refreshToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String newToken = task.getResult();
                        ConfigManager.setFirebaseToken(newToken);
                    }
                })
                .addOnFailureListener(ex -> {
                    Logger.log(getString(R.string.firebase_refresh_error_log) + ex.toString());
                    Toast.makeText(getApplicationContext(), getString(R.string.firebase_refresh_error_toast) + ex.toString(), Toast.LENGTH_SHORT).show();
                });
    }
}
