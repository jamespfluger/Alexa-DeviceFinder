package com.jamespfluger.alexadevicefinder;

import android.content.Context;
import android.widget.Toast;

public class CommonTools {

    public static void ShowToast(Context applicationContext, String toastMessage){
        Toast toast = Toast.makeText(applicationContext, toastMessage, Toast.LENGTH_SHORT);
        toast.show();
    }
}
