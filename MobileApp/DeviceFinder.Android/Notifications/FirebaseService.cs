using Android.App;
using Android.Content;
using Android.Gms.Tasks;
using Android.Util;
using DeviceFinder.Utility;
using Firebase;
using Firebase.Messaging;
using Xamarin.Essentials;
using AndroidTask = Android.Gms.Tasks.Task;

namespace DeviceFinder.Droid.Notifications
{
    [Service]
    [IntentFilter(new[] { "com.google.firebase.MESSAGING_EVENT" })]
    public class FirebaseService : FirebaseMessagingService, IOnCompleteListener
    {
        private readonly NotificationForge forge;

        public FirebaseService()
        {
            Log.Error("SAVEMEHELPME-FS", $"Platform.AppContext: {Platform.AppContext is not null}");
            Log.Error("SAVEMEHELPME-FS", $"Platform.CurrentActvitity: {Platform.CurrentActivity is not null}");
            FirebaseApp.InitializeApp(Platform.AppContext);
            forge = new NotificationForge(Platform.AppContext);
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
            AndroidTask getTokenTask = FirebaseMessaging.Instance.GetToken();
            getTokenTask.AddOnCompleteListener(this);
        }

        public void OnComplete(AndroidTask loginTask)
        {
            if (loginTask.IsSuccessful)
            {
                CachedData.FirebaseToken = loginTask.Result.ToString();
            }
        }
    }
}
