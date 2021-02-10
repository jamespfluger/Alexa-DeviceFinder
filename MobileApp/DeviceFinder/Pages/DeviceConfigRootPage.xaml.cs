using System;
using DeviceFinder.Abstractions;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace DeviceFinder.Pages
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class DeviceConfigRootPage : FlyoutPage
    {
        public DeviceConfigRootPage()
        {
            InitializeComponent();
            DependencyForge.Get<IDebugger>().LogDebugInfo(nameof(DeviceConfigRootPage));

            this.deviceConfigSidePage.menuItemsListView.ItemSelected += OnItemSelected;
        }

        private void OnItemSelected(object sender, SelectedItemChangedEventArgs e)
        {
            if (e.SelectedItem is DeviceConfigPageItem item)
            {
                this.Detail = new NavigationPage((Page)Activator.CreateInstance(item.TargetType));
                this.deviceConfigSidePage.menuItemsListView.SelectedItem = null;
                this.IsPresented = false;
            }
        }
    }
}