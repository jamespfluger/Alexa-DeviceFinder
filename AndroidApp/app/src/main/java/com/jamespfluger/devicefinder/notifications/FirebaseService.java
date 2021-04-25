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
import com.jamespfluger.devicefinder.utilities.Logger;
import com.jamespfluger.devicefinder.utilities.UserManager;

public class FirebaseService extends FirebaseMessagingService {
    private final UserManager userManager;
    private NotificationForge notificationForge;

    public FirebaseService() {
        this.userManager = new UserManager(getApplicationContext());
    }

    @Override
    public void onNewToken(@NonNull String newToken) {
        super.onNewToken(newToken);
        userManager.setFirebaseToken(newToken);
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
                            userManager.setFirebaseToken(newToken);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception ex) {
                        Logger.Log("Failed to refresh FirebaseToken with exception: " + ex.toString());
                        Toast.makeText(getApplicationContext(), "Error receiving Firebase token.\n" + ex.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
