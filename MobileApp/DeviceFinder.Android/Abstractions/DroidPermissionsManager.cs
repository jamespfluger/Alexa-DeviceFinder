using Android.App;
using Android.Content;
using Android.Net;
using Android.OS;
using Android.Provider;
using DeviceFinder.Abstractions;
using Xamarin.Essentials;

namespace DeviceFinder.Droid.Abstractions
{
    internal class DroidPermissionsManager : IPermissionsManager
    {
        private string dialogActionToTake;

        public void RequestPermissions()
        {
            if (Build.VERSION.SdkInt >= BuildVersionCodes.M)
            {
                RequestDoNotDisturb();
                RequestOverrideBatteryOptimization();
            }
        }

        private void RequestDoNotDisturb()
        {
            Context context = Platform.AppContext;
            NotificationManager notificationManager = (NotificationManager)context.GetSystemService(Context.NotificationService);

            if (!notificationManager.IsNotificationPolicyAccessGranted)
            {
                AlertDialog.Builder requireDoNotDisturb = new AlertDialog.Builder(Platform.CurrentActivity);
                requireDoNotDisturb.SetTitle(Resource.String.warning);
                requireDoNotDisturb.SetMessage(Resource.String.doNotDisturbWarning);
                requireDoNotDisturb.SetPositiveButton("Ok", OnDialogDismissed);

                dialogActionToTake = Settings.ActionNotificationPolicyAccessSettings;

                requireDoNotDisturb.Show();
            }
        }

        private void RequestOverrideBatteryOptimization()
        {
            Context context = Platform.AppContext;
            PowerManager powerManager = (PowerManager)context.GetSystemService(Context.PowerService);

            if (!powerManager.IsIgnoringBatteryOptimizations(context.PackageName))
            {
                AlertDialog.Builder requireBatteryOptimization = new AlertDialog.Builder(Platform.CurrentActivity);
                requireBatteryOptimization.SetTitle(Resource.String.warning);
                requireBatteryOptimization.SetMessage(Resource.String.batteryOptimizationWarning);
                requireBatteryOptimization.SetPositiveButton("Ok", OnDialogDismissed);

                dialogActionToTake = Settings.ActionRequestIgnoreBatteryOptimizations;

                requireBatteryOptimization.Show();
            }
        }

        private void OnDialogDismissed(object sender, DialogClickEventArgs args)
        {
            Intent intent = new Intent(dialogActionToTake, Uri.Parse($"package:{Platform.AppContext}"));
            intent.SetFlags(ActivityFlags.NewTask);

            Platform.AppContext.StartActivity(intent);
        }
    }
}