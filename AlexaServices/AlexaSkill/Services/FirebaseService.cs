using System;
using System.Diagnostics;
using System.Reflection;
using System.Threading.Tasks;
using DeviceFinder.AlexaSkill.Utility;
using DeviceFinder.Models.Devices;
using FirebaseAdmin;
using FirebaseAdmin.Messaging;
using Google.Apis.Auth.OAuth2;

namespace DeviceFinder.AlexaSkill.Services
{
    public class FirebaseService
    {
        public static FirebaseService Instance => lazyFirebaseService.Value;

        private static readonly Lazy<FirebaseService> lazyFirebaseService = new Lazy<FirebaseService>(() => new FirebaseService());
        private const string DEVICE_RING_CHANNEL_ID = "CHANNEL_4096";
        private const string NOTIFICATION_TITLE = "Device Finder (swipe to dismiss)";

        public FirebaseService()
        {
            // If the default instance hasn't been created, then we need to create it ourselves 
            if (FirebaseApp.DefaultInstance == null)
            {
                Logger.Log("Creating a new instance of the FirebaseApp");
                AppOptions appOptions = new AppOptions();
                appOptions.Credential = GoogleCredential.FromStream(Assembly.GetExecutingAssembly().GetManifestResourceStream("AlexaSkill.key.json"));

                FirebaseApp.Create(appOptions);
            }
        }

        public async Task<string> SendFirebaseMessage(UserDevice request)
        {
            try
            {
                Logger.Log($"Sending notification to {request.DeviceId}");

                Message message;

                if (request.DeviceOs == DeviceOperatingSystem.Android)
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
            notification.Title = NOTIFICATION_TITLE;

            AndroidConfig androidConfig = new AndroidConfig();
            androidConfig.Priority = Priority.High;
            androidConfig.Notification = new AndroidNotification();
            androidConfig.Notification.ChannelId = DEVICE_RING_CHANNEL_ID;

            Message message = new Message();
            message.Notification = notification;
            message.Android = androidConfig;
            message.Token = token;

            return message;
        }

        private Message CreateAppleNotification(string token)
        {
            Notification notification = new Notification();
            notification.Title = NOTIFICATION_TITLE;

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
