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
        private readonly string channelId = "CHANNEL_4096";
        private readonly Context context;
        private readonly NotificationManager notificationManager;
        private readonly NotificationCompat.Builder notificationBuilder;

        public NotificationForge(Context context)
        {
            this.context = context;
            notificationManager = (NotificationManager)context.GetSystemService(Context.NotificationService);

            notificationBuilder = new NotificationCompat.Builder(context, channelId);
            notificationBuilder.SetChannelId(channelId)
                    .SetLargeIcon(BitmapFactory.DecodeResource(context.Resources, Resource.Mipmap.ic_launcher))
                    .SetSmallIcon(Resource.Mipmap.ic_notification)
                    .SetAutoCancel(true)
                    .SetOnlyAlertOnce(true)
                    .SetPriority(NotificationCompat.PriorityMax)
                    .SetCategory(NotificationCompat.CategoryCall);

            if (Build.VERSION.SdkInt >= BuildVersionCodes.O && notificationManager.GetNotificationChannel(channelId) == null)
            {
                CreateNotificationChannel();
            }
        }

        public void IssueNotification(RemoteMessage remoteMessage)
        {
            notificationBuilder.SetContentText(remoteMessage.GetNotification().Title);
            notificationManager.Notify(DateTime.Today.Millisecond, notificationBuilder.Build());
        }

        private void CreateNotificationChannel()
        {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    context.GetString(Resource.String.deviceAlertChannelName),
                    NotificationImportance.Max);

            AudioAttributes ringtoneAttributes = new AudioAttributes.Builder()
                    .SetUsage(AudioUsageKind.NotificationRingtone)
                    .Build();

            channel.SetBypassDnd(true);
            channel.SetSound(RingtoneManager.GetActualDefaultRingtoneUri(context, RingtoneType.Ringtone), ringtoneAttributes);

            notificationBuilder.SetChannelId(channelId);
            notificationManager.CreateNotificationChannel(channel);
        }
    }
}