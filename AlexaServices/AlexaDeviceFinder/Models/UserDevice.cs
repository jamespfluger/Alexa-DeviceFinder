using Amazon.DynamoDBv2.DataModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace AlexaDeviceFinder.Models
{
    [DynamoDBTable("UserDevice")]
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
        [DynamoDBRangeKey("DeviceID")] // Sort key
        public string DeviceId { get; set; }
    }
}
