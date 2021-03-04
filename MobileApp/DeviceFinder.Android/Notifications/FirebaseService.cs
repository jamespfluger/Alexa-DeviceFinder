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
        private readonly NotificationForge forge;

        public FirebaseService()
        {
            FirebaseApp.InitializeApp(Platform.CurrentActivity);
            forge = new NotificationForge(Platform.CurrentActivity);
        }

        public FirebaseService(Context context)
        {
            FirebaseApp.InitializeApp(Platform.CurrentActivity);
            forge = new NotificationForge(Platform.CurrentActivity);
        }

        public override void OnNewToken(string newToken)
        {
            base.OnNewToken(newToken);
            CachedData.FirebaseToken = newToken;
        }

        public override void OnMessageReceived(RemoteMessage remoteMessage)
        {
            base.OnMessageReceived(remoteMessage);

            forge.IssueNotification(remoteMessage);
        }

        public void RefreshToken()
        {
            FirebaseMessaging.Instance.GetToken();
        }
    }
}
