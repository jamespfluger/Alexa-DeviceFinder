package com.jamespfluger.devicefinder.controls;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.SwitchCompat;
import androidx.databinding.DataBindingUtil;

import com.jamespfluger.devicefinder.R;
import com.jamespfluger.devicefinder.databinding.ComponentSettingsViewBinding;

public class SettingsView extends LinearLayout {
    public SwitchCompat settingsSwitch;
    private ComponentSettingsViewBinding binding;

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
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DataBindingUtil.inflate(inflater, R.layout.component_settings_view, this, true);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.SettingsView);

        TextView settingsTitle = findViewById(R.id.settings_title);
        TextView settingsSubtitle = findViewById(R.id.settings_subtitle);
        settingsSwitch = findViewById(R.id.settings_switch);

        settingsTitle.setText(attributes.getString(R.styleable.SettingsView_settingsTitle));
        settingsSubtitle.setText(attributes.getString(R.styleable.SettingsView_settingsSubtitle));

        attributes.recycle();

        setPaddingRelative(80, 80, 55, 0);
        setGravity(Gravity.CENTER | Gravity.TOP);
        setOrientation(HORIZONTAL);
    }
}
