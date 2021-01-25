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
using Xamarin.LoginWithAmazon.Additions;

namespace DeviceFinder.Droid.Listeners
{
    public class FetchUserListener : UserListener
    {
        private Context _context;

        public FetchUserListener(Context context)
        {
            this._context = context;
        }

        public override void OnSuccess(Java.Lang.Object p0)
        {

        }

        public override void OnError(Java.Lang.Object p0)
        {

        }

        public override void OnCancel(Java.Lang.Object p0)
        {

        }
    }
}
