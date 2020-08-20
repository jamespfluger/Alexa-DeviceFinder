package com.jamespfluger.alexadevicefinder.notifications;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class FirebaseService extends FirebaseMessagingService {
    NotificationForge notificationForge;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (notificationForge == null)
            notificationForge = new NotificationForge(getApplicationContext());

        notificationForge.issueNotification(remoteMessage);
    }
}
