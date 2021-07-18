package com.jamespfluger.devicefinder.utilities;

import android.util.Log;

public class Logger {
    private static final String TAG = "DEVICE_FINDER_LOGS";

    public static void Log(Object message) {
        Log(message, LogLevel.Information);
    }

    public static void Log(Object message, LogLevel logLevel) {
        if (logLevel == LogLevel.Verbose) {
            Log.v(TAG, message.toString());
        } else if (logLevel == LogLevel.Debug) {
            Log.d(TAG, message.toString());
        } else if (logLevel == LogLevel.Information) {
            Log.i(TAG, message.toString());
        } else if (logLevel == LogLevel.Warning) {
            Log.w(TAG, message.toString());
        } else if (logLevel == LogLevel.Error) {
            Log.e(TAG, message.toString());
        } else if (logLevel == LogLevel.Wtf) {
            Log.wtf(TAG, message.toString());
        }
    }
}
