package com.jamespfluger.alexadevicefinder.notifications;

import android.app.Activity;
import android.app.LauncherActivity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.jamespfluger.alexadevicefinder.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class NotificationForge {
    private Context context;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;

    private final int NOTIFICATION_ID = Integer.parseInt(new SimpleDateFormat("ddHHmmss",  Locale.US).format(new Date()));
    private final String CHANNEL_ID = "4096";

    public NotificationForge(Context context){
        this.context = context;

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            createNotificationChannel();
        }
    }

    public void issueNotification(String title, String message){
        setDeviceToMaxVolume();

        Intent cancelIntent = new Intent(context, CancelNotificationReceiver.class);
        PendingIntent pendingCancelIntent = PendingIntent.getBroadcast(context, 0, cancelIntent, 0);

         notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_round)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setChannelId(CHANNEL_ID)
                            .setAutoCancel(true)
                            .setDeleteIntent(pendingCancelIntent)
                            .setContentIntent(pendingCancelIntent)
                            .setOnlyAlertOnce(true);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

        Intent ringtoneIntent = new Intent(context, RingtonePlayingService.class);
        context.startService(ringtoneIntent);
    }

    @RequiresApi(api= Build.VERSION_CODES.O)
    private void createNotificationChannel(){
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, context.getString(R.string.deviceAlertChannelName), NotificationManager.IMPORTANCE_HIGH);
        channel.setSound(null, null); // Override the default notification sound so only our custom one plays

        notificationBuilder.setChannelId(CHANNEL_ID);
        notificationManager.createNotificationChannel(channel);
    }

    private void setDeviceToMaxVolume(){
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = manager.getStreamMaxVolume(AudioManager.STREAM_RING);
        manager.setStreamVolume(AudioManager.STREAM_RING, maxVolume, 0);
    }
}
