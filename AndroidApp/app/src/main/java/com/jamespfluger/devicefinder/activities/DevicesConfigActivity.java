package com.jamespfluger.devicefinder.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.jamespfluger.devicefinder.R;
import com.jamespfluger.devicefinder.activities.fragments.AboutFragment;
import com.jamespfluger.devicefinder.activities.fragments.DeviceConfigFragment;
import com.jamespfluger.devicefinder.activities.fragments.DeviceConfigFragmentDirections;
import com.jamespfluger.devicefinder.activities.fragments.PermissionsFragment;
import com.jamespfluger.devicefinder.api.ApiService;
import com.jamespfluger.devicefinder.api.ManagementInterface;
import com.jamespfluger.devicefinder.models.Device;
import com.jamespfluger.devicefinder.settings.ConfigManager;
import com.jamespfluger.devicefinder.settings.SettingsManager;
import com.jamespfluger.devicefinder.utilities.AmazonLoginHelper;
import com.jamespfluger.devicefinder.utilities.DeviceManager;
import com.jamespfluger.devicefinder.utilities.LogLevel;
import com.jamespfluger.devicefinder.utilities.Logger;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class DevicesConfigActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_device_config);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        final DrawerLayout drawer = findViewById(R.id.deviceNavigationActivityLayout);
        final NavigationView navigationView = findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);

        // Each fragment is a top-level destination
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.about_fragment, R.id.permissions_fragment, R.id.device_config_fragment).setOpenableLayout(drawer).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        initializeSidebar();
    }

    public void initializeSidebar() {
        // Create the normal menu items
        final NavigationView navigationView = findViewById(R.id.nav_view);
        final DrawerLayout drawer = findViewById(R.id.deviceNavigationActivityLayout);
        final Menu menu = navigationView.getMenu();
        menu.clear();
        createDefaultMenuItems(drawer, menu);
        getAndPopulateSidebarDevices();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.kebab_menu, menu);
        menu.getItem(0).setOnMenuItemClickListener(item -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.confirm_logout_and_delete_title)
                    .setIcon(R.drawable.ic_caution)
                    .setMessage(R.string.confirm_logout_and_delete_question)
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        Call<Void> deleteDeviceCall = ApiService.getInstance().deleteDevice(ConfigManager.getAlexaUserId(), ConfigManager.getDeviceId());
                        deleteDeviceCall.enqueue(new Callback<Void>() {
                            @Override
                            @EverythingIsNonNull
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    DeviceManager.removeDevice(ConfigManager.getDeviceId());
                                    Toast.makeText(getApplicationContext(), getString(R.string.settings_delete_toast) + SettingsManager.getDeviceName(), Toast.LENGTH_SHORT).show();
                                    AmazonLoginHelper.signOut(getApplicationContext());
                                    ConfigManager.reset();
                                    SettingsManager.reset();
                                    switchToLoginActivity();
                                } else {
                                    try {
                                        String errorMessage = response.errorBody() != null ? response.errorBody().string() : String.format(getString(R.string.unknown_error_http_message), response.code());
                                        Toast.makeText(getApplicationContext(), getString(R.string.settings_delete_error_toast) + errorMessage, Toast.LENGTH_LONG).show();
                                    } catch (IOException e) {
                                        Logger.Log(e, LogLevel.Error);
                                    }
                                }
                            }

                            @Override
                            @EverythingIsNonNull
                            public void onFailure(Call<Void> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                Logger.Log(t.getLocalizedMessage(), LogLevel.Error);
                            }
                        });
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
            return true;
        });
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration) || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.quit)
                .setMessage(R.string.confirm_quit_question)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    DevicesConfigActivity.super.onBackPressed();
                    finish();
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    public void getAndPopulateSidebarDevices() {
        ManagementInterface managementApi = ApiService.getInstance();
        Call<ArrayList<Device>> userCall = managementApi.getAllDevices(ConfigManager.getAlexaUserId());
        userCall.enqueue(new Callback<ArrayList<Device>>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful() && response.body() instanceof ArrayList) {
                    DeviceManager.setDevices((ArrayList<Device>) response.body());
                    populateSidebarDevices();
                } else {
                    try {
                        String errorMessage = response.errorBody() != null ? response.errorBody().string() : String.format(getString(R.string.unknown_error_http_message), response.code());
                        Toast.makeText(DevicesConfigActivity.this, getString(R.string.unable_to_load_devices) + errorMessage, Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Toast.makeText(DevicesConfigActivity.this, getString(R.string.unable_to_load_devices) + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createDefaultMenuItems(final DrawerLayout drawer, final Menu menu) {
        MenuItem aboutItem = menu.add(R.id.default_group, View.generateViewId(), Menu.NONE, R.string.about);
        MenuItem permissionsItem = menu.add(R.id.default_group, View.generateViewId(), Menu.NONE, R.string.permissions);

        aboutItem.setCheckable(true);
        aboutItem.setChecked(true);
        permissionsItem.setCheckable(true);

        aboutItem.setOnMenuItemClickListener(buildMenuItemClickListener(new AboutFragment(), null));
        permissionsItem.setOnMenuItemClickListener(buildMenuItemClickListener(new PermissionsFragment(), null));

        MenuItem temporaryItem = menu.add(R.id.devices_group, View.generateViewId(), 2, "(Loading Devices)");
        temporaryItem.setEnabled(false);
        temporaryItem.setCheckable(false);
    }

    private void populateSidebarDevices() {
        final NavigationView navigationView = findViewById(R.id.nav_view);
        final Menu menu = navigationView.getMenu();

        menu.removeGroup(R.id.devices_group);

        for (final Device device : DeviceManager.getDevices()) {
            MenuItem newDeviceMenuItem;

            if (device.getDeviceId().equals(ConfigManager.getDeviceId())) {
                newDeviceMenuItem = menu.add(R.id.devices_group, View.generateViewId(), Menu.FIRST, device.getDeviceName());
                newDeviceMenuItem.setIcon(R.drawable.ic_person);
            } else {
                newDeviceMenuItem = menu.add(R.id.devices_group, View.generateViewId(), Menu.NONE, device.getDeviceName());
            }

            newDeviceMenuItem.setOnMenuItemClickListener(buildMenuItemClickListener(new DeviceConfigFragment(), device.getDeviceId()));
            newDeviceMenuItem.setCheckable(true);
        }
    }

    private MenuItem.OnMenuItemClickListener buildMenuItemClickListener(final Fragment newFragment, String deviceId) {
        final DrawerLayout drawer = findViewById(R.id.deviceNavigationActivityLayout);

        return item -> {
            item.setChecked(true);
            drawer.close();

            NavDirections directions = getDirections(newFragment, deviceId);
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

            if (navHostFragment != null) {
                NavController navController = navHostFragment.getNavController();
                navController.navigate(directions);
            } else {
                Logger.Log(getString(R.string.error_navigation_log), LogLevel.Error);
                runOnUiThread(() -> Toast.makeText(getApplicationContext(), R.string.error_navigation_please_restart, Toast.LENGTH_SHORT).show());
            }

            return true;
        };
    }

    public void navigateToDefaultFragment() {
        NavDirections directions = DeviceConfigFragmentDirections.toAbout();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            navController.navigate(directions);
        } else {
            Logger.Log(getString(R.string.error_navigation_log), LogLevel.Error);
            runOnUiThread(() -> Toast.makeText(getApplicationContext(), R.string.error_navigation_please_restart, Toast.LENGTH_SHORT).show());
        }
    }

    private NavDirections getDirections(Fragment destinationFragment, String deviceId) {
        if (destinationFragment instanceof DeviceConfigFragment) {
            return DeviceConfigFragmentDirections.toDeviceConfig(deviceId);
        } else if (destinationFragment instanceof AboutFragment) {
            return DeviceConfigFragmentDirections.toAbout();
        } else {
            return DeviceConfigFragmentDirections.toPermissions();
        }
    }

    public void switchToLoginActivity() {
        Intent newIntent = new Intent(this, LoginActivity.class);
        startActivity(newIntent);
        finish();
        overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
    }
}
