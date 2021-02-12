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

            container.RegisterInstance<IGoogleAuthProvider>(new AndroidGoogleAuthProvider());
            container.Register<IToaster, DroidToaster>();
            container.Register<IDebugger, DroidDebugger>();
            container.Register<IPermissionsManager, DroidPermissionsManager>();

            DependencyForge.Container = container;
        }
    }
}
