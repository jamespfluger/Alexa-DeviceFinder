using DeviceFinder.Abstractions;
using SimpleInjector;
using Xamarin.Extensions.GoogleAuth;

namespace DeviceFinder.Droid.Abstractions
{
    public static class DependencyForgeInjector
    {
        public static void Inject()
        {
            Container container = new Container();

            container.Register<IAuthManager, DroidAmazonAuthManager>();
            container.Register<IPermissionsManager, DroidPermissionsManager>();
            container.Register<IDebugger, DroidDebugger>();

            DependencyForge.Container = container;
        }
    }
}
