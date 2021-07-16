package com.jamespfluger.devicefinder.activities;

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
import com.jamespfluger.devicefinder.activities.fragments.DeviceConfigFragmentArgs;
import com.jamespfluger.devicefinder.activities.fragments.DeviceConfigFragmentDirections;
import com.jamespfluger.devicefinder.activities.fragments.PermissionsFragment;
import com.jamespfluger.devicefinder.api.ApiService;
import com.jamespfluger.devicefinder.api.ManagementInterface;
import com.jamespfluger.devicefinder.models.Device;
import com.jamespfluger.devicefinder.settings.ConfigManager;
import com.jamespfluger.devicefinder.utilities.LogLevel;
import com.jamespfluger.devicefinder.utilities.Logger;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DevicesConfigActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;

    private ArrayList<Device> allDevices = new ArrayList<Device>();

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

        // Create the normal menu items
        final Menu menu = navigationView.getMenu();
        createDefaultMenuItems(drawer, menu);
        getDevices();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.kebab_menu, menu);
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
                .setPositiveButton(R.string.yes, (dialog, which) -> DevicesConfigActivity.super.onBackPressed())
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private void getDevices() {
        ManagementInterface managementApi = ApiService.createInstance();
        Call<ArrayList<Device>> userCall = managementApi.getAllDevices(ConfigManager.getAlexaUserIdConfig());
        userCall.enqueue(new Callback<ArrayList<Device>>() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful() && response.body() instanceof ArrayList) {
                    allDevices = (ArrayList<Device>) response.body();
                    populateDeviceList();
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

            }
        });
    }

    private void createDefaultMenuItems(final DrawerLayout drawer, final Menu menu) {
        MenuItem aboutItem = menu.add(R.id.default_group, View.generateViewId(), Menu.NONE, R.string.about);
        MenuItem permissionsItem = menu.add(R.id.default_group, View.generateViewId(), Menu.NONE, R.string.permissions);

        aboutItem.setOnMenuItemClickListener(buildMenuItemClickListener(new AboutFragment(), null));
        permissionsItem.setOnMenuItemClickListener(buildMenuItemClickListener(new PermissionsFragment(), null));
    }

    private void clearAllChecks(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            menu.getItem(i).setChecked(false);
        }
    }

    private void populateDeviceList() {
        final NavigationView navigationView = findViewById(R.id.nav_view);
        final Menu menu = navigationView.getMenu();

        for (final Device device : allDevices) {
            MenuItem newDeviceMenuItem = menu.add(R.id.devicesGroup, View.generateViewId(), Menu.NONE, device.getDeviceName());
            newDeviceMenuItem.setOnMenuItemClickListener(buildMenuItemClickListener(new DeviceConfigFragment(), device));
        }
    }

    private MenuItem.OnMenuItemClickListener buildMenuItemClickListener(final Fragment newFragment, Device device) {
        final DrawerLayout drawer = findViewById(R.id.deviceNavigationActivityLayout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        final Menu menu = navigationView.getMenu();

        return item -> {
            clearAllChecks(menu);
            item.setChecked(true);
            drawer.close();

            NavDirections directions = getDirections(newFragment, device);
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

    private NavDirections getDirections(Fragment destinationFragment, Device device) {
        if (destinationFragment instanceof DeviceConfigFragment) {
            return DeviceConfigFragmentDirections.toDeviceConfig(device);
        } else if (destinationFragment instanceof AboutFragment) {
            return DeviceConfigFragmentDirections.toAbout();
        } else {
            return DeviceConfigFragmentDirections.toPermissions();
        }
    }
}
