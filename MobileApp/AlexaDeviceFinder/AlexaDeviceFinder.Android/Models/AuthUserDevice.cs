namespace DeviceFinder.Droid.Models
{
    public class AuthUserDevice
    {

        public string UserId { get; set; }

        public string DeviceId { get; set; }

        public string DeviceName { get; set; }

        public string DeviceOs { get; set; } = "Android";

        public string Otp { get; set; }

        public AuthUserDevice()
        {
        }
    }
}