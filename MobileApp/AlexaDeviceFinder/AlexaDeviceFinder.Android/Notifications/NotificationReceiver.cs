using System;
using Android.Content;
using Android.Media;

namespace DeviceFinder.Droid.Notifications
{
    public class NotificationReceiver : BroadcastReceiver
    {

        public override void OnReceive(Context? context, Intent? intent)
        {
            AudioManager manager = (AudioManager)context.GetSystemService(Context.AudioService);
            int maxRingVolume = manager.GetStreamMaxVolume(Stream.Ring);
            manager.SetStreamVolume(Stream.Ring, maxRingVolume, 0);
        }
    }
}