using System;
using Android.Content;
using Android.Util;
using Android.Widget;
using DeviceFinder.Utility;
using Xamarin.Essentials;
using Xamarin.LoginWithAmazon;

namespace DeviceFinder.Droid.Listeners
{
    public class LoginListener : AuthorizeListener
    {
        public override void OnSuccess(AuthorizeResult authResult)
        {
            Log.Debug("DEVICEFINDERDROID", $"Logged in: {authResult?.User?.UserId}");
            SavedData.AmazonUserId = authResult?.User?.UserId;
            SavedData.AmazonToken = authResult.AccessToken;
        }

        public override void OnError(AuthError authError)
        {
            Log.Debug("DEVICEFINDERDROID", $"Error logging in: {authError.Message}", ToastLength.Short);
            SavedData.AmazonUserId = null;
            SavedData.AmazonToken = null;
        }

        public override void OnCancel(AuthCancellation authCancel)
        {
            Log.Debug("DEVICEFINDERDROID", $"Login canceled: {authCancel.Description}", ToastLength.Short);
            SavedData.AmazonUserId = null;
            SavedData.AmazonToken = null;
        }
    }
}