using DeviceFinder.Abstractions;
using DeviceFinder.Pages;
using DeviceFinder.Utility;
using Xamarin.AuthProviders.Google;
using Xamarin.Forms;

namespace DeviceFinder
{
    public partial class App : Application
    {
        public App()
        {
            InitializeComponent();

            MainPage = !string.IsNullOrEmpty(CachedData.AlexaUserId) ? new DeviceConfigRootPage() : new LoginPage();
        }

        protected override void OnStart()
        {
        }

        protected override void OnSleep()
        {
        }

        protected override void OnResume()
        {
        }
    }
}
