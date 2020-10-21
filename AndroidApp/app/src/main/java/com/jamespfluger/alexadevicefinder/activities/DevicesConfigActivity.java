package com.jamespfluger.alexadevicefinder.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.jamespfluger.alexadevicefinder.R;
import com.jamespfluger.alexadevicefinder.activities.ui.about.AboutFragment;
import com.jamespfluger.alexadevicefinder.activities.ui.device.DeviceFragment;
import com.jamespfluger.alexadevicefinder.activities.ui.home.HomeFragment;
import com.jamespfluger.alexadevicefinder.auth.ManagementInterface;
import com.jamespfluger.alexadevicefinder.models.UserDevice;
import com.jamespfluger.alexadevicefinder.utilities.PreferencesManager;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.internal.EverythingIsNonNull;

public class DevicesConfigActivity extends AppCompatActivity {
    private Retrofit retrofitEntity = new Retrofit.Builder()
            .baseUrl("https://qsbrgmx8u1.execute-api.us-west-2.amazonaws.com/prd/devicefinder/management/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private ManagementInterface managementApi = retrofitEntity.create(ManagementInterface.class);
    private AppBarConfiguration mAppBarConfiguration;
    private PreferencesManager preferencesManager;
    private ArrayList<UserDevice> allUserDevices = new ArrayList<UserDevice>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesManager = new PreferencesManager(getApplicationContext());

        setContentView(R.layout.activity_device_navigation);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);

        // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home).setOpenableLayout(drawer).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Create the normal menu items
        final Menu menu = navigationView.getMenu();
        createDefaultMenuItems(drawer, menu);
        /* TODO:
            - Load all devices from API into global list
            - Somehow associate menu items with IDs
            - Pass device into fragments
         */

        getDevices();

        /*
        for (int i=0; i<3; i++) {
            MenuItem newItem = menu.add(R.id.devicesGroup, View.generateViewId(), Menu.NONE, "Runtime item #" + i);
            newItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.nav_host_fragment, new DeviceFragment(), "").commit();
                    clearAllChecks(menu);
                    item.setChecked(true);
                    drawer.close();
                    return true;
                }
            });
        }*/
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

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)  || super.onSupportNavigateUp();
    }

    private void getDevices(){
        Call<ArrayList<UserDevice>> userCall = managementApi.getAllUserDevices(preferencesManager.getUserId());
        userCall.enqueue(new Callback<ArrayList<UserDevice>>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()){
                    allUserDevices = (ArrayList<UserDevice>)response.body();
                    populateDeviceList();
                }
                else{
                    try {
                        String errorMessage = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Toast.makeText(DevicesConfigActivity.this, response.code() + " - " + errorMessage, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            @EverythingIsNonNull
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    private void createDefaultMenuItems(final DrawerLayout drawer, final Menu menu) {
        MenuItem homeItem = menu.add(R.id.defaultGroup, View.generateViewId(), Menu.NONE, "Home");
        MenuItem aboutItem = menu.add(R.id.defaultGroup, View.generateViewId(), Menu.NONE, "About");

        homeItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, new HomeFragment(), "").commit();
                clearAllChecks(menu);
                item.setChecked(true);
                drawer.close();
                return true;
            }
        });

        aboutItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, new AboutFragment(), "").commit();
                clearAllChecks(menu);
                item.setChecked(true);
                drawer.close();
                return true;
            }
        });
    }

    private void clearAllChecks(Menu menu) {
        for (int i=0; i<menu.size(); i++) {
            menu.getItem(i).setChecked(false);
        }
    }

    private void populateDeviceList(){
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        final Menu menu = navigationView.getMenu();

        for(final UserDevice device : allUserDevices){
            MenuItem newDeviceMenuItem = menu.add(R.id.devicesGroup, View.generateViewId(), Menu.NONE, device.getDeviceName());

            newDeviceMenuItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.nav_host_fragment, new DeviceFragment(device), "").commit();
                    clearAllChecks(menu);
                    item.setChecked(true);
                    drawer.close();
                    return true;
                }
            });
        }
    }
}