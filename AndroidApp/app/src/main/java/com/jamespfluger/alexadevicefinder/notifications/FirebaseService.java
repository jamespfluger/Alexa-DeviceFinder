package com.jamespfluger.alexadevicefinder.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class FirebaseService extends FirebaseMessagingService {
    private NotificationForge notificationForge;
    private Context context;
    private final String sharedPrefsName = "com.jamespfluger.alexadevicefinder.SHARED_PREFERENCES";

    public FirebaseService(Context context) {
        this.context = context;
    }

    public FirebaseService() {
    }

    @Override
    public void onNewToken(String newToken) {
        super.onNewToken(newToken);
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(sharedPrefsName, Context.MODE_PRIVATE);
        sharedPref.edit().putString("firebasetoken", newToken).apply();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (notificationForge == null)
            notificationForge = new NotificationForge(getApplicationContext());

        notificationForge.issueNotification(remoteMessage);
    }

    public String getToken() {
        SharedPreferences sharedPref = context.getSharedPreferences(sharedPrefsName, Context.MODE_PRIVATE);
        String firebaseToken = sharedPref.getString("firebasetoken", null);
        if (firebaseToken != null) {
            return firebaseToken;
        } else {
            refreshToken();
            return sharedPref.getString("firebasetoken", null);
        }
    }

    public void refreshToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        String newToken = task.getResult().getToken();
                        SharedPreferences sharedPref = context.getSharedPreferences(sharedPrefsName, Context.MODE_PRIVATE);
                        sharedPref.edit().putString("firebasetoken", newToken).apply();
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
