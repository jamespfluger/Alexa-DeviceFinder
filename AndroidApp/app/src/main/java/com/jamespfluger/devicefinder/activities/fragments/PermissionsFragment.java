package com.jamespfluger.devicefinder.activities.fragments;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.jamespfluger.devicefinder.R;
import com.jamespfluger.devicefinder.activities.DevicesConfigActivity;
import com.jamespfluger.devicefinder.activities.NameActivity;
import com.jamespfluger.devicefinder.controls.PermissionsView;

import static android.content.Context.POWER_SERVICE;

public class PermissionsFragment extends Fragment {
    private PermissionsView disableBatteryView;
    private PermissionsView overrideDndView;
    private Activity parentActivity;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.clearDisappearingChildren();
        }

        View root = inflater.inflate(R.layout.fragment_permissions, container, false);

        if (getActivity() != null) {
            parentActivity = getActivity();
            disableBatteryView = root.findViewById(R.id.permissionsDisableBatteryView);
            overrideDndView = root.findViewById(R.id.permissionsOverrideDndView);

            Button continueButton = root.findViewById(R.id.permissionsContinueButton);

            if (parentActivity instanceof DevicesConfigActivity) {
                continueButton.setVisibility(View.GONE);
            } else {
                continueButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!hasGrantedDisableBatteryPermissions() || !hasGrantedDisableBatteryPermissions()) {
                            validatePermissions();
                        } else {
                            switchToDeviceNameActivity();
                        }
                    }
                });
            }
        }

        return root;
    }

    public boolean hasGrantedDisableBatteryPermissions() {
        PowerManager pm = (PowerManager) parentActivity.getSystemService(POWER_SERVICE);
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || pm.isIgnoringBatteryOptimizations(parentActivity.getPackageName());
    }

    public boolean hasGrantedDndPermissions() {
        NotificationManager notificationManager = (NotificationManager) parentActivity.getSystemService(Context.NOTIFICATION_SERVICE);
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || notificationManager.isNotificationPolicyAccessGranted();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (hasGrantedDisableBatteryPermissions() && disableBatteryView.getPermissionStatusButton() != null) {
            disableBatteryView.getPermissionStatusButton().setImageResource(R.drawable.check_circle);
            disableBatteryView.getPermissionStatusButton().setColorFilter(ContextCompat.getColor(parentActivity, R.color.green));
        } else if (disableBatteryView.getPermissionStatusButton() != null) {
            disableBatteryView.getPermissionStatusButton().setImageResource(R.drawable.alert_circle);
            disableBatteryView.getPermissionStatusButton().setColorFilter(ContextCompat.getColor(parentActivity, R.color.red_error));
        }

        if (hasGrantedDndPermissions() && overrideDndView.getPermissionStatusButton() != null) {
            overrideDndView.getPermissionStatusButton().setImageResource(R.drawable.check_circle);
            overrideDndView.getPermissionStatusButton().setColorFilter(ContextCompat.getColor(parentActivity, R.color.green));
        } else if (overrideDndView.getPermissionStatusButton() != null) {
            overrideDndView.getPermissionStatusButton().setImageResource(R.drawable.alert_circle);
            overrideDndView.getPermissionStatusButton().setColorFilter(ContextCompat.getColor(parentActivity, R.color.red_error));
        }
    }

    private void validatePermissions() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    switchToDeviceNameActivity();
                }
            }
        };

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(parentActivity);
        alertBuilder.setTitle(R.string.warning_message);
        alertBuilder.setIcon(R.drawable.caution_triangle);
        alertBuilder.setMessage(R.string.confirm_deny_permissions);
        alertBuilder.setPositiveButton(R.string.yes, dialogClickListener);
        alertBuilder.setNegativeButton(R.string.no, dialogClickListener);
        alertBuilder.show();
    }

    private void switchToDeviceNameActivity() {
        Intent newIntent = new Intent(parentActivity, NameActivity.class);
        startActivity(newIntent);
    }
}
