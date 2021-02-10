using System;
using System.Collections.Generic;
using System.Text;
using Amazon.DynamoDBv2.DataModel;
using DeviceFinder.Models.Devices;

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
            this.LoginUserId = userId;
            this.DeviceId = deviceId;
            this.ModifiedDate = DateTime.UtcNow;
        }

        public override string ToString()
        {
            StringBuilder modelInfo = new StringBuilder();

            modelInfo.Append($"{nameof(LoginUserId)}:{this.LoginUserId}");
            modelInfo.Append($"{nameof(DeviceId)}:{this.DeviceId}");
            modelInfo.Append($"{nameof(OneTimePassword)}:{this.OneTimePassword}");
            modelInfo.Append($"{nameof(ModifiedDate)}:{this.ModifiedDate}");

            return modelInfo.ToString();
        }
    }
}
