using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Util;
using Android.Views;
using Android.Widget;
using DeviceFinder.Utility;
using Xamarin.Essentials;
using Xamarin.LoginWithAmazon;

namespace DeviceFinder.Droid.Listeners
{
    public class AmazonTokenListener : GetTokenListener
    {
        public override void OnSuccess(GetTokenResult getTokenResult)
        {
            Log.Debug("DEVICEFINDERDROID", $"User fetched in: {getTokenResult.AccessToken}");
            SavedData.AmazonToken = getTokenResult.AccessToken;
        }

        public override void OnError(AuthError authError)
        {
            SavedData.AmazonToken = null;
            Log.Debug("DEVICEFINDERDROID", $"Failed to get token: {authError.Message}", ToastLength.Short);
        }
    }
}