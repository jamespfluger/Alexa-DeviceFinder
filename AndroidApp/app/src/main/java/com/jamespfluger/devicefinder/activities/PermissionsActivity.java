package com.jamespfluger.devicefinder.activities;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.jamespfluger.devicefinder.R;
import com.jamespfluger.devicefinder.activities.fragments.PermissionsFragment;

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
                .setTitle(R.string.quit)
                .setMessage(R.string.confirm_quit_question)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PermissionsActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }
}
