using System;
using System.IO;
using AlexaDeviceFinderSkill.Models;
using FirebaseAdmin;
using FirebaseAdmin.Messaging;
using Google.Apis.Auth.OAuth2;

namespace AlexaDeviceFinderSkill
{
    public class FirebaseUtil
    {
        public FirebaseUtil()
        {

        }

        public string SendMessage(string token)
        {
            try
            {
                // Iff the default instance hasn't been created, then we need to create it ourselves 
                if (FirebaseApp.DefaultInstance == null)
                    CreateFirebaseApp();

                // This creates a regular notifiation message to be sent
                Message message = CreateNotification(token);

                // Because we want to know the result of the message, we have to wait for it
                AlexaLambdaEntry.Logger.LogLine($"Sending notification to {token}");
                return FirebaseMessaging.DefaultInstance.SendAsync(message).GetAwaiter().GetResult();
            }
            catch (ArgumentException ex)
            {
                AlexaLambdaEntry.Logger.LogLine($"An invalid notification tried to be sent for token {token}: {ex}");
                throw;
            }
            catch (FirebaseMessagingException ex)
            {
                AlexaLambdaEntry.Logger.LogLine($"An unknown error occured in the Firebase messaging API for token {token}: {ex}");
                throw;
            }
            catch (Exception ex)
            {
                AlexaLambdaEntry.Logger.LogLine($"An unhandled exception occured when sending the message for token {token}: {ex}");
                throw;
            }
        }

        private void CreateFirebaseApp()
        {
            try
            {
                AlexaLambdaEntry.Logger.LogLine("Creating a new instance of the FirebaseApp");

                FirebaseApp firebaseApp = FirebaseApp.Create();
                AppOptions appOptions = new AppOptions();
                appOptions.Credential = GoogleCredential.FromFile(Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "key.json"));
            }
            catch (Exception ex)
            {
                AlexaLambdaEntry.Logger.LogLine($"Error creating new isntance of FirebaseApp {ex}");
                throw;
            }
        }

        private Message CreateNotification(string token)
        {
            try
            {
                Notification notification = new Notification();
                notification.Title = Constants.NOTIFICATION_TITLE;

                Message message = new Message();
                message.Android = new AndroidConfig();
                message.Android.Priority = Priority.High;
                message.Android.Notification = new AndroidNotification();
                message.Android.Notification.ChannelId = Constants.DEVICE_RING_CHANNEL_ID;
                message.Notification = notification;
                message.Token = token;

                return message;
            }
            catch (Exception ex)
            {
                AlexaLambdaEntry.Logger.Log($"Error creating notification for token {token}: {ex}");
                throw;
            }
        }
    }
}
