using System;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Views;
using Android.Views.Animations;
using Android.Views.InputMethods;
using Android.Widget;
using DeviceFinder.Droid.Models;
using DeviceFinder.Droid.Utilities;
using static Android.Views.View;

namespace DeviceFinder.Droid.Activities
{
    public class NameActivity : Xamarin.Forms.Platform.Android.FormsAppCompatActivity
    {
        private PreferencesManager preferencesManager;

        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            preferencesManager = new PreferencesManager(this.ApplicationContext);

            base.SetContentView(Resource.Layout.activity_name);
            InitializeUi();
        }

        private void InitializeUi()
        {
            EditText deviceNameField = (EditText)this.FindViewById(Resource.Id.deviceNameField);
            Button deviceNameContinueButton = (Button)this.FindViewById(Resource.Id.deviceNameContinueButton);
            // TODO: remove this before going live
            deviceNameField.SetText("James", TextView.BufferType.Normal);

            deviceNameField.FocusChange += DeviceNameFieldFocusChange;
            deviceNameContinueButton.Click += ContinueButtonClick;
        }

        private void ContinueButtonClick(object sender, EventArgs args)
        {
            EditText deviceNameField = this.FindViewById(Resource.Id.deviceNameField) as EditText;
            string deviceName = deviceNameField.Text;

            if (deviceNameField.Text.Length == 0)
            {
                TextView deviceNameErrorDescription = this.FindViewById(Resource.Id.deviceNameErrorDescription) as TextView;
                deviceNameErrorDescription.Text = "You must provide a device name before continuing.";
                deviceNameErrorDescription.Visibility = ViewStates.Visible;

                Animation errorAnimation = AnimationUtils.LoadAnimation(this, Resource.Animation.shake);
                deviceNameField.StartAnimation(errorAnimation);
            }
            else if (!CommonData.Names.Contains(deviceName.ToLowerInvariant()))
            {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                alertBuilder.SetTitle("Warning");
                alertBuilder.SetMessage("The name \"" + deviceName + "\" is not a common first name.\nWhile not required, the skill works best if you use a first name.\n\nAre you sure you want to continue?");
                alertBuilder.SetPositiveButton("Yes", YesAction);
                alertBuilder.SetNegativeButton("No", YesAction);
                alertBuilder.Show();
            }
            else
            {
                preferencesManager.SetDeviceName(deviceNameField.Text);
                SwitchToActivity(typeof(OtpActivity));
            }
        }

        private void YesAction(object sender, DialogClickEventArgs args)
        {
            EditText deviceNameField = this.FindViewById(Resource.Id.deviceNameField) as EditText;
            preferencesManager.SetDeviceName(deviceNameField.Text);
            SwitchToActivity(typeof(OtpActivity));
        }

        private void SwitchToActivity(Type newActivity)
        {
            Intent newIntent = new Intent(this, newActivity);
            this.StartActivity(newIntent);
            this.Finish();
        }

        private void DeviceNameFieldFocusChange(object sender, FocusChangeEventArgs args)
        {
            if (!args.HasFocus)
            {
                InputMethodManager inputMethodManager = (InputMethodManager)this.GetSystemService(Context.InputMethodService);

                if (inputMethodManager != null)
                {
                    inputMethodManager.HideSoftInputFromWindow(((View)sender).ApplicationWindowToken, 0);
                }
            }
            else
            {
                TextView deviceNameErrorDescription = this.FindViewById(Resource.Id.deviceNameErrorDescription) as TextView;
                deviceNameErrorDescription.Visibility = ViewStates.Invisible;
            }
        }
    }
}