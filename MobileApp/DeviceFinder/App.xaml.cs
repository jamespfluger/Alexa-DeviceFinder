using DeviceFinder.Pages;
using Xamarin.Forms;

namespace DeviceFinder
{
    public partial class App : Application
    {
        public App()
        {
            InitializeComponent();

            this.MainPage = new LoginPage();
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
