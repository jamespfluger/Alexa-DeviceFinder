using Android.OS;
using Android.Views;
using AndroidX.Fragment.App;

namespace DeviceFinder.Droid.Activities.Fragments
{
    public class  HomeFragment : Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container.ClearDisappearingChildren();
        View root = inflater.Inflate(Resource.Layout.fragment_home, container, false);
        return root;
    }
}}