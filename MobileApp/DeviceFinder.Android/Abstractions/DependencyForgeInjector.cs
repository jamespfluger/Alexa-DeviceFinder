using Android.Content;
using DeviceFinder.Abstractions;
using SimpleInjector;
using Xamarin.LoginWithAmazon.API;

namespace DeviceFinder.Droid.Abstractions
{
    public static class DependencyForgeInjector
    {
        public static void Inject(RequestContext requestContext)
        {
            Container container = new Container();

            container.RegisterInstance<IAmazonAuthManager>(new DroidAmazonAuthManager(requestContext));
            container.Register<IPermissionsManager, DroidPermissionsManager>();
            container.Register<IDebugger, DroidDebugger>();

            DependencyForge.Container = container;
        }
    }
}