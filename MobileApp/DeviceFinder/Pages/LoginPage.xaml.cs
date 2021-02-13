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
            this.LoginButton.Clicked += OnLoginButtonClicked;
        }

        private async void OnLoginButtonClicked(object sender, EventArgs e)
        {
            IGoogleAuthProvider authProvider = DependencyForge.Get<IGoogleAuthProvider>();
            AuthResult authResponse = null;

            try
            {
                authResponse = await authProvider.LoginAsync();
            }
            catch (AuthException authEx)
            {
                IToaster toaster = DependencyForge.Get<IToaster>();
                toaster.ShowShortToast(authEx.ErrorMessage);
            }

            if (authProvider.IsLoggedIn)
            {
                CachedData.UserId = authResponse.Account.Id;
                CachedData.DeviceName = authResponse.Account.GivenName;

                App.Current.MainPage = new NamePage();
            }
        }
    }
}
