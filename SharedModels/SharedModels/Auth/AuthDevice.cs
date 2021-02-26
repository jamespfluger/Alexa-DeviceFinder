using System;
using System.Text;
using Amazon.DynamoDBv2.DataModel;

namespace DeviceFinder.Models.Auth
{
    /// <summary>
    /// Model coming from device
    /// </summary>
    [DynamoDBTable("DeviceFinder_AuthDevices")]
    public class AuthDevice
    {
        [DynamoDBHashKey("DeviceID")]
        public string DeviceId { get; set; }

        [DynamoDBProperty("LoginUserID")]
        public string LoginUserId { get; set; }

        [DynamoDBProperty("OneTimePassword")]
        public string OneTimePassword { get; set; }

        [DynamoDBProperty("ModifiedDate")]
        public DateTime ModifiedDate { get; set; }

        public AuthDevice() { }

        public AuthDevice(string userId, string deviceId)
        {
            LoginUserId = userId;
            DeviceId = deviceId;
            ModifiedDate = DateTime.UtcNow;
        }

        public override string ToString()
        {
            StringBuilder modelInfo = new StringBuilder();

            modelInfo.Append($"{nameof(LoginUserId)}:{LoginUserId}");
            modelInfo.Append($"{nameof(DeviceId)}:{DeviceId}");
            modelInfo.Append($"{nameof(OneTimePassword)}:{OneTimePassword}");
            modelInfo.Append($"{nameof(ModifiedDate)}:{ModifiedDate}");

            return modelInfo.ToString();
        }
    }
}
