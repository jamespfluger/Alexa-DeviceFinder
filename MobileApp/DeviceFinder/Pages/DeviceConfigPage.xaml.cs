using DeviceFinder.Abstractions;
using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace DeviceFinder.Pages
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class DeviceConfigPage : ContentPage
    {
        public DeviceConfigPage()
        {
            DependencyForge.Get<IDebugger>().LogDebugInfo(nameof(DeviceConfigPage));
            InitializeComponent();
        }
    }
}