using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq.Expressions;
using System.Text;
using System.Threading.Tasks;
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
                if (FirebaseApp.DefaultInstance == null)
                {
                    AlexaLambdaEntry.Logger.LogLine("Creating a new instance of the FirebaseApp");

                    FirebaseApp firebaseApp = FirebaseApp.Create(new AppOptions()
                    {
                        Credential = GoogleCredential.FromFile(Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "key.json")),
                    });

                    AlexaLambdaEntry.Logger.LogLine("Successfully created the FirebaseApp");
                }

                AlexaLambdaEntry.Logger.LogLine("Creating the notification");
                Notification notification = new Notification();
                notification.Title = "Device Finder (swipe to dismiss)";

                AlexaLambdaEntry.Logger.LogLine("Creating the message");
                Message message = new Message();
                message.Android = new AndroidConfig();
                message.Android.Priority = Priority.High;
                message.Android.Notification = new AndroidNotification();
                message.Android.Notification.ChannelId = Constants.DEVICE_RING_CHANNEL_ID;
                message.Notification = notification;
                message.Token = token;

                AlexaLambdaEntry.Logger.LogLine("Sending the notification");
                return FirebaseMessaging.DefaultInstance.SendAsync(message).GetAwaiter().GetResult();
            }
            catch (ArgumentException ex)
            {
                AlexaLambdaEntry.Logger.LogLine($"An invalid notification tried to be sent: {ex}");
                throw;
            }
            catch (FirebaseMessagingException ex)
            {
                AlexaLambdaEntry.Logger.LogLine($"An unknown error occured in the Firebase messaging API: " + ex);
                throw;
            }
            catch (Exception ex)
            {
                AlexaLambdaEntry.Logger.LogLine($"An unhandled exception occured when sending the message: {ex}");
                throw;
            }
        }

        private List<String> GetSubFiles(string directory)
        {
            List<String> files = new List<String>();
            try
            {
                foreach (string f in Directory.GetFiles(directory))
                {
                    files.Add(f);
                }
            }
            catch (Exception ex)
            {
                AlexaLambdaEntry.Logger.LogLine($"Error getting directories at {directory}: {ex}");
            }

            try
            {
                foreach (string d in Directory.GetDirectories(directory))
                {
                    files.AddRange(GetSubFiles(d));
                }
            }
            catch (System.Exception ex)
            {
                AlexaLambdaEntry.Logger.LogLine($"Error getting directories at {directory}: {ex}");
            }

            return files;
        }
    }
}
