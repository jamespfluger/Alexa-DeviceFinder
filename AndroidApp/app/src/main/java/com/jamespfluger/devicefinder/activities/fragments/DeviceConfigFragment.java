package com.jamespfluger.devicefinder.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jamespfluger.devicefinder.R;
import com.jamespfluger.devicefinder.activities.DevicesConfigActivity;
import com.jamespfluger.devicefinder.api.ApiService;
import com.jamespfluger.devicefinder.api.ManagementInterface;
import com.jamespfluger.devicefinder.databinding.FragmentDeviceConfigBinding;
import com.jamespfluger.devicefinder.models.Device;
import com.jamespfluger.devicefinder.settings.ConfigManager;
import com.jamespfluger.devicefinder.utilities.AmazonLoginHelper;
import com.jamespfluger.devicefinder.utilities.DeviceManager;
import com.jamespfluger.devicefinder.utilities.LogLevel;
import com.jamespfluger.devicefinder.utilities.Logger;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class DeviceConfigFragment extends Fragment {
    private Device device;

    public DeviceConfigFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentDeviceConfigBinding binding = FragmentDeviceConfigBinding.inflate(inflater, container, false);

        if (getArguments() != null) {
            String deviceId = DeviceConfigFragmentArgs.fromBundle(getArguments()).getDeviceId();
            this.device = DeviceManager.findDeviceById(deviceId);
        } else {
            this.device = null;
        }

        binding.setDevice(device);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        final EditText deviceName = view.findViewById(R.id.settings_device_name_field);
        deviceName.setText(device.getDeviceName());
        deviceName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus && getActivity() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);

                if (inputMethodManager != null) {
                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                v.clearFocus();
            }
        });

        final Button saveButton = view.findViewById(R.id.settings_save_button);
        final Button deleteButton = view.findViewById(R.id.settings_delete_button);

        saveButton.setOnClickListener(v -> {
            changeSavePanelVisibility(true);

            ManagementInterface managementService = ApiService.createInstance();

            Call<Void> updateSettingsCall = managementService.updateDevice(device, ConfigManager.getAlexaUserId(), device.getDeviceId());
            updateSettingsCall.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), getString(R.string.settings_save_toast) + response.message(), Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            String errorMessage = response.errorBody() != null ? response.errorBody().string() : String.format(getString(R.string.unknown_error_http_message), response.code());
                            Toast.makeText(getContext(), getString(R.string.settings_save_error_toast) + errorMessage, Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    changeSavePanelVisibility(false);
                }

                @Override
                public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                    changeSavePanelVisibility(false);
                    Toast.makeText(getContext(), getString(R.string.settings_save_error_toast) + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            });
        });

        deleteButton.setOnClickListener(v -> {
            String dialogTitle;
            String dialogMessage;
            int iconResourceId;

            if (device.getDeviceId().equals(ConfigManager.getDeviceId())) {
                dialogTitle = getString(R.string.delete_normal_device_question);
                dialogMessage = getString(R.string.confirm_delete_device) + device.getDeviceName() + getString(R.string.confirm_delete_normal_device);
                iconResourceId = R.drawable.ic_caution;
            } else {
                dialogTitle = getString(R.string.delete_active_device_question);
                dialogMessage = getString(R.string.confirm_delete_device) + device.getDeviceName() + getString(R.string.confirm_delete_active_device);
                iconResourceId = R.drawable.ic_alert;
            }

            new MaterialAlertDialogBuilder(getContext())
                    .setTitle(dialogTitle)
                    .setIcon(iconResourceId)
                    .setMessage(dialogMessage)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        Call<Void> deleteDeviceCall = ApiService.createInstance().deleteDevice(device.getAlexaUserId(), device.getDeviceId());
                        deleteDeviceCall.enqueue(new Callback<Void>() {
                            @Override
                            @EverythingIsNonNull
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    DeviceManager.removeDevice(device);
                                    DevicesConfigActivity configActivity = (DevicesConfigActivity) getActivity();
                                    Toast.makeText(getContext(), getString(R.string.settings_delete_toast) + device.getDeviceName(), Toast.LENGTH_SHORT).show();

                                    if (configActivity != null) {
                                        if (DeviceManager.isEmpty() || device.getDeviceId().equals(ConfigManager.getDeviceId())) {
                                            AmazonLoginHelper.signOut(getContext());
                                            configActivity.switchToLoginActivity();
                                        } else {
                                            configActivity.initializeSidebar();
                                            configActivity.navigateToDefaultFragment();
                                        }
                                    }
                                } else {
                                    try {
                                        String errorMessage = response.errorBody() != null ? response.errorBody().string() : String.format(getString(R.string.unknown_error_http_message), response.code());
                                        Toast.makeText(getContext(), getString(R.string.settings_delete_error_toast) + errorMessage, Toast.LENGTH_LONG).show();
                                    } catch (IOException e) {
                                        Logger.Log(e, LogLevel.Error);
                                    }
                                }
                            }

                            @Override
                            @EverythingIsNonNull
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                Logger.Log(t.getLocalizedMessage(), LogLevel.Error);
                            }
                        });
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        });

        Spinner wifiDropdown = view.findViewById(R.id.settings_wifi_ssid_dropdown);
        wifiDropdown.setEnabled(false);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, new String[]{getString(R.string.feature_not_yet_available)});
        wifiDropdown.setAdapter(adapter);
    }

    private void changeSavePanelVisibility(boolean shouldBeVisible) {
        if (getActivity() != null) {
            if (shouldBeVisible) {
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                getActivity().findViewById(R.id.settings_save_wait_panel).setVisibility(View.VISIBLE);
            } else {
                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                getActivity().findViewById(R.id.settings_save_wait_panel).setVisibility(View.GONE);
            }
        }
    }

    private void switchToActivity(Class<?> newActivity) {
        Intent newIntent = new Intent(getActivity(), newActivity);
        startActivity(newIntent);
    }
}
