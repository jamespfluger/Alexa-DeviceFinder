using System.Collections.Generic;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.Net;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;

namespace DeviceFinder.Droid.Abstractions
{
    class PermissionsManager
    {

        public void RequestPermissions(Context context)
        {

            NotificationManager notificationManager = (NotificationManager)context.GetSystemService(Context.NotificationService);

            if (Build.VERSION.SdkInt >= BuildVersionCodes.M)
            {
                if (!notificationManager.IsNotificationPolicyAccessGranted)
                {

                    AlertDialog.Builder requireDoNotDisturb = new AlertDialog.Builder(context);
                    requireDoNotDisturb.SetTitle(Resource.String.warning);
                    requireDoNotDisturb.SetMessage(Resource.String.doNotDisturbWarning);

                    if (!notificationManager.IsNotificationPolicyAccessGranted)
                    {
                        Intent intent = new Intent(Android.Provider.Settings.ActionNotificationPolicyAccessSettings);
                        context.StartActivity(intent);
                    }
                }

                PowerManager pm = (PowerManager)context.GetSystemService(Context.PowerService);
                string packageName = context.PackageName;
                if (!pm.IsIgnoringBatteryOptimizations(packageName))
                {

                    AlertDialog.Builder requireBatteryOptimization = new AlertDialog.Builder(context);
                    requireBatteryOptimization.SetTitle(Resource.String.warning);
                    requireBatteryOptimization.SetMessage(Resource.String.batteryOptimizationWarning);

                    Intent intent = new Intent();
                    intent.SetAction(Android.Provider.Settings.ActionRequestIgnoreBatteryOptimizations);
                    intent.SetData(Uri.Parse("package:" + packageName));
                    context.StartActivity(intent);
                }
            }
        }
    }
}