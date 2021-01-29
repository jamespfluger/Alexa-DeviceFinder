using Android.App;
using Android.Content;
using Android.Net;
using Android.OS;
using Android.Provider;

namespace DeviceFinder.Droid.Utilities
{
    public class  PermissionsRequester {

    public void RequestPermissions(Context context) {

        NotificationManager notificationManager = (NotificationManager) context.GetSystemService(Context.NotificationService);

        if (Build.VERSION.SdkInt >= BuildVersionCodes.M) {
            if (!notificationManager.IsNotificationPolicyAccessGranted) {

                AlertDialog.Builder requireDoNotDisturb = new AlertDialog.Builder(context);
                requireDoNotDisturb.SetTitle(Resource.String.warning);
                requireDoNotDisturb.SetMessage(Resource.String.doNotDisturbWarning);

                if (!notificationManager.IsNotificationPolicyAccessGranted) {
                    Intent intent = new Intent(Android.Provider.Settings.ActionNotificationPolicyAccessSettings);
                    context.StartActivity(intent);
                }
            }

            PowerManager pm = (PowerManager) context.GetSystemService(Context.PowerService);
            string packageName = context.PackageName;
            if (!pm.IsIgnoringBatteryOptimizations(packageName)) {

                AlertDialog.Builder requireBatteryOptimization = new AlertDialog.Builder(context);
                requireBatteryOptimization.SetTitle(Resource.String.warning);
                requireBatteryOptimization.SetMessage(Resource.String.batteryOptimizationWarning);

                Intent intent = new Intent();
                intent.SetAction(Settings.ActionRequestIgnoreBatteryOptimizations);
                intent.SetData(Uri.Parse("package:" + packageName));
                context.StartActivity(intent);
            }
        }
    }
}
}