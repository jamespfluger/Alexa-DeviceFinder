package com.jamespfluger.alexadevicefinder.notifications;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class FirebaseService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s)
    {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        NotificationForge notificationService = new NotificationForge(getApplicationContext());

        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        notificationService.issueNotification(title, body);
    }

}