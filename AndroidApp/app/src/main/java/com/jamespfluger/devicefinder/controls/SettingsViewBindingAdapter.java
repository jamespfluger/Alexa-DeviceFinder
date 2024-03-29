package com.jamespfluger.devicefinder.controls;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.InverseBindingMethod;
import androidx.databinding.InverseBindingMethods;

@InverseBindingMethods({@InverseBindingMethod(type = SettingsView.class, attribute = "android:checked"),})
public class SettingsViewBindingAdapter {
    @BindingAdapter("android:checked")
    public static void setChecked(SettingsView view, boolean checked) {
        view.setChecked(checked);
    }

    @BindingAdapter(value = {"android:checkedAttrChanged"}, requireAll = false)
    public static void setListeners(SettingsView view, final InverseBindingListener attrChange) {
        view.settingsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> attrChange.onChange());
    }
}
