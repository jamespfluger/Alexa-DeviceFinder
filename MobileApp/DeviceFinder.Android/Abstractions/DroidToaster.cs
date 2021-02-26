
using Android.App;
using Android.Widget;
using DeviceFinder.Abstractions;

namespace DeviceFinder.Droid.Abstractions
{
    public class DroidToaster : IToaster
    {
        public void ShowShortToast(string message)
        {
            Toast.MakeText(Application.Context, message, ToastLength.Short).Show();
        }

        public void ShowLongToast(string message)
        {
            Toast.MakeText(Application.Context, message, ToastLength.Long).Show();
        }
    }
}
