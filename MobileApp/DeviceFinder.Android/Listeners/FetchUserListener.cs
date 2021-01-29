using Android.Content;
using Android.Util;
using Android.Widget;
using DeviceFinder.Utility;
using Xamarin.Essentials;
using Xamarin.LoginWithAmazon;
using Xamarin.LoginWithAmazon.Additions;

namespace DeviceFinder.Droid.Listeners
{
    public class FetchUserListener : UserListener
    {
        public override void OnSuccess(Java.Lang.Object userObj)
        {
            User user = userObj as User;
            Log.Debug("DEVICEFINDERDROID", $"User fetched in: {user?.UserId}");
            SavedData.AmazonUserId = user.UserId;
        }

        public override void OnError(Java.Lang.Object error)
        {
            Log.Debug("DEVICEFINDERDROID", $"Error fetching user: {error.GetType().FullName}", ToastLength.Short);
            SavedData.AmazonUserId = null;
        }

        public override void OnCancel(Java.Lang.Object cancel)
        {
            Log.Debug("DEVICEFINDERDROID", $"Cancel fetching user: {cancel.GetType().FullName}", ToastLength.Short);
            SavedData.AmazonUserId = null;
        }
    }
}
