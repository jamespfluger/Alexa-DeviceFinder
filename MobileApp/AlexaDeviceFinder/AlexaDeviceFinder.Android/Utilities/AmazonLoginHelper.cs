using Android.Content;
using Android.Widget;

namespace DeviceFinder.Droid.Utilities
{
    public class AmazonLoginHelper
    {
        private Context _context;

        public static void SetUserId(Context context)
        {
            //var userFetchResult = User.Fetch(context, new Listener<User, AuthError>();
        }

        public void OnUserFetchSuccess()// User user)
        {
            PreferencesManager preferencesManager = new PreferencesManager(_context);
            preferencesManager.SetAmazonUserId("user.GetUserId()");
        }

        public void OnUserFetchError()//AuthError ae)
        {
            Toast.MakeText(_context, "Error retrieving profile information.", ToastLength.Short).Show();
        }

        public static void SignOut(Context context)
        {
            //var signOutResult = AuthorizationManager.SignOut(context, new Listener<Void, AuthError>();
        }

        public void OnSignOutSuccess()//Void response)
        {
            //Toast.MakeText(context, "Signed out", ToastLength.Short).Show();
        }

        public void OnSignOutError()//AuthError authError)
        {
            Toast.MakeText(_context, "Failed to sign out", ToastLength.Short).Show();
        }
    }
}