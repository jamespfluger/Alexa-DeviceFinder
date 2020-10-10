namespace DeviceFinder.Models.Devices
{
    public class UserSettings
    {
        public bool UseVolumeOverride { get; set; }

        public int Volume { get; set; }

        public bool ShouldUseFlash { get; set; }

        public bool ShouldVibrate { get; set; }
    }
}
