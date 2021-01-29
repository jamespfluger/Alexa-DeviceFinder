using Android.App;
using Android.Content.PM;
using Android.OS;
using Android.Runtime;
using Android.Util;
using DeviceFinder.Abstractions;
using DeviceFinder.Droid.Abstractions;
using DeviceFinder.Droid.Listeners;
using Xamarin.Essentials;
using Xamarin.Forms;
using Xamarin.LoginWithAmazon.API;

namespace DeviceFinder.Droid
{
    [Activity(Label = "AlexaDeviceFinder", Icon = "@mipmap/icon", Theme = "@style/MainTheme", NoHistory = true, ConfigurationChanges = ConfigChanges.ScreenSize | ConfigChanges.Orientation | ConfigChanges.UiMode | ConfigChanges.ScreenLayout | ConfigChanges.SmallestScreenSize)]
    public class MainActivity : global::Xamarin.Forms.Platform.Android.FormsAppCompatActivity
    {
        private RequestContext requestContext;

        protected override void OnCreate(Bundle savedInstanceState)
        {
            TabLayoutResource = Resource.Layout.Tabbar;
            ToolbarResource = Resource.Layout.Toolbar;

            base.OnCreate(savedInstanceState);

            Platform.Init(this, savedInstanceState);
            Forms.Init(this, savedInstanceState);

            requestContext = RequestContext.Create(ApplicationContext);
            LoginListener amazonLoginListener = new LoginListener();
            requestContext.RegisterListener(amazonLoginListener);

            DependencyForgeInjector.Inject(requestContext);
            DependencyForge.Get<IAmazonAuthManager>().RefreshToken();
            DependencyForge.Get<IAmazonAuthManager>().RefreshUser();

            LoadApplication(new App());
        }

        public override void OnRequestPermissionsResult(int requestCode, string[] permissions, [GeneratedEnum] Android.Content.PM.Permission[] grantResults)
        {
            Platform.OnRequestPermissionsResult(requestCode, permissions, grantResults);

            base.OnRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        protected override void OnResume()
        {
            base.OnResume();
            requestContext.OnResume();
        }

        protected override void OnPause()
        {
            base.OnPause();
            Log.Warn("DEVICEFINDERDROID", $"Activity is being paused: {nameof(MainActivity)}");
        }
    }
}