package com.jamespfluger.devicefinder.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
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
import com.jamespfluger.devicefinder.controls.SettingsView;
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

        final Spinner wifiDropdown = view.findViewById(R.id.settings_wifi_ssid_dropdown);
        wifiDropdown.setEnabled(false);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, new String[]{getString(R.string.feature_not_yet_available)});
        wifiDropdown.setAdapter(adapter);

        saveButton.setOnClickListener(v -> saveDevice());

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
                        Call<Void> deleteDeviceCall = ApiService.getInstance().deleteDevice(device.getAlexaUserId(), device.getDeviceId());
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
                                        Logger.log(errorMessage, LogLevel.Error);
                                    } catch (IOException e) {
                                        Logger.log(e, LogLevel.Error);
                                    }
                                }
                            }

                            @Override
                            @EverythingIsNonNull
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(getContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                Logger.log(t.getLocalizedMessage(), LogLevel.Error);
                            }
                        });
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        });

        initializeOnClickListeners();
    }

    private void saveDevice() {
        ManagementInterface managementService = ApiService.getInstance();

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
                        Logger.log(errorMessage, LogLevel.Error);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Logger.log(e.getLocalizedMessage(), LogLevel.Error);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), getString(R.string.settings_save_error_toast) + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Logger.log(t.getLocalizedMessage(), LogLevel.Error);
            }
        });
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

    private void initializeOnClickListeners() {
        if (getActivity() != null) {
            SettingsView useFlashlightSetting = getActivity().findViewById(R.id.settings_enable_flashlight);
            SettingsView useVibrateSetting = getActivity().findViewById(R.id.settings_enable_vibration);
            SettingsView useOnWifiOnly = getActivity().findViewById(R.id.settings_enable_wifi);
            Spinner wifiDropdown = getActivity().findViewById(R.id.settings_wifi_ssid_dropdown);
            SeekBar volumeOverrideValueSlider = getActivity().findViewById(R.id.settings_volume_to_use_slider);

            useFlashlightSetting.setSaveListener(createSaveDeviceSwitchListener());
            useVibrateSetting.setSaveListener(createSaveDeviceSwitchListener());
            useOnWifiOnly.setSaveListener(createSaveDeviceSwitchListener());
            wifiDropdown.setOnItemSelectedListener(createSaveDeviceSpinnerListener());
            volumeOverrideValueSlider.setOnClickListener(createSaveDeviceSwitchListener());
            volumeOverrideValueSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    device.setVolumeOverrideValue(progress);
                }

                @Override
                 public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    saveDevice();
                }
            });
        }
    }

    private View.OnClickListener createSaveDeviceSwitchListener() {
        return v -> saveDevice();
    }

    private AdapterView.OnItemSelectedListener createSaveDeviceSpinnerListener() {
        return new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                saveDevice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                saveDevice();
            }
        };
    }

    private SeekBar.OnSeekBarChangeListener createSaveDeviceSeekBarListener() {
        return new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Logger.log("Progress changing " + progress + " " + fromUser);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Logger.log("Starting to track...");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Logger.log("Stopping track...");
                saveDevice();
            }
        };
    }

    private void switchToActivity(Class<?> newActivity) {
        Intent newIntent = new Intent(getActivity(), newActivity);
        startActivity(newIntent);
    }
}
