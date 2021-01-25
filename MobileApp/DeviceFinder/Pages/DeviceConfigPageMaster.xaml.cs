using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading.Tasks;

using Xamarin.Forms;
using Xamarin.Forms.Xaml;

namespace DeviceFinder
{
    [XamlCompilation(XamlCompilationOptions.Compile)]
    public partial class DeviceConfigPageMaster : ContentPage
    {
        public ListView ListView;

        public DeviceConfigPageMaster()
        {
            InitializeComponent();

            BindingContext = new DeviceConfigPageMasterViewModel();
            ListView = MenuItemsListView;
        }

        class DeviceConfigPageMasterViewModel : INotifyPropertyChanged
        {
            public ObservableCollection<DeviceConfigPageMasterMenuItem> MenuItems { get; set; }

            public DeviceConfigPageMasterViewModel()
            {
                MenuItems = new ObservableCollection<DeviceConfigPageMasterMenuItem>(new[]
                {
                    new DeviceConfigPageMasterMenuItem { Id = 0, Title = "Page 1" },
                    new DeviceConfigPageMasterMenuItem { Id = 1, Title = "Page 2" },
                    new DeviceConfigPageMasterMenuItem { Id = 2, Title = "Page 3" },
                    new DeviceConfigPageMasterMenuItem { Id = 3, Title = "Page 4" },
                    new DeviceConfigPageMasterMenuItem { Id = 4, Title = "Page 5" },
                });
            }

            #region INotifyPropertyChanged Implementation
            public event PropertyChangedEventHandler PropertyChanged;
            void OnPropertyChanged([CallerMemberName] string propertyName = "")
            {
                if (PropertyChanged == null)
                    return;

                PropertyChanged.Invoke(this, new PropertyChangedEventArgs(propertyName));
            }
            #endregion
        }
    }
}