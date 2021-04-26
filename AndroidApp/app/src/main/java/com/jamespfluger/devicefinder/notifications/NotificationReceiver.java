package com.jamespfluger.devicefinder.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Vibrator;

import androidx.annotation.RequiresApi;

import com.jamespfluger.devicefinder.settings.SettingsManager;
import com.jamespfluger.devicefinder.utilities.Logger;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (SettingsManager.getUseOnWifiOnlySetting() && !isWifiConnected(context)) {
            return;
        }

        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxRingVolume = manager.getStreamMaxVolume(AudioManager.STREAM_RING);
        maxRingVolume = (int) Math.ceil(maxRingVolume * 0.001);
        manager.setStreamVolume(AudioManager.STREAM_RING, maxRingVolume, 0);

        if (SettingsManager.getUseFlashlightSetting() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            startFlashlight(context);
        }

        if (SettingsManager.getUseVibrateSetting() == Boolean.TRUE) {
            startVibrate(context);
            final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.cancel();
        }
    }

    private boolean isWifiConnected(Context context) {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return networkInfo == null || networkInfo.isConnected();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startFlashlight(Context context) {
        final CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    boolean isFlashOn = true;

                    for (int i = 0; i < 10; i++) {
                        final String cameraId = cameraManager.getCameraIdList()[0];
                        cameraManager.setTorchMode(cameraId, isFlashOn);

                        isFlashOn = !isFlashOn;

                        Thread.sleep(500);
                    }
                } catch (CameraAccessException cameraAccessException) {
                    Logger.Log("Camera access unavailable: " + cameraAccessException.getLocalizedMessage());
                } catch (InterruptedException interruptedException) {
                    Logger.Log("Sleep during camera flash was interrupted: " + interruptedException.getLocalizedMessage());
                }
            }
        });
    }

    private void startVibrate(final Context context) {
        final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                long[] pattern = new long[]{0, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000};
                vibrator.vibrate(pattern, -1);
            }
        });
    }
}
