using System;
using System.Collections.Generic;
using DeviceFinder.Abstractions;
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
            DependencyForge.Get<IDebugger>().LogDebugInfo(nameof(VerificationPage));
        }

        private void OnSubmitButtonClicked(object sender, EventArgs e)
        {
            Application.Current.MainPage = new DeviceConfigPage();
        }

        private void OnOtpFieldTextChanged(object sender, TextChangedEventArgs args)
        {
            Entry currentOtpField = sender as Entry;
            bool entryHasContent = args.NewTextValue.Length != 0;
            int currentTabIndex = currentOtpField.TabIndex;

            // If content was added and we're at the end OR if content was removed and we're at the beginning, break out
            if (entryHasContent && currentTabIndex == 5 || !entryHasContent && currentTabIndex == 0)
            {
                currentOtpField.Unfocus();
            }
            else
            {
                IDictionary<int, List<ITabStopElement>> pageTabIndexes = this.GetTabIndexesOnParentPage(out int _);
                Entry nextOtpField = this.FindNextElement(entryHasContent, pageTabIndexes, ref currentTabIndex) as Entry;

                nextOtpField.Focus();
            }
        }
    }
}
