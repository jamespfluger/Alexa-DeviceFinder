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
        public AuthPage()
        {
            InitializeComponent();
            InitializeOtpListeners();
        }

        public void InitializeOtpListeners()
        {
            OtpField1.TextChanged += OnOtpFieldTextChanged;
            OtpField2.TextChanged += OnOtpFieldTextChanged;
            OtpField3.TextChanged += OnOtpFieldTextChanged;
            OtpField4.TextChanged += OnOtpFieldTextChanged;
            OtpField5.TextChanged += OnOtpFieldTextChanged;
            OtpField6.TextChanged += OnOtpFieldTextChanged;
        }

        private void OnOtpFieldTextChanged(object sender, TextChangedEventArgs args)
        {
            if (args.NewTextValue.Length == 1)
            {
                Entry otpField = sender as Entry;

                switch (otpField.TabIndex)
                {
                    case 0:
                        OtpField2.Focus();
                        break;
                    case 1:
                        OtpField3.Focus();
                        break;
                    case 2:
                        OtpField4.Focus();
                        break;
                    case 3:
                        OtpField5.Focus();
                        break;
                    case 4:
                        OtpField6.Focus();
                        break;
                    case 5:
                        OtpField1.Focus();
                        break;
                    default:
                        break;
                }
            }
            else
            {
                Entry otpField = sender as Entry;

                switch (otpField.TabIndex)
                {
                    case 0:
                        OtpField6.Focus();
                        break;
                    case 1:
                        OtpField1.Focus();
                        break;
                    case 2:
                        OtpField2.Focus();
                        break;
                    case 3:
                        OtpField3.Focus();
                        break;
                    case 4:
                        OtpField4.Focus();
                        break;
                    case 5:
                        OtpField5.Focus();
                        break;
                    default:
                        break;
                }
            }
        }
    }
}