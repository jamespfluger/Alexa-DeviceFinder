using System;
using System.Collections.Generic;
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

        [DynamoDBProperty("AmazonUserID")]
        public string AmazonUserId { get; set; }

        [DynamoDBProperty("DeviceName")]
        public string DeviceName { get; set; }

        [DynamoDBProperty("DeviceOS")]
        public DeviceOperatingSystem DeviceOs { get; set; }

        [DynamoDBProperty("OneTimePassword")]
        public string OneTimePassword { get; set; }

        [DynamoDBProperty("DeviceOS")]
        public DeviceOperatingSystem DeviceOs { get; set; }

        [DynamoDBProperty("ModifiedDate")]
        public DateTime ModifiedDate { get; set; }
        public static object DeviceOperatingSystem { get; internal set; }

        public AuthDevice() { this.ModifiedDate = DateTime.UtcNow; }

        public AuthDevice(string userId, string deviceId)
        {
            this.AmazonUserId = userId;
            this.DeviceId = deviceId;
            this.ModifiedDate = DateTime.UtcNow;
        }

        public override string ToString()
        {
            List<string> modelInformation = new List<string>();

            modelInformation.Add(nameof(AuthDevice.AmazonUserId) + ":" + AmazonUserId);
            modelInformation.Add(nameof(AuthDevice.DeviceId) + ":" + DeviceId);
            modelInformation.Add(nameof(AuthDevice.DeviceName) + ":" + DeviceName);
            modelInformation.Add(nameof(AuthDevice.DeviceOs) + ":" + DeviceOs.ToString());
            modelInformation.Add(nameof(AuthDevice.OneTimePassword) + ":" + OneTimePassword);
            modelInformation.Add(nameof(AuthDevice.ModifiedDate) + ":" + ModifiedDate);

            return string.Join('|', modelInformation);
        }
    }
}
