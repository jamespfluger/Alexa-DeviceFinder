package com.jamespfluger.devicefinder.controls;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.opengl.Visibility;
import android.provider.Settings;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.jamespfluger.devicefinder.R;

public class PermissionsView extends LinearLayout {
    private ImageButton permissionStatus;

    public PermissionsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public PermissionsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    public ImageButton getPermissionStatusButton() {
        return permissionStatus;
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.PermissionsView);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DataBindingUtil.inflate(inflater, R.layout.component_permissions_view, this, true);

        final Button allowButton = findViewById(R.id.permissionAllowButton);
        final TextView explanationTextView = findViewById(R.id.permissionExplanation);
        final Button expandExplanationButton = findViewById(R.id.permissionExpandButton);
        permissionStatus = findViewById(R.id.permissionStatusIcon);

        String permissionsTitle = attributes.getString(R.styleable.PermissionsView_permissionsTitle);
        String permissionsExplanation = attributes.getString(R.styleable.PermissionsView_permissionsExplanation);

        allowButton.setText(permissionsTitle);
        explanationTextView.setText(permissionsExplanation);

        expandExplanationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (explanationTextView.getVisibility() == GONE)
                    explanationTextView.setVisibility(VISIBLE);
                else
                    explanationTextView.setVisibility(GONE);
            }
        });

        if (permissionsTitle != null && permissionsTitle.equals("Disable Battery Optimization")) {
            allowButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }
        else {
            allowButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        Intent intent = new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
                        v.getContext().startActivity(intent);
                    }
                }
            });
        }

        attributes.recycle();

        setOrientation(HORIZONTAL);
    }
}