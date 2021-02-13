using System;
using DeviceFinder.Abstractions;
using DeviceFinder.Utility;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace DeviceFinder.Pages
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class NamePage : ContentPage
    {
        public NamePage()
        {
            InitializeComponent();
            this.ContinueButton.Clicked += OnContinueButtonClicked;
            this.DeviceNameEntry.Text = SavedData.DeviceName ?? "Your name";
        }

        private void OnContinueButtonClicked(object sender, EventArgs e)
        {
            Application.Current.MainPage = new VerificationPage();
        }
    }
}
