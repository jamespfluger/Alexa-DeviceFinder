package com.jamespfluger.devicefinder.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.RingtoneManager;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.jamespfluger.devicefinder.R;

public final class NotificationForge {
    private static final String CHANNEL_ID = "CHANNEL_4096";
    private final Context context;

    public NotificationForge(Context context) {
        this.context = context;
    }

    public static void initializeNotificationChannel(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O && notificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, context.getString(R.string.deviceAlertChannelName), NotificationManager.IMPORTANCE_HIGH);

            AudioAttributes ringtoneAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            channel.setBypassDnd(true);
            channel.setSound(RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE), ringtoneAttributes);

            notificationManager.createNotificationChannel(channel);
        }
    }

    public void issueNotification(RemoteMessage remoteMessage) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = (int) System.currentTimeMillis();

        notificationBuilder.setChannelId(CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_notification)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setDeleteIntent(createOnDismissedIntent(notificationId));

        notificationBuilder.setContentText(remoteMessage.getNotification().getTitle());
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    private PendingIntent createOnDismissedIntent(int notificationId) {
        Intent intent = new Intent(context, NotificationDismissedReceiver.class);
        return PendingIntent.getBroadcast(context, notificationId, intent, 0);
    }
}
