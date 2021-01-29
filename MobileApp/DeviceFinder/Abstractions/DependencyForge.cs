using System;
using SimpleInjector;

namespace DeviceFinder.Abstractions
{
    public class DependencyForge
    {
        public static Container Container { get; set; }

        public static T Get<T>() where T : class
        {
            if (Container == null)
                throw new InvalidOperationException("Container has not yet been initialized.");

            return Container.GetInstance<T>();
        }
    }
}
