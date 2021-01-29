using System;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Views;
using Android.Widget;
using DeviceFinder.Droid.Utilities;

namespace DeviceFinder.Droid.Activities
{
    public class LoginActivity : Activity
    {
        //private RequestContext requestContext;
        private Button loginButton;
        private TextView loginText;
        private ProgressBar loginProgressBar;


        protected void onCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            // TODO: remove before release
            AmazonLoginHelper.SignOut(ApplicationContext);

            //requestContext = RequestContext.Create(ApplicationContext);
            //requestContext.RegisterListener(new AuthorizeListener());

            SetContentView(Resource.Layout.activity_login);
            initializeUI();
        }

        public void onSignOutSuccess()//AuthorizeResult authorizeResult)
        {
            Toast.MakeText(ApplicationContext, "Successfully logged into Amazon.", ToastLength.Short).Show();
            switchToActivity(typeof(NameActivity));
        }

        public void onSignOutError()//AuthError authError)
        {
            Toast.MakeText(ApplicationContext, "Error logging in. Please try again.", ToastLength.Short).Show();
            setLoggingInState(false);
        }

        public void onCancel()//AuthCancellation authCancellation)
        {
            Toast.MakeText(ApplicationContext, "Login cancelled.", ToastLength.Short).Show();
            setLoggingInState(false);
        }

        protected void onResume()
        {
            base.OnResume();
            //requestContext.OnResume();
        }

        private void initializeUI()
        {
            loginProgressBar = this.FindViewById(Resource.Id.loginProgressBar) as ProgressBar;
            loginButton = this.FindViewById(Resource.Id.loginButton) as Button;
            loginText = this.FindViewById(Resource.Id.loginText) as TextView;

            //
            loginButton.Text = ("LOGIN WITH AMAZON");
            loginButton.SetCompoundDrawablesRelativeWithIntrinsicBounds(Resource.Drawable.amazon_logo, 0, 0, 0);

            /* TODO: verify this is not needed
            loginButton.Text = ("CONNECT WITH ALEXA");
            loginButton.SetCompoundDrawablesRelativeWithIntrinsicBounds(R.Drawable.Amazon_alexa_logo, 0, 0, 0); */

            loginButton.Click += onLoginButtonClick; //.SetOnClickListener(new View.OnClickListener());
        }

        public void onLoginButtonClick(object sender, EventArgs args)
        {
            //AuthorizationManager.Authorize(
            //        new AuthorizeRequest.Builder(requestContext)
            //                .AddScopes(ProfileScope.UserId())
            //                .Build()
            //);
            setLoggingInState(true);
        }

        private void setLoggingInState(bool loggingIn)
        {
            if (loggingIn)
            {
                loginButton.Visibility = ViewStates.Gone;
                loginText.Visibility = ViewStates.Gone;
                loginProgressBar.Visibility = ViewStates.Visible;
            }
            else
            {
                loginButton.Visibility = ViewStates.Visible;
                loginText.Visibility = ViewStates.Visible;
                loginProgressBar.Visibility = ViewStates.Gone;
            }
        }

        private void switchToActivity(Type newActivity)
        {
            Intent newIntent = new Intent(this, newActivity);
            StartActivity(newIntent);
            Finish();
        }
    }
}