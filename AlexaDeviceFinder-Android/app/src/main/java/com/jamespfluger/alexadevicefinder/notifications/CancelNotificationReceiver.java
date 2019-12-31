package com.jamespfluger.alexadevicefinder.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CancelNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent stopIntent = new Intent(context, RingtonePlayingService.class);
        context.stopService(stopIntent);
    }
}
