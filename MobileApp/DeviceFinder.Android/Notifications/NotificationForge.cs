using System;
using Android.App;
using Android.Content;
using Android.Graphics;
using Android.Media;
using Android.OS;
using AndroidX.Core.App;
using Firebase.Messaging;

namespace DeviceFinder.Droid.Notifications
{
    public class NotificationForge
    {
        private string CHANNEL_ID = "CHANNEL_4096";
        private Context context;
        private NotificationManager notificationManager;
        private NotificationCompat.Builder notificationBuilder;

        public NotificationForge(Context context)
        {
            this.context = context;
            notificationManager = (NotificationManager)context.GetSystemService(Context.NotificationService);

            notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
            notificationBuilder.SetChannelId(CHANNEL_ID)
                    .SetLargeIcon(BitmapFactory.DecodeResource(context.Resources, Resource.Mipmap.ic_launcher))
                    .SetSmallIcon(Resource.Mipmap.ic_notification)
                    .SetAutoCancel(true)
                    .SetOnlyAlertOnce(true)
                    .SetPriority(NotificationCompat.PriorityMax)
                    .SetCategory(NotificationCompat.CategoryCall);

            if (Build.VERSION.SdkInt >= BuildVersionCodes.O && notificationManager.GetNotificationChannel(CHANNEL_ID) == null)
            {
                createNotificationChannel();
            }
        }

        public void issueNotification(RemoteMessage remoteMessage)
        {
            notificationBuilder.SetContentText(remoteMessage.GetNotification().Title);
            notificationManager.Notify((int)DateTime.Today.Millisecond, notificationBuilder.Build());
        }

        private void createNotificationChannel()
        {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    context.GetString(Resource.String.deviceAlertChannelName),
                    NotificationImportance.Max);

            AudioAttributes ringtoneAttributes = new AudioAttributes.Builder()
                    .SetUsage(AudioUsageKind.NotificationRingtone)
                    .Build();

            channel.SetBypassDnd(true);
            channel.SetSound(RingtoneManager.GetActualDefaultRingtoneUri(this.context, RingtoneType.Ringtone), ringtoneAttributes);

            notificationBuilder.SetChannelId(CHANNEL_ID);
            notificationManager.CreateNotificationChannel(channel);
        }
    }
}