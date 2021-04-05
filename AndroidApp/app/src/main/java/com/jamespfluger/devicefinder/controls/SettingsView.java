package com.jamespfluger.devicefinder.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;

import com.jamespfluger.devicefinder.R;

public class SettingsView extends LinearLayout {
    private SwitchCompat settingsSwitch;

    public SettingsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SettingsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    public boolean isChecked() {
        return settingsSwitch.isChecked();
    }

    public void setChecked(boolean checked) {
        settingsSwitch.setChecked(checked);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.component_settings_view, this);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SettingsView);

        TextView settingsTitle = findViewById(R.id.settingsTitle);
        TextView settingsSubtitle = findViewById(R.id.settingsSubtitle);
        settingsSwitch = findViewById(R.id.settingsSwitch);

        settingsTitle.setText(attributes.getString(R.styleable.SettingsView_settingsTitle));
        settingsSubtitle.setText(attributes.getString(R.styleable.SettingsView_settingsSubtitle));

        attributes.recycle();

        setPaddingRelative(80, 80, 55, 0);
        setGravity(Gravity.CENTER | Gravity.TOP);
        setOrientation(HORIZONTAL);
    }
}
