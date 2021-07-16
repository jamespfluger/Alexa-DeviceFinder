package com.jamespfluger.devicefinder.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jamespfluger.devicefinder.R;
import com.jamespfluger.devicefinder.utilities.AmazonLoginHelper;

public class PermissionsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        NavController controller = Navigation.findNavController(this, R.id.permissions_fragment_container);
        controller.navigate(R.id.permissions_fragment);
    }

    @Override
    public void onBackPressed() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.quit)
                .setMessage(R.string.confirm_logout_question)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    AmazonLoginHelper.signOut(getApplicationContext());
                    switchToLoginActivity();
                })
                .setNegativeButton(R.string.no, null)
                .show();
    }

    private void switchToLoginActivity() {
        Intent newIntent = new Intent(this, LoginActivity.class);
        startActivity(newIntent);
        finish();
        overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
    }
}
