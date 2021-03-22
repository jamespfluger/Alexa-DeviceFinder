﻿using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using DeviceFinder.Abstractions;
using DeviceFinder.API;
using DeviceFinder.Models.Auth;
using DeviceFinder.Models.Devices;
using DeviceFinder.Utility;
using Newtonsoft.Json;
using RestSharp;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;
using Device = DeviceFinder.Models.Devices.Device;

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
                return;
            }

            saveOverlay.IsVisible = true;

            AuthData authData = new AuthData
            {
                OneTimePasscode = string.Join("", otpFields.Select(f => f.Text)),
                FirebaseToken = CachedData.FirebaseToken,
                LoginUserId = CachedData.LoginUserId,
                DeviceName = CachedData.DeviceName
            };

            IRestResponse saveResponse = null;

            try
            {
                ApiService api = new ApiService();
                saveResponse = await api.CreateDevice(authData);

                Device newDevice = JsonConvert.DeserializeObject<Device>(saveResponse.Content);
                CachedData.AlexaUserId = newDevice.AlexaUserId;

                saveOverlay.IsVisible = false;
            }
            catch (Exception ex)
            {
                IToaster toaster = DependencyForge.Get<IToaster>();
                toaster.ShowShortToast(ex.Message);
            }
            finally
            {
                if (saveResponse.ResponseStatus != ResponseStatus.Completed || saveResponse.StatusCode != HttpStatusCode.Created)
                    HandleSaveError(saveResponse);
            }

            saveOverlay.IsVisible = false;

            if (saveResponse.ResponseStatus == ResponseStatus.Completed && saveResponse.StatusCode == HttpStatusCode.Created)
                Application.Current.MainPage = new DeviceConfigRootPage();
        }

        private void HandleSaveError(IRestResponse saveResponse)
        {
            IToaster toaster = DependencyForge.Get<IToaster>();

            StringBuilder errorMessage = new StringBuilder();
            errorMessage.AppendLine(saveResponse.Content);

            if (saveResponse.ResponseStatus != ResponseStatus.Completed)
                errorMessage.AppendLine(saveResponse.ResponseStatus.ToString());
            if (!string.IsNullOrEmpty(saveResponse.ErrorMessage))
                errorMessage.AppendLine(saveResponse.ErrorMessage);

            toaster.ShowShortToast(errorMessage.ToString());
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