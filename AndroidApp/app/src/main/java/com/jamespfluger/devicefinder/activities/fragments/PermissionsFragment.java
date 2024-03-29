package com.jamespfluger.devicefinder.activities.fragments;

import static android.content.Context.POWER_SERVICE;

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
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jamespfluger.devicefinder.R;
import com.jamespfluger.devicefinder.activities.DevicesConfigActivity;
import com.jamespfluger.devicefinder.activities.NameActivity;
import com.jamespfluger.devicefinder.controls.PermissionsView;
import com.jamespfluger.devicefinder.utilities.Dialog;

public class PermissionsFragment extends Fragment {
    private PermissionsView overrideDndView;
    private PermissionsView disableBatteryView;
    private Activity parentActivity;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container != null) {
            container.clearDisappearingChildren();
        }

        View root = inflater.inflate(R.layout.fragment_permissions, container, false);

        if (getActivity() != null) {
            parentActivity = getActivity();
            overrideDndView = root.findViewById(R.id.permissions_override_dnd_view);
            disableBatteryView = root.findViewById(R.id.permissions_disable_battery_view);
            overrideDndView.setPermissionSupported(Build.VERSION_CODES.M);
            disableBatteryView.setPermissionSupported(Build.VERSION_CODES.M);

            ImageView helpIcon = root.findViewById(R.id.permissions_help_icon);
            helpIcon.setOnClickListener(view -> Dialog.ShowInformation(parentActivity, R.string.permissions, R.string.help_icon_permissions));

            Button continueButton = root.findViewById(R.id.permissions_continue_button);

            if (parentActivity instanceof DevicesConfigActivity) {
                continueButton.setVisibility(View.GONE);
            } else {
                continueButton.setOnClickListener(v -> {
                    if (!hasGrantedDisableBatteryPermissions() || !hasGrantedDndPermissions()) {
                        validatePermissions();
                    } else {
                        switchToDeviceNameActivity();
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

        overrideDndView.updatePermissionStatus(hasGrantedDndPermissions());
        disableBatteryView.updatePermissionStatus(hasGrantedDisableBatteryPermissions());
    }

    private void validatePermissions() {
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            if (which == DialogInterface.BUTTON_POSITIVE) {
                switchToDeviceNameActivity();
            }
        };

        MaterialAlertDialogBuilder alertBuilder = new MaterialAlertDialogBuilder(parentActivity);
        alertBuilder.setTitle(R.string.warning_message);
        alertBuilder.setIcon(R.drawable.ic_caution);
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
