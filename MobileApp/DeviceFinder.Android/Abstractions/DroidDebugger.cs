using Android.Util;
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