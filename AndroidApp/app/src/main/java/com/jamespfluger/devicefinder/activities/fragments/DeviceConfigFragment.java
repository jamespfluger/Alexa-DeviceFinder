package com.jamespfluger.devicefinder.activities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import com.amazon.identity.auth.device.AuthError;
import com.amazon.identity.auth.device.api.Listener;
import com.amazon.identity.auth.device.api.authorization.AuthorizationManager;
import com.jamespfluger.devicefinder.R;
import com.jamespfluger.devicefinder.activities.LoginActivity;
import com.jamespfluger.devicefinder.api.ApiService;
import com.jamespfluger.devicefinder.api.ManagementInterface;
import com.jamespfluger.devicefinder.databinding.FragmentDeviceConfigBinding;
import com.jamespfluger.devicefinder.models.Device;
import com.jamespfluger.devicefinder.settings.ConfigManager;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class DeviceConfigFragment extends Fragment {
    private final Device device;

    public DeviceConfigFragment(Device device) {
        this.device = device;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentDeviceConfigBinding binding = FragmentDeviceConfigBinding.inflate(inflater, container, false);
        binding.setDevice(device);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        final EditText deviceName = view.findViewById(R.id.settingsDeviceNameField);
        deviceName.setText(device.getDeviceName());
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

        final Button saveButton = view.findViewById(R.id.settingsSaveButton);
        final Button deleteButton = view.findViewById(R.id.settingsDeleteButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                getActivity().findViewById(R.id.settingsSaveWaitPanel).setVisibility(View.VISIBLE);

                ManagementInterface managementService = ApiService.createInstance();

                Call<Void> updateSettingsCall = managementService.updateDevice(device, ConfigManager.getAlexaUserIdConfig(), device.getDeviceId());
                updateSettingsCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Successfully saved settings" + response.message(), Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                String errorMessage = response.errorBody() != null ? response.errorBody().string() : String.format("Unknown error (%s)", response.code());
                                Toast.makeText(getContext(), "Failed to save settings - " + errorMessage, Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        getActivity().findViewById(R.id.settingsSaveWaitPanel).setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        getActivity().findViewById(R.id.settingsSaveWaitPanel).setVisibility(View.GONE);
                    }
                });
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthorizationManager.signOut(getContext(), new Listener<Void, AuthError>() {
                    @Override
                    public void onSuccess(Void response) {
                        switchToActivity(LoginActivity.class);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Signed out", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onError(AuthError authError) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "Failed to sign out", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

        Spinner wifiDropdown = view.findViewById(R.id.settingsWifiSsdDropdown);
        wifiDropdown.setEnabled(false);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, new String[]{"(Feature not yet available)"});
        wifiDropdown.setAdapter(adapter);
    }

    private void switchToActivity(Class<?> newActivity) {
        Intent newIntent = new Intent(getActivity(), newActivity);
        startActivity(newIntent);
    }
}
