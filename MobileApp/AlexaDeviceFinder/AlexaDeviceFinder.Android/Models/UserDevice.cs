namespace DeviceFinder.Droid.Models
{
    public class UserDevice
    {
        public string AlexaUserId { get; set; }

        public string DeviceId { get; set; }

        public string AmazonUserId { get; set; }

        public string AlexaDeviceId { get; set; }

        public string DeviceName { get; set; }

        public string DeviceOs { get; set; }

        public string ModifiedDate { get; set; }

        public DeviceSettings DeviceSettings { get; set; }

        public UserDevice()
        {
        }
    }
}