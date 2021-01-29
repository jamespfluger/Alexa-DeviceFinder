using Android.Content;
using Android.Gms.Tasks;
using Firebase.Messaging;

namespace DeviceFinder.Droid.Notifications
{
    public class FirebaseService : FirebaseMessagingService
    {
        private NotificationForge forge;
        private readonly Context context;

        public FirebaseService(Context context)
        {
            this.context = context;
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

            if (this.forge == null)
                this.forge = new NotificationForge(this.ApplicationContext);

            this.forge.IssueNotification(remoteMessage);
        }

        public void RefreshToken()
        {
            Task tokenResult = FirebaseMessaging.Instance.GetToken();
            //tokenResult.Result;
        }
    }
}