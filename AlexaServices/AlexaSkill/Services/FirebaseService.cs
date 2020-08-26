using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;
using AlexaDeviceFinderSkill.Models;
using FirebaseAdmin;
using FirebaseAdmin.Messaging;
using Google.Apis.Auth.OAuth2;

namespace AlexaDeviceFinderSkill.Services
{
    public class FirebaseService
    {
        public static FirebaseService Instance { get { return lazyFirebaseService.Value; } }

        private static readonly Lazy<FirebaseService> lazyFirebaseService = new Lazy<FirebaseService>(() => new FirebaseService());

        public FirebaseService()
        {
            // If the default instance hasn't been created, then we need to create it ourselves 
            if (FirebaseApp.DefaultInstance == null)
            {
                Logger.Log("Creating a new instance of the FirebaseApp");
                AppOptions appOptions = new AppOptions();
                appOptions.Credential = GoogleCredential.FromStream(Assembly.GetExecutingAssembly().GetManifestResourceStream("AlexaDeviceFinderSkill.key.json"));
                FirebaseApp.Create(appOptions);
            }
        }

        public async Task<string> SendFirebaseMessage(AmazonUserDevice request)
        {
            try
            {
                Logger.Log($"Sending notification to {request.DeviceId}");

                Message message;

                if (request.DeviceOs == AmazonUserDevice.DeviceOperatingSystem.Android)
                    message = CreateAndroidNotification(request.DeviceId);
                else
                    message = CreateAppleNotification(request.DeviceId);

                Stopwatch s = Stopwatch.StartNew();
                string sendResult = await FirebaseMessaging.DefaultInstance.SendAsync(message);
                Logger.Log($"Firebase notification send time: {s.ElapsedMilliseconds}ms");

                return sendResult;
            }
            catch (Exception ex)
            {
                Logger.Log($"An unhandled exception occured when sending the message for token {request.DeviceId}: {ex}");
                throw;
            }
        }

        private Message CreateAndroidNotification(string token)
        {
            Notification notification = new Notification();
            notification.Title = Constants.NOTIFICATION_TITLE;

            AndroidConfig androidConfig = new AndroidConfig();
            androidConfig.Priority = Priority.High;
            androidConfig.Notification = new AndroidNotification();
            androidConfig.Notification.ChannelId = Constants.DEVICE_RING_CHANNEL_ID;

            Message message = new Message();
            message.Notification = notification;
            message.Android = androidConfig;
            message.Token = token;

            return message;
        }

        private Message CreateAppleNotification(string token)
        {
            Notification notification = new Notification();
            notification.Title = Constants.NOTIFICATION_TITLE;

            ApnsConfig apnsConfig = new ApnsConfig();
            apnsConfig.FcmOptions = new ApnsFcmOptions();

            Message message = new Message();
            message.Notification = notification;
            message.Apns = apnsConfig;
            message.Token = token;

            return message;
        }
    }
}
