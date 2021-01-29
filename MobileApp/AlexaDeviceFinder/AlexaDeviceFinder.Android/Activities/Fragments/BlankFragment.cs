using Android.OS;
using Android.Views;
using AndroidX.Fragment.App;

namespace DeviceFinder.Droid.Activities.Fragments
{
    public class BlankFragment : Fragment
    {
        public BlankFragment()
        {
            // Required empty public constructor
        }

        public static BlankFragment newInstance()
        {
            BlankFragment fragment = new BlankFragment();
            fragment.Arguments = new Bundle();
            return fragment;
        }

        public void onCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
        }

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
        {
            return inflater.Inflate(Resource.Layout.fragment_blank, container, false);
        }
    }
}