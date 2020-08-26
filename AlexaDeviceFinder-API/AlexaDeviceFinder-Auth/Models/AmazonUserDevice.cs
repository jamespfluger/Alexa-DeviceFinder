using System;
using Amazon.DynamoDBv2.DataModel;

namespace AlexaDeviceFinderAuth.Models
{
    [DynamoDBTable("AmazonUserDevices")]
    public class AmazonUserDevice
    {
        public enum DeviceOperatingSystem
        {
            Unknown,
            Android,
            Apple
        }

        [DynamoDBHashKey("AmazonUserID")]
        public string AmazonUserId { get; set; }

        [DynamoDBProperty("DeviceID")]
        public string DeviceId { get; set; }

        [DynamoDBProperty("DeviceOS")]
        public DeviceOperatingSystem DeviceOs { get; set; }

        [DynamoDBProperty("ModifiedDate")]
        public DateTime ModifiedDate { get; set; }

        public AmazonUserDevice()
        {
            this.ModifiedDate = DateTime.UtcNow;
        }

        public AmazonUserDevice(string userId, string deviceId)
        {
            this.AmazonUserId = userId;
            this.DeviceId = deviceId;
            this.ModifiedDate = DateTime.UtcNow;
        }

        public override string ToString()
        {
            return $"{this.AmazonUserId}:{ this.DeviceId}";
        }
    }
}
