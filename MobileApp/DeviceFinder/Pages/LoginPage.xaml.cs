using System;
using System.Threading.Tasks;
using DeviceFinder.Abstractions;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace DeviceFinder.Pages
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class LoginPage : ContentPage
    {
        public LoginPage()
        {
            InitializeComponent();
            DependencyForge.Get<IDebugger>().LogDebugInfo(nameof(LoginPage));
            this.LoginButton.Clicked += OnLoginButtonClicked;
        }

        private void OnLoginButtonClicked(object sender, EventArgs e)
        {
            //IPermissionsManager permissionsManager = DependencyForge.Get<IPermissionsManager>();
            //permissionsManager.RequestPermissions();
            IAmazonAuthManager authManager = DependencyForge.Get<IAmazonAuthManager>();
            authManager.SignIn();
            //App.Current.MainPage = new NamePage();
        }
    }
}