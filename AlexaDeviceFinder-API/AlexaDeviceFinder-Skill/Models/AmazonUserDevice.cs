using Amazon.DynamoDBv2.DataModel;

namespace AlexaDeviceFinderSkill.Models
{
    [DynamoDBTable("AmazonUserDevices")]
    public class AmazonUserDevice
    {
        public enum DeviceOperatingSystem
        {
            Android,
            Apple
        }

        [DynamoDBHashKey("AmazonUserID")]
        public string AmazonUserId { get; set; }

        [DynamoDBProperty("DeviceID")]
        public string DeviceId { get; set; }

        [DynamoDBProperty("DeviceOS")]
        public DeviceOperatingSystem DeviceOs { get; set; }

        public AmazonUserDevice() { }

        public AmazonUserDevice(string userId, string deviceId)
        {
            this.AmazonUserId = userId;
            this.DeviceId = deviceId;
        }
    }
}
