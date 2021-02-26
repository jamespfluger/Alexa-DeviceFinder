using Android.App;
using Android.Content;
using DeviceFinder.Utility;
using Firebase;
using Firebase.Messaging;
using Xamarin.Essentials;

namespace DeviceFinder.Droid.Notifications
{
    [Service]
    [IntentFilter(new[] { "com.google.firebase.MESSAGING_EVENT" })]
    public class FirebaseService : FirebaseMessagingService
    {
        private NotificationForge forge;

        public FirebaseService()
        {
            FirebaseApp.InitializeApp(Platform.AppContext);
        }

        public FirebaseService(Context context)
        {
            FirebaseApp.InitializeApp(context);
        }

        public override void OnNewToken(string newToken)
        {
            base.OnNewToken(newToken);
            CachedData.FirebaseToken = newToken;
        }

        public override void OnMessageReceived(RemoteMessage remoteMessage)
        {
            base.OnMessageReceived(remoteMessage);

            if (forge == null)
                forge = new NotificationForge(ApplicationContext);

            forge.IssueNotification(remoteMessage);
        }

        public void RefreshToken()
        {
            FirebaseMessaging.Instance.GetToken();
        }
    }
}