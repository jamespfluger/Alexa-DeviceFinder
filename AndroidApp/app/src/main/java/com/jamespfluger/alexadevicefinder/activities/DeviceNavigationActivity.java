package com.jamespfluger.alexadevicefinder.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.jamespfluger.alexadevicefinder.R;
import com.jamespfluger.alexadevicefinder.activities.ui.about.AboutFragment;
import com.jamespfluger.alexadevicefinder.activities.ui.device.DeviceFragment;
import com.jamespfluger.alexadevicefinder.activities.ui.home.HomeFragment;

import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.HashSet;

public class DeviceNavigationActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private HashSet<Integer> uniqueDevices = new HashSet<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        }
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
}