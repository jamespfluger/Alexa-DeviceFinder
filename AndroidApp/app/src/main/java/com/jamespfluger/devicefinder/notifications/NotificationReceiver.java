package com.jamespfluger.devicefinder.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Vibrator;

import androidx.annotation.RequiresApi;

import com.jamespfluger.devicefinder.R;
import com.jamespfluger.devicefinder.settings.SettingsManager;
import com.jamespfluger.devicefinder.utilities.Logger;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxRingVolume = manager.getStreamMaxVolume(AudioManager.STREAM_RING);
        maxRingVolume = (int) Math.round(maxRingVolume * (SettingsManager.getVolumeOverrideValue() / 100.0));

        manager.setStreamVolume(AudioManager.STREAM_RING, maxRingVolume, 0);

        if (SettingsManager.getUseVibrate()) {
            startVibrate(context);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && SettingsManager.getUseFlashlight()) {
            startFlashlight(context);
        }
    }

    private void startVibrate(final Context context) {
        final Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

        long[] pattern = new long[]{0, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000};

        vibrator.vibrate(pattern, -1,
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build());
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startFlashlight(Context context) {
        final CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);

        AsyncTask.execute(() -> {
            try {
                boolean isFlashOn = true;

                for (int i = 0; i < 10; i++) {
                    final String cameraId = cameraManager.getCameraIdList()[0];
                    cameraManager.setTorchMode(cameraId, isFlashOn);
                    isFlashOn = !isFlashOn;

                    Thread.sleep(500);
                }
            } catch (CameraAccessException cameraAccessException) {
                Logger.log(context.getString(R.string.camera_unavailable_error_log) + cameraAccessException.getLocalizedMessage());
            } catch (InterruptedException interruptedException) {
                Logger.log(context.getString(R.string.camera_interrupted_error_log) + interruptedException.getLocalizedMessage());
            }
        });
    }

}
