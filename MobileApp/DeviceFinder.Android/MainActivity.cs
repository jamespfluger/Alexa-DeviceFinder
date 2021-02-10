using Android.App;
using Android.Content;
using Android.Content.PM;
using Android.OS;
using Android.Runtime;
using Android.Util;
using DeviceFinder.Abstractions;
using DeviceFinder.Droid.Abstractions;
using DeviceFinder.Droid.Notifications;
using Xamarin.Essentials;
using Xamarin.Extensions.GoogleAuth;
using Xamarin.Forms;

namespace DeviceFinder.Droid
{
    [Activity(Label = "AlexaDeviceFinder", Icon = "@mipmap/icon", Theme = "@style/MainTheme", NoHistory = true, ConfigurationChanges = ConfigChanges.ScreenSize | ConfigChanges.Orientation | ConfigChanges.UiMode | ConfigChanges.ScreenLayout | ConfigChanges.SmallestScreenSize)]
    public class MainActivity : global::Xamarin.Forms.Platform.Android.FormsAppCompatActivity
    {
        protected override void OnCreate(Bundle savedInstanceState)
        {
            TabLayoutResource = Resource.Layout.Tabbar;
            ToolbarResource = Resource.Layout.Toolbar;

            base.OnCreate(savedInstanceState);

            Platform.Init(this, savedInstanceState);
            Forms.Init(this, savedInstanceState);

            AndroidGoogleAuthProvider.Initialize(this);
            DependencyForgeInjector.Inject();

            FirebaseService firebaseService = new FirebaseService(this);
            firebaseService.RefreshToken();

            LoadApplication(new App());
        }

        protected override void OnResume()
        {
            base.OnResume();
        }

        protected override void OnActivityResult(int requestCode, Result resultCode, Intent data)
        {
            base.OnActivityResult(requestCode, resultCode, data);
            AndroidGoogleAuthProvider.OnAuthCompleted(requestCode, resultCode, data);
        }

        public override void OnRequestPermissionsResult(int requestCode, string[] permissions, [GeneratedEnum] Android.Content.PM.Permission[] grantResults)
        {
            Platform.OnRequestPermissionsResult(requestCode, permissions, grantResults);

            base.OnRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}