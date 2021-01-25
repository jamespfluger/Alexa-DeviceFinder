using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using DeviceFinder.Abstractions;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace DeviceFinder
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class LoginPage : ContentPage
    {
        public static ILoginWithAmazonManager LoginManager { get; set; }

        public static void Init(ILoginWithAmazonManager loginManager)
        {
            LoginPage.LoginManager = loginManager;
        }

        public LoginPage()
        {
            InitializeComponent();
            LoginButton.Clicked += OnLoginButtonClicked;
        }

        private void OnLoginButtonClicked(object sender, EventArgs e)
        {
            LoginManager.SignIn();
            //App.Current.MainPage = new NamePage();
        }
    }
}