using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Threading.Tasks;

namespace DeviceFinder.Utility
{
    public static class CommonData
    {
        public static HashSet<string> Names { get; private set; }

        // Amazon won't tell us what first names are valid, so we need to check locally
        public static void Init()
        {
            Task.Run(() =>
            {
                Assembly currentAssembly = IntrospectionExtensions.GetTypeInfo(typeof(CommonData)).Assembly;
                Stream resourceStream = currentAssembly.GetManifestResourceStream("DeviceFinder.names.txt");
                
                using (StreamReader reader = new StreamReader(resourceStream))
                {
                    string rawNamesContent = reader.ReadToEnd();
                    Names = new HashSet<string>(rawNamesContent.Split('\n'));
                }
            });
        }
    }
}