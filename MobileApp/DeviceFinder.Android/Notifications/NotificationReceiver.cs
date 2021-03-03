using Android.App;
using Android.Content;
using Android.Media;

namespace DeviceFinder.Droid.Notifications
{
    [BroadcastReceiver(Enabled = true, Exported = true, Permission = "com.google.android.c2dm.permission.SEND")]
    [IntentFilter(new[] { "com.google.android.c2dm.intent.RECEIVE" })]
    public class NotificationReceiver : BroadcastReceiver
    {
        public override void OnReceive(Context context, Intent intent)
        {
            AudioManager manager = (AudioManager)context.GetSystemService(Context.AudioService);
            int maxRingVolume = manager.GetStreamMaxVolume(Stream.Ring);
            manager.SetStreamVolume(Stream.Ring, maxRingVolume, 0);
        }
    }
}