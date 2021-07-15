package com.jamespfluger.devicefinder.controls;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;

import com.jamespfluger.devicefinder.R;

public class PermissionsView extends LinearLayout {
    private ImageButton permissionStatusButton;

    public PermissionsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public PermissionsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    public void updatePermissionStatus(boolean isEnabled) {
        Drawable statusIcon;

        if (isEnabled) {
            statusIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_positive, getContext().getTheme());
        } else {
            statusIcon = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_alert, getContext().getTheme());
        }

        permissionStatusButton.setImageDrawable(statusIcon);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.PermissionsView);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DataBindingUtil.inflate(inflater, R.layout.component_permissions_view, this, true);

        final TextView explanationTextView = findViewById(R.id.permissions_explanation);
        final Button expandExplanationButton = findViewById(R.id.permissions_expand_button);
        final ImageButton expandExplanationArrpw = findViewById(R.id.permissions_expand_arrow);
        permissionStatusButton = findViewById(R.id.permissions_status_button);
        Button grantPermissionButton = findViewById(R.id.permissions_grant_button);

        String permissionsTitle = attributes.getString(R.styleable.PermissionsView_permissionsTitle);
        String permissionsExplanation = attributes.getString(R.styleable.PermissionsView_permissionsExplanation);

        grantPermissionButton.setText(permissionsTitle);
        explanationTextView.setText(permissionsExplanation);

        expandExplanationButton.setOnClickListener(v -> {
            if (explanationTextView.getVisibility() == GONE) {
                explanationTextView.setVisibility(VISIBLE);
            } else {
                explanationTextView.setVisibility(GONE);
            }
        });
        expandExplanationArrpw.setOnClickListener(v -> {
            if (explanationTextView.getVisibility() == GONE) {
                explanationTextView.setVisibility(VISIBLE);
            } else {
                explanationTextView.setVisibility(GONE);
            }
        });

        int permissionToGrant = attributes.getInt(R.styleable.PermissionsView_permissionToGrant, -1);

        if (permissionToGrant == 0) {
            grantPermissionButton.setOnClickListener(v -> {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                    v.getContext().startActivity(intent);
                }
            });
        } else if (permissionToGrant == 1) {
            grantPermissionButton.setOnClickListener(v -> {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                    v.getContext().startActivity(intent);
                }
            });
        }

        attributes.recycle();
    }
}
