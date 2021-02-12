using System;
using System.Collections.Generic;
using System.Text;
using Xamarin.Essentials;

namespace DeviceFinder.Utility
{
    public static class SavedData
    {
        private const string userId = "userid";
        private const string deviceName = "devicename";
        private const string alexaUserId = "alexauserid";
        private const string firebaseToken = "firebasetoken";

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
    }
}
