namespace DeviceFinder.Models.Devices
{
    public class DeviceSettings
    {
        public bool UseVolumeOverride { get; set; }

        public int Volume { get; set; }

        public bool ShouldUseFlash { get; set; }

        public bool ShouldUseVibrate { get; set; }

        public bool ShouldLimitToWifi { get; set; }

        public string ConfiguredWifiSsid { get; set; }
    }
}
