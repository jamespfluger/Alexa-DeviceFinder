using System;
using Android.Content;
using DeviceFinder.Abstractions;
using DeviceFinder.Droid.Listeners;
using Xamarin.Essentials;
using Xamarin.LoginWithAmazon;
using Xamarin.LoginWithAmazon.API;

namespace DeviceFinder.Droid.Abstractions
{
    public class DroidAmazonAuthManager : IAmazonAuthManager
    {
        private RequestContext requestContext;

        public DroidAmazonAuthManager(RequestContext requestContext)
        {
            this.requestContext = requestContext;
        }

        public void RefreshUser()
        {
            User.Fetch(Platform.AppContext, new FetchUserListener());
        }

        public void SignIn()
        {
            AuthorizationManager.Authorize(new AuthorizeRequest.Builder(requestContext)
                                                               .AddScope(ProfileScope.UserId())
                                                               .Build());
        }

        public void SignOut()
        {
            AuthorizationManager.SignOut(Platform.AppContext, new LoginListener());
        }

        public void RefreshToken()
        {
            RequestContext requestContext = RequestContext.Create(Platform.AppContext);

            LoginListener amazonLoginListener = new LoginListener();
            requestContext.RegisterListener(amazonLoginListener);

            AuthorizationManager.GetToken(Platform.AppContext, new IScope[] { ProfileScope.UserId() }, new LoginListener());
        }
    }
}