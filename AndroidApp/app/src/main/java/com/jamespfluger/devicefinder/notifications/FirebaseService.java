package com.jamespfluger.devicefinder.notifications;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jamespfluger.devicefinder.R;
import com.jamespfluger.devicefinder.settings.ConfigManager;
import com.jamespfluger.devicefinder.utilities.Logger;

public class FirebaseService extends FirebaseMessagingService {
    private NotificationForge notificationForge;

    public FirebaseService() {
    }

    @Override
    public void onNewToken(@NonNull String newToken) {
        super.onNewToken(newToken);
        ConfigManager.setFirebaseTokenConfig(newToken);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (notificationForge == null)
            notificationForge = new NotificationForge(getApplicationContext());

        notificationForge.issueNotification(remoteMessage);
    }

    public void refreshToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        InstanceIdResult taskResult = task.getResult();

                        if (taskResult != null) {
                            String newToken = taskResult.getToken();
                            ConfigManager.setFirebaseTokenConfig(newToken);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception ex) {
                        Logger.Log(getString(R.string.firebase_refresh_error_log) + ex.toString());
                        Toast.makeText(getApplicationContext(), getString(R.string.firebase_refresh_error_toast) + ex.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
