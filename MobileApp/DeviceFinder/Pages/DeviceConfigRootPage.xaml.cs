using DeviceFinder.API;
using DeviceFinder.Utility;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;
using Device = DeviceFinder.Models.Devices.Device;

namespace DeviceFinder.Pages
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class DeviceConfigRootPage : FlyoutPage
    {
        public DeviceConfigRootPage()
        {
            InitializeComponent();

            deviceConfigSidePage.menuItemsListView.ItemSelected += OnItemSelected;
        }

        private void OnItemSelected(object sender, SelectedItemChangedEventArgs e)
        {
            if (e.SelectedItem is DeviceConfigPageItem item)
            {
                Detail = new NavigationPage((Page)Activator.CreateInstance(item.TargetType));
                deviceConfigSidePage.menuItemsListView.SelectedItem = null;
                IsPresented = false;
            }
        }
    }
}