package com.jamespfluger.devicefinder.notifications;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.jamespfluger.devicefinder.utilities.PreferencesManager;

public class FirebaseService extends FirebaseMessagingService {
    private PreferencesManager preferencesManager;
    private NotificationForge notificationForge;
    private Context context;

    public FirebaseService(Context context) {
        this.context = context;
        this.preferencesManager = new PreferencesManager(context);
    }

    public FirebaseService() {
    }

    @Override
    public void onNewToken(@NonNull String newToken) {
        super.onNewToken(newToken);
        preferencesManager.setDeviceId(newToken);
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
                        //TODO: handle instance where task istself is null
                        //TODO: make sure the deviceis actually online
                        InstanceIdResult taskResult = task.getResult();

                        if (taskResult != null) {
                            String newToken = taskResult.getToken();
                            preferencesManager.setDeviceId(newToken);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error receiving Firebase token.\nPlease log in again", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
