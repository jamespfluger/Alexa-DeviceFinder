using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace DeviceFinder
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class AuthPage : ContentPage
    {
        private readonly List<Entry> otpFields;
        public AuthPage()
        {
            InitializeComponent();
            InitializeUi();
        }

        private void InitializeUi()
        {
            SubmitButton.Clicked += OnSubmitButtonClicked;

            otpField0.TextChanged += OnOtpFieldTextChanged;
            otpField1.TextChanged += OnOtpFieldTextChanged;
            otpField2.TextChanged += OnOtpFieldTextChanged;
            otpField3.TextChanged += OnOtpFieldTextChanged;
            otpField4.TextChanged += OnOtpFieldTextChanged;
            otpField5.TextChanged += OnOtpFieldTextChanged;
        }

        private void OnSubmitButtonClicked(object sender, EventArgs e)
        {
            App.Current.MainPage = new DeviceConfigPage();
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
                var pageTabIndexes = this.GetTabIndexesOnParentPage(out int _);
                Entry nextOtpField = this.FindNextElement(entryHasContent, pageTabIndexes, ref currentTabIndex) as Entry;

                nextOtpField.Focus();
            }
        }
    }
}
