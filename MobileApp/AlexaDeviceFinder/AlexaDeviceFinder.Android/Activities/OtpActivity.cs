using System;
using System.Text;
using Android.Content;
using Android.OS;
using Android.Views;
using Android.Views.Animations;
using Android.Views.InputMethods;
using Android.Widget;
using DeviceFinder.Droid.API;
using DeviceFinder.Droid.Models;
using DeviceFinder.Droid.Utilities;
using Java.IO;
using RestSharp;

namespace DeviceFinder.Droid.Activities
{
    public class OtpActivity : AndroidX.AppCompat.App.AppCompatActivity
    {
        private PreferencesManager _preferencesManager;

        protected void onCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            _preferencesManager = new PreferencesManager(ApplicationContext);

            SetContentView(Resource.Layout.activity_otp);
            this.FindViewById(Resource.Id.otpControlsLayout).Touch += ControlsTouchListener;
            this.FindViewById(Resource.Id.otpVerifyButton).Click += createVerifyClickListener;
        }

        private void createVerifyClickListener(object sender, EventArgs args)
        {
            // Disable and hide views
            setViewAndChildrenEnabled(this.FindViewById(Resource.Id.otpControlsLayout), false);
            this.FindViewById(Resource.Id.otpVerificationPanel).Visibility = ViewStates.Visible;

            // Build OTP
            StringBuilder otpBuilder = new StringBuilder();
            ViewGroup viewGroup = this.FindViewById(Resource.Id.otpFieldRow) as ViewGroup;
            for (int i = 0; i < viewGroup.ChildCount; i++)
            {
                View child = viewGroup.GetChildAt(i);
                if (child is OtpEditText)
                    otpBuilder.Append(((OtpEditText)child).Text);
            }

            if (otpBuilder.Length != 6)
            {
                displayUiErrors();
                setViewAndChildrenEnabled(this.FindViewById(Resource.Id.otpControlsLayout), true);
                this.FindViewById(Resource.Id.otpVerificationPanel).Visibility = ViewStates.Gone;
                return;
            }

            // Build auth device
            AuthUserDevice authUserDevices = new AuthUserDevice();
            authUserDevices.UserId = _preferencesManager.GetAmazonUserId();
            authUserDevices.DeviceId = _preferencesManager.GetDeviceId();
            authUserDevices.DeviceName = _preferencesManager.GetDeviceName();
            authUserDevices.Otp = otpBuilder.ToString();

            // Execute authorization
            ApiService authApi = new ApiService();
            UserDevice userCall = authApi.AddUserDevice();

            if (userCall != null)
            {
                OnResponse(null);
            }
        }

        public void OnResponse(IRestResponse response)
        {
            if (response.IsSuccessful)
            {
                Toast.MakeText(ApplicationContext, "Successfully connected with Alexa", ToastLength.Short).Show();
                UserDevice newDevice = new UserDevice();//response.Body
                _preferencesManager.SetUserId(newDevice.AlexaUserId);
                switchToConfigActivity();
            }
            else
            {
                Toast.MakeText(ApplicationContext, response.StatusCode + " - " + response.ErrorMessage, ToastLength.Short).Show();
                displayUiErrors();
            }
            setViewAndChildrenEnabled(this.FindViewById(Resource.Id.otpControlsLayout), true);
            this.FindViewById(Resource.Id.otpVerificationPanel).Visibility = ViewStates.Gone;
        }

        public void OnFailure(IRestResponse response)
        {
            Toast.MakeText(ApplicationContext, response.ErrorMessage, ToastLength.Short).Show();
            setViewAndChildrenEnabled(this.FindViewById(Resource.Id.otpControlsLayout), true);
            this.FindViewById(Resource.Id.otpVerificationPanel).Visibility = ViewStates.Gone;
            displayUiErrors();
        }

        private void ControlsTouchListener(object sender, View.TouchEventArgs args)
        {
            //public bool onTouch(View v, MotionEvent event)
            if (CurrentFocus == null)
                return;

            InputMethodManager imm = (InputMethodManager)GetSystemService(Context.InputMethodService);

            if (imm != null)
            {
                imm.HideSoftInputFromWindow(CurrentFocus.WindowToken, 0);
                CurrentFocus.ClearFocus();
            }
        }

        private void displayUiErrors()
        {
            ViewGroup viewGroup = this.FindViewById(Resource.Id.otpFieldRow) as ViewGroup;

            for (int i = 0; i < viewGroup.ChildCount; i++)
            {
                View child = viewGroup.GetChildAt(i);
                if (child is OtpEditText)
                    ((OtpEditText)child).SetErrorState();
            }

            Animation animation = AnimationUtils.LoadAnimation(ApplicationContext, Resource.Animation.shake);
            viewGroup.StartAnimation(animation);
        }

        private void setViewAndChildrenEnabled(View view, bool enabled)
        {
            view.Enabled = enabled;

            if (view is ViewGroup)
            {
                ViewGroup viewGroup = (ViewGroup)view;

                for (int i = 0; i < viewGroup.ChildCount; i++)
                {
                    View child = viewGroup.GetChildAt(i);
                    setViewAndChildrenEnabled(child, enabled);
                }
            }
        }

        private void switchToConfigActivity()
        {
            Intent otpIntent = new Intent(this, typeof(DevicesConfigActivity));
            StartActivity(otpIntent);
            Finish();
        }
    }
}