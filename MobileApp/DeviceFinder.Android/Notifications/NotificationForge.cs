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
            this.notificationManager = (NotificationManager)context.GetSystemService(Context.NotificationService);

            this.notificationBuilder = new NotificationCompat.Builder(context, this.channelId);
            this.notificationBuilder.SetChannelId(this.channelId)
                    .SetLargeIcon(BitmapFactory.DecodeResource(context.Resources, Resource.Mipmap.ic_launcher))
                    .SetSmallIcon(Resource.Mipmap.ic_notification)
                    .SetAutoCancel(true)
                    .SetOnlyAlertOnce(true)
                    .SetPriority(NotificationCompat.PriorityMax)
                    .SetCategory(NotificationCompat.CategoryCall);

            if (Build.VERSION.SdkInt >= BuildVersionCodes.O && this.notificationManager.GetNotificationChannel(this.channelId) == null)
            {
                CreateNotificationChannel();
            }
        }

        public void IssueNotification(RemoteMessage remoteMessage)
        {
            this.notificationBuilder.SetContentText(remoteMessage.GetNotification().Title);
            this.notificationManager.Notify(DateTime.Today.Millisecond, this.notificationBuilder.Build());
        }

        private void CreateNotificationChannel()
        {
            NotificationChannel channel = new NotificationChannel(
                    this.channelId,
                    this.context.GetString(Resource.String.deviceAlertChannelName),
                    NotificationImportance.Max);

            AudioAttributes ringtoneAttributes = new AudioAttributes.Builder()
                    .SetUsage(AudioUsageKind.NotificationRingtone)
                    .Build();

            channel.SetBypassDnd(true);
            channel.SetSound(RingtoneManager.GetActualDefaultRingtoneUri(this.context, RingtoneType.Ringtone), ringtoneAttributes);

            this.notificationBuilder.SetChannelId(this.channelId);
            this.notificationManager.CreateNotificationChannel(channel);
        }
    }
}