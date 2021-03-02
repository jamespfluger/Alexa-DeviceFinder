using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;
using DeviceFinder.Abstractions;
using DeviceFinder.API;
using DeviceFinder.Models.Auth;
using DeviceFinder.Models.Devices;
using DeviceFinder.Utility;
using RestSharp;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace DeviceFinder.Pages
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class VerificationPage : ContentPage
    {
        public VerificationPage()
        {
            InitializeComponent();
        }

        private async void OnSubmitButtonClicked(object sender, EventArgs e)
        {
            IEnumerable<Entry> otpFields = otpContainer.Children.OfType<Entry>();
            
            if (otpFields.Any(e => string.IsNullOrWhiteSpace(e.Text)))
            {
                otpContainerFrame.BorderColor = (Color)Application.Current.Resources["DarkErrorColor"];
                await AnimationUtil.Shake(otpContainer, 0.9);
            }
            else
            {
                saveOverlay.IsVisible = true;

                AuthDevice newDevice = new AuthDevice
                {
                    DeviceId = CachedData.FirebaseToken,
                    LoginUserId = CachedData.LoginUserId,
                    DeviceName = CachedData.DeviceName,
                    DeviceOs = DeviceOperatingSystem.Android,
                    OneTimePasscode = string.Join("", otpFields.Select(f => f.Text))
                };

                Stopwatch timer = Stopwatch.StartNew();
                
                ApiService api = new ApiService();
                IRestResponse saveResponse = await api.SaveDevice(newDevice);
                IToaster toaster = DependencyForge.Get<IToaster>();

                timer.Stop();

                if (timer.ElapsedMilliseconds < 500)
                    await Task.Delay(1000 - (int)timer.ElapsedMilliseconds);

                saveOverlay.IsVisible = false;

                //Application.Current.MainPage = new DeviceConfigPage();
            }
        }

        private void OnOtpFieldTextChanged(object sender, TextChangedEventArgs args)
        {
            Entry currentOtpField = sender as Entry;
            int currentTabIndex = otpContainer.Children.IndexOf(currentOtpField);
            int nextTabIndex = args.NewTextValue.Length != 0 ? currentTabIndex + 1 : currentTabIndex - 1;

            // If we're at the beginning and text was deleted OR if we're at the end and text was added, break out
            if (nextTabIndex >= 0 && nextTabIndex < otpContainer.Children.Count)
            {
                otpContainer.Children[nextTabIndex].Focus();
            }
            else
            {
                currentOtpField.Unfocus();
            }
        }

        private void OnOtpFieldFocus(object sender, FocusEventArgs args)
        {
            otpContainerFrame.BorderColor = Color.Transparent;
        }
    }
}
