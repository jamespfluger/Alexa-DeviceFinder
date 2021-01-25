using System;
using Android.Content;
using Android.OS;
using Android.Widget;
using DeviceFinder.Abstractions;
using DeviceFinder.Droid.Listeners;
using Xamarin.Essentials;
using Xamarin.LoginWithAmazon;
using Xamarin.LoginWithAmazon.Additions;
using Xamarin.LoginWithAmazon.API;
using Xamarin.LoginWithAmazon.Interactive;
using Xamarin.LoginWithAmazon.Thread;

namespace DeviceFinder.Droid.Abstractions
{
    public class LoginWithAmazonManager : ILoginWithAmazonManager
    {
        private Context _context;

        public LoginWithAmazonManager(Context context)
        {
            this._context = context;
        }

        public AmazonUser GetUser()
        {
            User.Fetch(_context, new FetchUserListener(_context));

            return null;
        }

        public AuthResult SignIn()
        {
            RequestContext requestContext = RequestContext.Create(_context);

            LoginListener amazonLoginListener = new LoginListener(_context);
            requestContext.RegisterListener(amazonLoginListener);

            AuthorizationManager.Authorize(new AuthorizeRequest.Builder(requestContext)
                                                               .AddScope(ProfileScope.UserId())
                                                               .Build());

            return null;
        }

        public AuthResult SignOut()
        {
            throw new NotImplementedException();
        }
    }
}