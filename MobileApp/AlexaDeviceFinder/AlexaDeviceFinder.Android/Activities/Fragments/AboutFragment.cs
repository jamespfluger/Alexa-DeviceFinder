using Android.OS;
using Android.Views;
using AndroidX.Fragment.App;

namespace DeviceFinder.Droid.Activities.Fragments
{
    public class AboutFragment : Fragment
    {

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            container.ClearDisappearingChildren();
            ChildFragmentManager.PopBackStack();
            View root = inflater.Inflate(Resource.Layout.fragment_about, container, false);
            return root;
        }
    }
}