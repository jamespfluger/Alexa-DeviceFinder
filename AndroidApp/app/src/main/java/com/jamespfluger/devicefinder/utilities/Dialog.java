package com.jamespfluger.devicefinder.utilities;

import android.content.Context;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jamespfluger.devicefinder.R;

public class Dialog {
    public static void ShowInformation(Context context, String title, String content) {
        new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(content)
                .setPositiveButton(R.string.ok, null)
                .show();
    }

    public static void ShowInformation(Context context, int title, int content) {
        Dialog.ShowInformation(context, context.getString(title), context.getString(content));
    }
}
