package com.jamespfluger.alexadevicefinder;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;

import static android.content.Context.POWER_SERVICE;

public class PermissionsRequester {

    public void requestPermissions(Context context){

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(!notificationManager.isNotificationPolicyAccessGranted()){

                AlertDialog.Builder requireDoNotDisturb = new AlertDialog.Builder(context);
                requireDoNotDisturb.setTitle(R.string.warning);
                requireDoNotDisturb.setMessage(R.string.doNotDisturbWarning);

                if(!notificationManager.isNotificationPolicyAccessGranted()) {
                    Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                    context.startActivity(intent);
                }
            }

            PowerManager pm = (PowerManager) context.getSystemService(POWER_SERVICE);
            String packageName = context.getPackageName();
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {

                AlertDialog.Builder requireBatteryOptimization = new AlertDialog.Builder(context);
                requireBatteryOptimization.setTitle(R.string.warning);
                requireBatteryOptimization.setMessage(R.string.batteryOptimizationWarning);

                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                context.startActivity(intent);
            }
        }
    }
}
