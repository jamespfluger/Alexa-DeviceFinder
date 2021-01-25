using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;
using Xamarin.LoginWithAmazon;

namespace DeviceFinder.Droid.Listeners
{
    public class LoginListener : AuthorizeListener
    {
        private Context _context;

        public LoginListener(Context context)
        {
            this._context = context;
        }

        public override void OnSuccess(Java.Lang.Object authResult)
        {
            User user = authResult as User;
            //Toast.MakeText(_context, $"Successfully retrieved user.{authResult}", ToastLength.Short).Show();
        }

        public override void OnSuccess(AuthorizeResult authResult)
        {
            //Toast.MakeText(_context, $"Somehow did an auth success", ToastLength.Short).Show();
        }

        public override void OnError(AuthError authError)
        {
            //Toast.MakeText(_context, "Failed to retrieve token.", ToastLength.Short).Show();
        }

        public override void OnCancel(AuthCancellation authCancel)
        {
            throw new NotImplementedException();
        }
    }
}