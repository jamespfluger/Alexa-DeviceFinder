package com.jamespfluger.devicefinder.utilities;

import android.util.Log;

public class Logger {
    private static final String TAG = "DEVICEFINDER";

    public static void Log(String message) {
        Log.i(TAG, message);
    }

    public static void Log(String message, LogLevel logLevel) {
        if (logLevel == LogLevel.Verbose) {
            Log.v(TAG, message);
        } else if (logLevel == LogLevel.Debug) {
            Log.d(TAG, message);
        } else if (logLevel == LogLevel.Information) {
            Log.i(TAG, message);
        } else if (logLevel == LogLevel.Warning) {
            Log.w(TAG, message);
        } else if (logLevel == LogLevel.Error) {
            Log.e(TAG, message);
        } else if (logLevel == LogLevel.Wtf) {
            Log.wtf(TAG, message);
        }
    }

    public enum LogLevel {
        Verbose,
        Debug,
        Information,
        Warning,
        Error,
        Wtf
    }
}
