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
            FirebaseApp.InitializeApp(Platform.AppContext);
            forge = new NotificationForge(ApplicationContext);
        }

        public FirebaseService(Context context)
        {
            FirebaseApp.InitializeApp(context);
            forge = new NotificationForge(ApplicationContext);
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
