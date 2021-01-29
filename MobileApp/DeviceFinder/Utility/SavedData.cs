using System;
using System.Collections.Generic;
using System.Text;
using Xamarin.Essentials;

namespace DeviceFinder.Utility
{
    public static class SavedData
    {
        private const string amazonUserId = "amazonuserid";
        private const string alexaUserId = "alexauserid";
        private const string amazonToken = "amazonToken";
        private const string deviceName = "devicename";
        private const string firebaseToken = "firebasetoken";

        public static string AmazonUserId
        {
            get { return Preferences.Get(amazonUserId, null); }
            set { Preferences.Set(amazonUserId, value); }
        }

        public static string AlexaUserId
        {
            get { return Preferences.Get(alexaUserId, null); }
            set { Preferences.Set(alexaUserId, value); }
        }

        public static string AmazonToken
        {
            get { return Preferences.Get(amazonToken, null); }
            set { Preferences.Set(amazonToken, value); }
        }

        public static string DeviceName
        {
            get { return Preferences.Get(deviceName, null); }
            set { Preferences.Set(deviceName, value); }
        }

        public static string FirebaseToken
        {
            get { return Preferences.Get(firebaseToken, null); }
            set { Preferences.Set(firebaseToken, value); }
        }
    }
}
