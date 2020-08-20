using Amazon.DynamoDBv2.DataModel;

namespace AlexaDeviceFinderSkill.Models
{
    [DynamoDBTable("Evergreen")]
    public class UserDevice
    {
        public UserDevice() { }

        public UserDevice(string userId, string deviceId)
        {
            this.UserId = userId;
            this.DeviceId = deviceId;
        }

        [DynamoDBHashKey("UserID")] // Partition key
        public string UserId { get; set; }

        [DynamoDBProperty("DeviceID")]
        public string DeviceId { get; set; }

        public override string ToString()
        {
            return $"{this.UserId};{ this.DeviceId}";
        }
    }
}
