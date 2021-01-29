using Android.Content;
using DeviceFinder.Droid.Notifications;

namespace DeviceFinder.Droid.Utilities
{
    public class PreferencesManager
    {
        private string PREFERENCES_NAME = "com.Jamespfluger.Alexadevicefinder.SHARED_PREFERENCES";
        private string PREFERENCE_NAME_FIREBASE = "firebasetoken";
        private string PREFERENCE_NAME_AMAZON_USER_ID = "amazonuserid";
        private string PREFERENCE_NAME_ALEXA_USER_ID = "alexauserid";
        private string PREFERENCE_NAME_DEVICE_NAME = "devicename";
        private ISharedPreferences Preferences;
        private Context Context;

        public PreferencesManager(Context context)
        {
            this.Preferences = context.GetSharedPreferences(PREFERENCES_NAME, FileCreationMode.Private);
            this.Context = context;
        }

        public string GetDeviceId()
        {
            string firebaseToken = Preferences.GetString(PREFERENCE_NAME_FIREBASE, null);

            if (firebaseToken != null)
            {
                return firebaseToken;
            }
            else
            {
                FirebaseService firebaseService = new FirebaseService();
                firebaseService.RefreshToken();
            }

            return Preferences.GetString(PREFERENCE_NAME_FIREBASE, null);
        }

        public void SetDeviceId(string deviceId)
        {
            Preferences.Edit().PutString(PREFERENCE_NAME_FIREBASE, deviceId).Apply();
        }

        public void RefreshDeviceId()
        {
            FirebaseService firebaseService = new FirebaseService(Context);
            firebaseService.RefreshToken();
        }

        public string GetAmazonUserId()
        {
            return Preferences.GetString(PREFERENCE_NAME_AMAZON_USER_ID, null);
        }

        public void SetAmazonUserId(string userId)
        {
            Preferences.Edit().PutString(PREFERENCE_NAME_AMAZON_USER_ID, userId).Apply();
        }

        public string GetDeviceName()
        {
            return Preferences.GetString(PREFERENCE_NAME_DEVICE_NAME, null);
        }

        public void SetDeviceName(string deviceName)
        {
            Preferences.Edit().PutString(PREFERENCE_NAME_DEVICE_NAME, deviceName).Apply();
        }

        public string GetUserId()
        {
            return Preferences.GetString(PREFERENCE_NAME_ALEXA_USER_ID, null);
        }

        public void SetUserId(string userId)
        {
            Preferences.Edit().PutString(PREFERENCE_NAME_ALEXA_USER_ID, userId).Apply();
        }
    }
}