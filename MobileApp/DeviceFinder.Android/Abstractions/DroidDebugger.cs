using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Util;
using Android.Views;
using Android.Widget;
using DeviceFinder.Abstractions;
using Xamarin.Essentials;

namespace DeviceFinder.Droid.Abstractions
{
    public class DroidDebugger : IDebugger
    {
        private const string debugTag = "DEVICEFINDERDROID";

        public void LogDebugInfo(string text)
        {
            LogInfo($"LOG --- {text} --- END");
            LogInfo($"Component: {Platform.CurrentActivity.ComponentName}");
            LogInfo($"LocalClass: {Platform.CurrentActivity.LocalClassName}");
            LogInfo($"START --- {text} --- START");
        }

        private void LogInfo(string text)
        {
            Log.Debug(debugTag, text);
        }
    }
}