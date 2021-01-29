namespace DeviceFinder.Droid.Models
{
    public class DeviceSettings
    {
        public string AlexaUserId { get; set; }

        public string DeviceId { get; set; }

        public string DeviceName { get; set; }

        public bool UseFlashlight { get; set; }

        public bool UseVibrate { get; set; }

        public bool ShouldLimitToWifi { get; set; }

        public string ConfiguredWifiSsid { get; set; }

        public bool UseVolumeOverride { get; set; }

        public int OverriddenVolumeValue { get; set; }

        public DeviceSettings()
        {
        }

        public DeviceSettings(string alexaUserId, string deviceId)
        {
            this.AlexaUserId = alexaUserId;
            this.DeviceId = deviceId;
        }
    }
}