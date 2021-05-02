package com.jamespfluger.devicefinder.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;

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

    public boolean hasGrantedDisableBatteryPermissions() {
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

        if (hasGrantedDisableBatteryPermissions() && disableBatteryView.getPermissionStatusButton() != null) {
            disableBatteryView.getPermissionStatusButton().setImageResource(R.drawable.check_circle);
            disableBatteryView.getPermissionStatusButton().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.green));
        }
        else if (disableBatteryView.getPermissionStatusButton() != null){
            disableBatteryView.getPermissionStatusButton().setImageResource(R.drawable.alert_circle);
            disableBatteryView.getPermissionStatusButton().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.red_error));
        }

        if (hasGrantedDndPermissions() && overrideDndView.getPermissionStatusButton() != null) {
            overrideDndView.getPermissionStatusButton().setImageResource(R.drawable.check_circle);
            overrideDndView.getPermissionStatusButton().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.green));
        }
        else if (overrideDndView.getPermissionStatusButton() != null){
            overrideDndView.getPermissionStatusButton().setImageResource(R.drawable.alert_circle);
            overrideDndView.getPermissionStatusButton().setColorFilter(ContextCompat.getColor(getApplicationContext(), R.color.red_error));
        }
    }

    public void onContinueClick(View view) {
        if (!hasGrantedDisableBatteryPermissions() || !hasGrantedDisableBatteryPermissions()) {
            validatePermissions();
        }
        else {
            switchToActivity();
        }
    }

    private void validatePermissions() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    switchToActivity();
                }
            }
        };

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Warning");
        alertBuilder.setIcon(R.drawable.caution_triangle);
        alertBuilder.setMessage("Are you sure you don't want to grant these permissions? It may take longer to find your device!\n\nYou can always change this later.");
        alertBuilder.setPositiveButton("Yes", dialogClickListener);
        alertBuilder.setNegativeButton("No", dialogClickListener);
        alertBuilder.show();
    }

    private void switchToActivity() {
        Intent newIntent = new Intent(this, NameActivity.class);
        startActivity(newIntent);
        finish();
    }
}
