package com.jamespfluger.alexadevicefinder.notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.jamespfluger.alexadevicefinder.R;

public final class NotificationService {
    private Context context;
    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;

    private final int NOTIFICATION_ID = 1024;
    private final String CHANNEL_ID = "4096";

    public NotificationService(Context context){
        this.context = context;

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
    }

    public void issueNotification(String title, String message){
        Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            createNotificationChannel("Device Alert (Required)");
        }

         notificationBuilder.setSmallIcon(R.mipmap.ic_launcher_round)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setAutoCancel(false)
                            .setSound(defaultRingtoneUri);

        setDeviceToMaxVolume();
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    @RequiresApi(api= Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelName){
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
        Uri defaultRingtoneUri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE);

        AudioAttributes att = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();
        channel.setSound(defaultRingtoneUri, att);

        notificationBuilder.setChannelId(CHANNEL_ID);
        notificationManager.createNotificationChannel(channel);
    }

    private void setDeviceToMaxVolume(){
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0);
    }
}
