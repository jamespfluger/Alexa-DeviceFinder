package com.jamespfluger.alexadevicefinder.activities.ui.device;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.jamespfluger.alexadevicefinder.R;
import com.jamespfluger.alexadevicefinder.api.ApiService;
import com.jamespfluger.alexadevicefinder.api.ManagementInterface;
import com.jamespfluger.alexadevicefinder.models.DeviceSettings;
import com.jamespfluger.alexadevicefinder.models.EndpointType;
import com.jamespfluger.alexadevicefinder.models.UserDevice;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class DeviceFragment extends Fragment {
    private UserDevice userDevice;

    public DeviceFragment(UserDevice userDevice) {
        this.userDevice = userDevice;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_device_config, container, false);
        return root;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        final EditText deviceName = view.findViewById(R.id.settingsDeviceNameField);
        Button saveButton = view.findViewById(R.id.settingsSaveButton);
        Button deleteButton = view.findViewById(R.id.settingsDeleteButton);

        deviceName.setText(userDevice.getDeviceSettings().getDeviceName());

        final SwitchCompat useFlashlight = view.findViewById(R.id.settingsEnableFlashlightSwitch);
        final SwitchCompat useVibration = view.findViewById(R.id.settingsEnableVibrationSwitch);
        final SwitchCompat useWifi = view.findViewById(R.id.settingsEnableWifiSwitch);
        final SwitchCompat overrideMaxVolume = view.findViewById(R.id.settingsOverrideMaxVolumeSwitch);
        final SeekBar overrideMaxVolumeValue = view.findViewById(R.id.settingsVolumeToUseSlider);
        useFlashlight.setChecked(userDevice.getDeviceSettings().getUseFlashlight());
        useVibration.setChecked(userDevice.getDeviceSettings().getUseVibrate());
        useWifi.setChecked(userDevice.getDeviceSettings().getShouldLimitToWifi());
        overrideMaxVolume.setChecked(userDevice.getDeviceSettings().getUseVolumeOverride());
        overrideMaxVolumeValue.setProgress(userDevice.getDeviceSettings().getOverriddenVolumeValue());

        deviceName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);

                    if (inputMethodManager != null) {
                        inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }

                    v.clearFocus();
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeviceSettings deviceSettings = userDevice.getDeviceSettings();
                deviceSettings.setAlexaUserId(userDevice.getAlexaUserId());
                deviceSettings.setDeviceId(userDevice.getDeviceId());
                deviceSettings.setDeviceName(deviceName.getText().toString());
                deviceSettings.setUseFlashlight(useFlashlight.isChecked());
                deviceSettings.setUseVibrate(useVibration.isChecked());
                deviceSettings.setShouldLimitToWifi(useWifi.isChecked());
                deviceSettings.setConfiguredWifiSsid(null);
                deviceSettings.setUseVolumeOverride(overrideMaxVolume.isChecked());
                deviceSettings.setOverriddenVolumeValue(overrideMaxVolumeValue.getProgress());

                //ManagementInterface managementService = ApiService.createManagementInstance();

                ManagementInterface managementService = ApiService.createInstance(EndpointType.MANAGEMENT);


                Call<Void> updateSettingsCall = managementService.saveDeviceSettings(deviceSettings);
                updateSettingsCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (!response.isSuccessful()) {
                            try {
                                Toast.makeText(getContext(), "Failed to save settings - " + response.errorBody().string(), Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getContext(), "Successfully saved settings" + response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        int b = 1;
                    }
                });
            }
        });

        Spinner wifiDropdown = (Spinner) view.findViewById(R.id.settingsWifiSsdDropdown);
        wifiDropdown.setEnabled(false);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, new String[]{"(Feature not yet available)"});
        wifiDropdown.setAdapter(adapter);
    }
}