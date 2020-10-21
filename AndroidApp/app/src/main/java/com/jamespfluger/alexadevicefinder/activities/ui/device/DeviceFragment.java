package com.jamespfluger.alexadevicefinder.activities.ui.device;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.jamespfluger.alexadevicefinder.R;
import com.jamespfluger.alexadevicefinder.models.UserDevice;

public class DeviceFragment extends Fragment {
    private DeviceViewModel deviceViewModel;
    private UserDevice userDevice;

    public DeviceFragment(UserDevice userDevice){
        this.userDevice = userDevice;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_device_config, container, false);
        deviceViewModel = ViewModelProviders.of(this).get(DeviceViewModel.class);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        EditText deviceName = view.findViewById(R.id.deviceConfigDeviceNameField);
        deviceName.setText(userDevice.getDeviceName());
    }
}