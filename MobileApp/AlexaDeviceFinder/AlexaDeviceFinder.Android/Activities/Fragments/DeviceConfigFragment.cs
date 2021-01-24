using System;
using Android.Content;
using Android.OS;
using Android.Views;
using Android.Views.InputMethods;
using Android.Widget;
using AndroidX.AppCompat.Widget;
using AndroidX.Fragment.App;
using DeviceFinder.Droid.API;
using DeviceFinder.Droid.Models;
using RestSharp;
using static Android.Views.View;

namespace DeviceFinder.Droid.Activities.Fragments
{
    public class DeviceConfigFragment : Fragment
    {
        private readonly UserDevice _userDevice;

        public DeviceConfigFragment(UserDevice userDevice)
        {
            this._userDevice = userDevice;
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            View root = inflater.Inflate(Resource.Layout.fragment_device_config, container, false);
            return root;
        }


        public void onViewCreated(View view, Bundle savedInstanceState)
        {
            EditText deviceName = view.FindViewById(Resource.Id.settingsDeviceNameField) as EditText;
            Button saveButton = view.FindViewById(Resource.Id.settingsSaveButton) as Button;
            Button deleteButton = view.FindViewById(Resource.Id.settingsDeleteButton) as Button;

            deviceName.Text = (_userDevice.DeviceSettings.DeviceName);

            SwitchCompat useFlashlight = view.FindViewById(Resource.Id.settingsEnableFlashlightSwitch) as SwitchCompat;
            SwitchCompat useVibration = view.FindViewById(Resource.Id.settingsEnableVibrationSwitch) as SwitchCompat;
            SwitchCompat useWifi = view.FindViewById(Resource.Id.settingsEnableWifiSwitch) as SwitchCompat;
            SwitchCompat overrideMaxVolume = view.FindViewById(Resource.Id.settingsOverrideMaxVolumeSwitch) as SwitchCompat;
            SeekBar overrideMaxVolumeValue = view.FindViewById(Resource.Id.settingsVolumeToUseSlider) as SeekBar;
            useFlashlight.Checked = _userDevice.DeviceSettings.UseFlashlight;
            useVibration.Checked = _userDevice.DeviceSettings.UseVibrate;
            useWifi.Checked = _userDevice.DeviceSettings.ShouldLimitToWifi;
            overrideMaxVolume.Checked = _userDevice.DeviceSettings.UseVolumeOverride;
            overrideMaxVolumeValue.Progress = (_userDevice.DeviceSettings.OverriddenVolumeValue);

            deviceName.FocusChange += onDeviceNameFocusChange;
            saveButton.Click += onSaveButtonClick;
            deleteButton.Click += onDeleteButtonClick;
        }

        public void onDeleteButtonClick(object sender, EventArgs args)
        {
            //AuthorizationManager.SignOut(getContext(), new Listener<Void, AuthError>()

            Spinner wifiDropdown = Activity.FindViewById(Resource.Id.settingsWifiSsdDropdown) as Spinner;
            wifiDropdown.Enabled = true;
            ArrayAdapter<string> adapter = new ArrayAdapter<string>(Context, Android.Resource.Layout.SimpleSpinnerDropDownItem, new string[] { "(Feature not yet available)" });
            wifiDropdown.Adapter = adapter;
        }

        public void onSignOutError()//AuthError authError)
        {
            Toast.MakeText(Context, "Failed to sign out", ToastLength.Short).Show();
        }

        public void onDeviceNameFocusChange(object sender, FocusChangeEventArgs args)
        {
            View senderView = sender as View;

            if (!args.HasFocus)
            {
                InputMethodManager inputMethodManager = (InputMethodManager)Activity.GetSystemService(Android.Content.Context.InputMethodService);

                if (inputMethodManager != null)
                {
                    inputMethodManager.HideSoftInputFromWindow(senderView.WindowToken, 0);
                }

                senderView.ClearFocus();
            }
        }

        public void onSaveButtonClick(object sender, EventArgs args)
        {
            Activity.Window.SetFlags(WindowManagerFlags.NotTouchable, WindowManagerFlags.NotTouchable);
            Activity.FindViewById(Resource.Id.settingsSaveWaitPanel).Visibility = ViewStates.Visible;

            EditText deviceName = Activity.FindViewById(Resource.Id.settingsDeviceNameField) as EditText;
            SwitchCompat useFlashlight = Activity.FindViewById(Resource.Id.settingsEnableFlashlightSwitch) as SwitchCompat;
            SwitchCompat useVibration = Activity.FindViewById(Resource.Id.settingsEnableVibrationSwitch) as SwitchCompat;
            SwitchCompat useWifi = Activity.FindViewById(Resource.Id.settingsEnableWifiSwitch) as SwitchCompat;
            SwitchCompat overrideMaxVolume = Activity.FindViewById(Resource.Id.settingsOverrideMaxVolumeSwitch) as SwitchCompat;
            SeekBar overrideMaxVolumeValue = Activity.FindViewById(Resource.Id.settingsVolumeToUseSlider) as SeekBar;

            DeviceSettings deviceSettings = _userDevice.DeviceSettings;
            deviceSettings.AlexaUserId = _userDevice.AlexaUserId;
            deviceSettings.DeviceId = _userDevice.DeviceId;
            deviceSettings.DeviceName = deviceName.Text;
            deviceSettings.UseFlashlight = useFlashlight.Checked;
            deviceSettings.UseVibrate = useVibration.Checked;
            deviceSettings.ShouldLimitToWifi = useWifi.Checked;
            deviceSettings.ConfiguredWifiSsid = null;
            deviceSettings.UseVolumeOverride = overrideMaxVolume.Checked;
            deviceSettings.OverriddenVolumeValue = overrideMaxVolumeValue.Progress;

            ApiService apiService = new ApiService();
            apiService.SaveDeviceSettings(deviceSettings);




        }


        public void onSaveButtonClickFailure()
        {
            Activity.Window.ClearFlags(WindowManagerFlags.NotTouchable);
            Activity.FindViewById(Resource.Id.settingsSaveWaitPanel).Visibility = ViewStates.Gone;
        }


        public void onSignOutSuccess()//Void response)
        {
            switchToActivity(typeof(LoginActivity));
            Toast.MakeText(Context, "Signed out", ToastLength.Short).Show();
        }

        public void onSaveDeviceSettingsResponse(IRestResponse response)
        {
            if (!response.IsSuccessful)
            {
                Toast.MakeText(Context, "Failed to save settings - " + response.ErrorMessage, ToastLength.Short).Show();
            }
            else
            {
                Toast.MakeText(Context, "Successfully saved settings" + response.Content, ToastLength.Short).Show();
            }

            Activity.Window.ClearFlags(WindowManagerFlags.NotTouchable);
            Activity.FindViewById(Resource.Id.settingsSaveWaitPanel).Visibility = ViewStates.Gone;
        }

        private void switchToActivity(Type newActivity)
        {
            Intent newIntent = new Intent(Activity, newActivity);
            StartActivity(newIntent);
        }
    }
}