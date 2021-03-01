using System;
using System.Collections.Generic;
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

        [DynamoDBRangeKey("LoginUserID")]
        public string LoginUserId { get; set; }

        [DynamoDBProperty("OneTimePasscode")]
        public string OneTimePasscode { get; set; }

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
            List<string> modelInfo = new List<string>
            {
                nameof(LoginUserId) + ":" + LoginUserId,
                nameof(DeviceId) + ":" + DeviceId,
                nameof(OneTimePasscode) + ":" + OneTimePasscode,
                nameof(ModifiedDate) + ":" + ModifiedDate
            };

            return string.Join("|", modelInfo);
        }
    }
}
