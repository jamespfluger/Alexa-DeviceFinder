using System;
using Android.Content;
using Android.OS;
using AndroidX.AppCompat.App;
using DeviceFinder.Droid.Models;
using DeviceFinder.Droid.Utilities;

namespace DeviceFinder.Droid.Activities
{
    public class LaunchActivity : AppCompatActivity
    {
        protected void onCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            PreferencesManager preferencesManager = new PreferencesManager(ApplicationContext);
            preferencesManager.RefreshDeviceId();
            CommonData.Init(Assets);

            SetContentView(Resource.Layout.activity_launch);
            selectActivityToLaunch();
        }

        private void selectActivityToLaunch()
        {

            //Scope[] scopes = { ProfileScope.UserId() };

            //AuthorizationManager.GetToken(this, scopes, new Listener<AuthorizeResult, AuthError>() 
        }

        public void onSuccess()//AuthorizeResult result)
        {
            PermissionsRequester permissionsRequester = new PermissionsRequester();
            permissionsRequester.RequestPermissions(this);

            //if (result.GetAccessToken() != null)
            {
                AmazonLoginHelper.SetUserId(ApplicationContext);
                switchToActivity(typeof(LoginActivity));
            }
            //else
            {
                switchToActivity(typeof(LoginActivity));
            }
        }

        public void onError()//AuthError ae)
        {
            switchToActivity(typeof(LoginActivity));
        }

        private void switchToActivity(Type newActivity)
        {
            Intent newIntent = new Intent(this, newActivity);
            StartActivity(newIntent);
            Finish();
        }
    }
}