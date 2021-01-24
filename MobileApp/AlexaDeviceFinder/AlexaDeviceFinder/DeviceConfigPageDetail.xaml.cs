using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace DeviceFinder
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class DeviceConfigPageDetail : ContentPage
    {
        public DeviceConfigPageDetail()
        {
            Application.Current.UserAppTheme = OSAppTheme.Light;
            InitializeComponent();
        }
    }
}