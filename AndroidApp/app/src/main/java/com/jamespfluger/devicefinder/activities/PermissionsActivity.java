package com.jamespfluger.devicefinder.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.jamespfluger.devicefinder.R;
import com.jamespfluger.devicefinder.activities.fragments.PermissionsFragment;
import com.jamespfluger.devicefinder.controls.PermissionsView;

public class PermissionsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment permissionsFragment = new PermissionsFragment();
        ft.replace(R.id.permissionsActivityFragment, permissionsFragment).commit();
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Quit")
                .setMessage("Are you sure you want to quit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    PermissionsActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
