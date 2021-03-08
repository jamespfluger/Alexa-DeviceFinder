using System;
using System.Collections.Generic;
using Amazon.DynamoDBv2.DataModel;

namespace DeviceFinder.Models.Auth
{
    /// <summary>
    /// OTP model sent to DynamoDB
    /// </summary>
    [DynamoDBTable("DeviceFinder_AlexaUsers")]
    public class AlexaUser
    {
        [DynamoDBHashKey("OneTimePasscode")]
        public string OneTimePasscode { get; set; }

        [DynamoDBProperty("AlexaUserID")]
        public string AlexaUserId { get; set; }

        [DynamoDBProperty("TimeToLive")]
        public long TimeToLive { get; set; }

        [DynamoDBProperty("ModifiedDate")]
        public DateTime ModifiedDate { get; }

        public AlexaUser() { ModifiedDate = DateTime.UtcNow; }

        public override string ToString()
        {
            List<string> modelInfo = new List<string>
            {
                $"{nameof(OneTimePasscode)}:{OneTimePasscode}",
                $"{nameof(AlexaUserId)}:{AlexaUserId}",
                $"{nameof(TimeToLive)}:{TimeToLive}",
                $"{nameof(ModifiedDate)}:{ModifiedDate}"
            };

            return string.Join("|", modelInfo);
        }
    }
}
