using System.Collections.Generic;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;
using DeviceFinder.Models.Devices;
using Device = DeviceFinder.Models.Devices.Device;
using System.Collections.ObjectModel;
using DeviceFinder.API;
using DeviceFinder.Utility;

namespace DeviceFinder.Pages
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class DeviceConfigSidePage : ContentPage
    {
        public DeviceConfigSidePage()
        {
            InitializeComponent();
        }
    }
}