using Xamarin.Essentials;

namespace DeviceFinder.Utility
{
    public static class CachedData
    {
        private const string userId = "userid";
        private const string deviceName = "devicename";
        private const string alexaUserId = "alexauserid";
        private const string firebaseToken = "firebasetoken";
        private const string firstName = "firstName";
        private const string email = "email";

        public static string UserId
        {
            get => Preferences.Get(userId, null);
            set => Preferences.Set(userId, value);
        }

        public static string FirebaseToken
        {
            get => Preferences.Get(firebaseToken, null);
            set => Preferences.Set(firebaseToken, value);
        }

        public static string AlexaUserId
        {
            get => Preferences.Get(alexaUserId, null);
            set => Preferences.Set(alexaUserId, value);
        }

        public static string DeviceName
        {
            get => Preferences.Get(deviceName, null);
            set => Preferences.Set(deviceName, value);
        }

        public static string FirstName
        {
            get => Preferences.Get(firstName, null);
            set => Preferences.Set(firstName, value);
        }

        public static string Email
        {
            get => Preferences.Get(email, null);
            set => Preferences.Set(email, value);
        }
    }
}
