package com.jamespfluger.devicefinder.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxRingVolume = manager.getStreamMaxVolume(AudioManager.STREAM_RING);
        manager.setStreamVolume(AudioManager.STREAM_RING, maxRingVolume, 0);
    }
}
