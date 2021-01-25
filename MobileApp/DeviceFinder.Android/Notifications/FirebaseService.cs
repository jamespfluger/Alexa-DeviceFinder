using System;
using Android.Content;
using Android.Gms.Extensions;
using Android.Gms.Tasks;
using Android.Widget;
using DeviceFinder.Droid.Utilities;
using Firebase.Messaging;

namespace DeviceFinder.Droid.Notifications
{
    public class FirebaseService : FirebaseMessagingService
    {
        private NotificationForge NotificationForge;
        private Context Context;

        public FirebaseService(Context context)
        {
            this.Context = context;
        }

        public FirebaseService()
        {
        }

        public override void OnNewToken(string newToken)
        {
            base.OnNewToken(newToken);
            //PreferencesManager.SetDeviceId(newToken);
        }

        public override void OnMessageReceived(RemoteMessage remoteMessage)
        {
            base.OnMessageReceived(remoteMessage);

            if (NotificationForge == null)
                NotificationForge = new NotificationForge(ApplicationContext);

            NotificationForge.issueNotification(remoteMessage);
        }

        public void RefreshToken()
        {
            Task tokenResult = FirebaseMessaging.Instance.GetToken();
            //tokenResult.Result;
        }
    }
}