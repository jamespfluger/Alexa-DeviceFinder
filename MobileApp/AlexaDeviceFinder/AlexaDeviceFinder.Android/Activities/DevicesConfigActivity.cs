using System.Collections.Generic;
using Android.Content;
using Android.OS;
using Android.Views;
using Android.Widget;
using AndroidX.AppCompat.Widget;
using AndroidX.DrawerLayout.Widget;
using AndroidX.Fragment.App;
using AndroidX.Navigation;
using AndroidX.Navigation.UI;
using DeviceFinder.Droid.Activities.Fragments;
using DeviceFinder.Droid.API;
using DeviceFinder.Droid.Models;
using DeviceFinder.Droid.Utilities;
using Google.Android.Material.Navigation;
using RestSharp;

namespace DeviceFinder.Droid.Activities
{
    public class DevicesConfigActivity : AndroidX.AppCompat.App.AppCompatActivity
    {
        private AppBarConfiguration mAppBarConfiguration;
        private PreferencesManager preferencesManager;
        private List<UserDevice> allUserDevices = new List<UserDevice>();


        protected void onCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            preferencesManager = new PreferencesManager(ApplicationContext);

            SetContentView(Resource.Layout.activity_device_navigation);
            AndroidX.AppCompat.Widget.Toolbar toolbar = this.FindViewById(Resource.Id.toolbar) as AndroidX.AppCompat.Widget.Toolbar;
            DrawerLayout drawer = this.FindViewById(Resource.Id.drawer_layout) as DrawerLayout;
            Google.Android.Material.Navigation.NavigationView navigationView = this.FindViewById(Resource.Id.nav_view) as Google.Android.Material.Navigation.NavigationView;

            SetSupportActionBar(toolbar);

            // Passing each menu ID as a set of Ids because each menu should be considered as top level destinations.
            mAppBarConfiguration = new AppBarConfiguration.Builder(Resource.Id.nav_home).SetOpenableLayout(drawer).Build();

            NavController navController = Navigation.FindNavController(this, Resource.Id.nav_host_fragment);
            NavigationUI.SetupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.SetupWithNavController(navigationView, navController);

            // Create the normal menu items
            createDefaultMenuItems(drawer, navigationView.Menu);
            getDevices();
        }


        public bool onCreateOptionsMenu(IMenu menu)
        {
            // Inflate the menu; this adds items to the action bar if it is present.
            MenuInflater.Inflate(Resource.Menu.kebab_menu, menu);
            return true;
        }


        public bool onSupportNavigateUp()
        {
            NavController navController = Navigation.FindNavController(this, Resource.Id.nav_host_fragment);
            return NavigationUI.NavigateUp(navController, mAppBarConfiguration) || base.OnSupportNavigateUp();
        }

        private void getDevices()
        {
            ApiService managementApi = new ApiService();
            List<UserDevice> userCall = managementApi.GetAllUserDevices(preferencesManager.GetUserId());
            //userCall.Enqueue(new Callback<List<UserDevice>>() { }

        }

        public void onGetDevicesResponse(IRestResponse response)//Call call, Response response)
        {
            if (response.IsSuccessful)
            {
                allUserDevices = new List<UserDevice>();//(List<UserDevice>)response.Content;
                populateDeviceList();
            }
            else
            {
                Toast.MakeText(this, response.StatusCode + " - " + response.ErrorMessage, ToastLength.Short).Show();
            }
        }



        public void onGetDevicesFailure()//Call call, Throwable t)
        {

        }

        private void createDefaultMenuItems(DrawerLayout drawer, IMenu menu)
        {
            IMenuItem homeItem = menu.Add(Resource.Id.defaultGroup, View.GenerateViewId(), Menu.None, "Home");
            IMenuItem aboutItem = menu.Add(Resource.Id.defaultGroup, View.GenerateViewId(), Menu.None, "About");

            homeItem.SetOnMenuItemClickListener(new DeviceClickListener(this, new HomeFragment()));
            aboutItem.SetOnMenuItemClickListener(new DeviceClickListener(this, new AboutFragment()));
        }

        private void clearAllChecks(IMenu menu)
        {
            for (int i = 0; i < menu.Size(); i++)
            {
                menu.GetItem(i).SetChecked(false);
            }
        }

        private void populateDeviceList()
        {
            NavigationView navigationView = this.FindViewById(Resource.Id.nav_view) as NavigationView;
            IMenu menu = navigationView.Menu;

            foreach (UserDevice device in allUserDevices)
            {
                IMenuItem newDeviceMenuItem = menu.Add(Resource.Id.devicesGroup, View.GenerateViewId(), Menu.None, device.DeviceName);
                newDeviceMenuItem.SetOnMenuItemClickListener(new DeviceClickListener(this, (new DeviceConfigFragment(device))));
            }
        }

        public class DeviceClickListener : Java.Lang.Object, IMenuItemOnMenuItemClickListener
        {
            private FragmentActivity _fragmentActivity;
            private Fragment _newFragment;

            public DeviceClickListener(FragmentActivity fragmentActivity, Fragment newFragment)
            {
                this._fragmentActivity = fragmentActivity;
                this._newFragment = newFragment;
            }

            public bool OnMenuItemClick(IMenuItem item)
            {
                DrawerLayout drawer = _fragmentActivity.FindViewById(Resource.Id.drawer_layout) as DrawerLayout;
                NavigationView navigationView = _fragmentActivity.FindViewById(Resource.Id.nav_view) as NavigationView;
                IMenu menu = navigationView.Menu;

                FragmentTransaction ft = _fragmentActivity.SupportFragmentManager.BeginTransaction();
                ft.Replace(Resource.Id.nav_host_fragment, _newFragment, "").Commit();
                for (int i = 0; i < menu.Size(); i++)
                {
                    menu.GetItem(i).SetChecked(false);
                }
                item.SetChecked(true);
                drawer.Close();

                return true;
            }
        }
    }
}