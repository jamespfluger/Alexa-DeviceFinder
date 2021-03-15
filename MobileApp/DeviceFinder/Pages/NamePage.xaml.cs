using System;
using System.Collections.Generic;
using System.IO;
using System.Reflection;
using System.Threading.Tasks;
using DeviceFinder.Utility;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace DeviceFinder.Pages
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class NamePage : ContentPage
    {
        private readonly Task namesInitTask;
        private HashSet<string> commonNames;

        public NamePage()
        {
            InitializeComponent();

            namesInitTask = InitNamesList();
            continueButton.Clicked += OnContinueButtonClicked;
            nameField.Focused += OnNameFieldFocused;
        }

        private async void OnContinueButtonClicked(object sender, EventArgs e)
        {
            string nameEntered = nameField.Text?.Trim() ?? string.Empty;

            if (string.IsNullOrEmpty(nameEntered))
            {
                nameFieldContainer.BorderColor = (Color)Application.Current.Resources["DarkErrorColor"];
                errorLabel.IsVisible = true;
                await AnimationUtil.Shake(nameField, 0.9);
            }
            else if (await ValidateName(nameEntered))
            {
                CachedData.DeviceName = nameEntered;
                Application.Current.MainPage = new NavigationPage(new VerificationPage());
            }
        }

        private async Task<bool> ValidateName(string nameEntered)
        {
            // Finish loading the names if we haven't yet
            await namesInitTask;

            // If the provided name is not in the set, then the user is (probably) using an uncommon name
            if (!commonNames.Contains(nameEntered.ToLowerInvariant()))
                return await DisplayAlert("Warning", $"The name '{nameEntered}' MAY be uncommon enough that Alexa could have trouble understanding it. Are you sure you want to continue?\nYou can always change this name later.", "Yes", "No");
            else
                return true;
        }

        private Task InitNamesList()
        {
            return Task.Run(async () =>
            {
                // To get the embedded file we need to get the stream from the assembly using reflection
                Assembly currentAssembly = typeof(NamePage).Assembly;
                Stream resourceStream = currentAssembly.GetManifestResourceStream("DeviceFinder.names.txt");

                // From there we can load the contents of names.txt into a file and split
                using StreamReader reader = new StreamReader(resourceStream);
                string rawNamesContent = await reader.ReadToEndAsync();

                commonNames = new HashSet<string>(rawNamesContent.Split(","));
            });
        }

        private void OnNameFieldFocused(object sender, FocusEventArgs args)
        {
            nameFieldContainer.BorderColor = Color.Transparent;
            errorLabel.IsVisible = false;
        }
    }
}
