using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using Android.Content.Res;

namespace DeviceFinder.Droid.Utilities
{
    public static class CommonData
    {
        public static HashSet<string> Names { get; private set; }

        // Amazon won't tell us what first names are valid, so we need to check locally
        public static void Init(AssetManager assets)
        {
            Task.Run(() =>
            {
                using (StreamReader sr = new StreamReader(assets.Open("names.txt")))
                {
                    string rawNamesContent = sr.ReadToEnd();
                    Names = rawNamesContent.Split('\n').ToHashSet();
                }
            });
        }
    }
}