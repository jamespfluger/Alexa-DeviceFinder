package com.jamespfluger.devicefinder.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.jamespfluger.devicefinder.R;
import com.jamespfluger.devicefinder.controls.PermissionsView;

public class PermissionsActivity extends AppCompatActivity {
    PermissionsView disableBatteryView;
    PermissionsView overrideDndView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        disableBatteryView = findViewById(R.id.permissionsDisableBatteryView);
        overrideDndView = findViewById(R.id.permissionsOverrideDndView);
    }

    public boolean hasGranteddisableBatteryPermissions() {
        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(POWER_SERVICE);

        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || pm.isIgnoringBatteryOptimizations(getPackageName());
    }

    public boolean hasGrantedDndPermissions() {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || notificationManager.isNotificationPolicyAccessGranted();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (hasGranteddisableBatteryPermissions() && disableBatteryView.getPermissionStatusButton() != null) {
            disableBatteryView.getPermissionStatusButton().setImageResource(R.drawable.check_circle);
            disableBatteryView.getPermissionStatusButton().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.green));
        }
        else if (disableBatteryView.getPermissionStatusButton() != null){
            disableBatteryView.getPermissionStatusButton().setImageResource(R.drawable.alert_circle);
            disableBatteryView.getPermissionStatusButton().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.errorred));
        }

        if (hasGrantedDndPermissions() && overrideDndView.getPermissionStatusButton() != null) {
            overrideDndView.getPermissionStatusButton().setImageResource(R.drawable.check_circle);
            overrideDndView.getPermissionStatusButton().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.green));
        }
        else if (overrideDndView.getPermissionStatusButton() != null){
            overrideDndView.getPermissionStatusButton().setImageResource(R.drawable.alert_circle);
            overrideDndView.getPermissionStatusButton().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.errorred));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}