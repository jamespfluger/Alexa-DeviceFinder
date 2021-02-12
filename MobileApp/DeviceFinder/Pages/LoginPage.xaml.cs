using System;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using DeviceFinder.Abstractions;
using DeviceFinder.Utility;
using Xamarin.Extensions.GoogleAuth;
using Xamarin.Extensions.GoogleAuth.Exceptions;
using Xamarin.Extensions.GoogleAuth.Models;
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
            CommonData.Init();
            DependencyForge.Get<IDebugger>().LogDebugInfo(nameof(LoginPage));
            this.LoginButton.Clicked += OnLoginButtonClicked;
        }

        private async void OnLoginButtonClicked(object sender, EventArgs e)
        {
            AuthResult authResponse = null;

            try
            {
                authResponse = await GoogleAuthProvider.Provider.LoginAsync();
            }
            catch (AuthException authEx)
            {
                IToaster toaster = DependencyForge.Get<IToaster>();

                toaster.ShowLongToast(authEx.ErrorMessage);
            }

            if (GoogleAuthProvider.Provider.IsLoggedIn)
            {
                App.Current.MainPage = new NamePage();
            }
        }
    }
}
