using System;
using System.Collections.Generic;
using Amazon.DynamoDBv2.DataModel;

namespace DeviceFinder.Models.Auth
{
    /// <summary>
    /// OTP model sent to DynamoDB
    /// </summary>
    [DynamoDBTable("DeviceFinder_AuthAlexaUsers")]
    public class AuthAlexaUser
    {
        [DynamoDBHashKey("OneTimePasscode")]
        public string OneTimePasscode { get; set; }

        [DynamoDBProperty("AlexaUserID")]
        public string AlexaUserId { get; set; }

        [DynamoDBProperty("AlexaDeviceID")]
        public string AlexaDeviceId { get; set; }

        [DynamoDBProperty("TimeToLive")]
        public long TimeToLive { get; set; }

        [DynamoDBProperty("ModifiedDate")]
        public DateTime ModifiedDate { get; set; }

        public AuthAlexaUser() { ModifiedDate = DateTime.UtcNow; }

        public override string ToString()
        {
            List<string> modelInformation = new List<string>();

            modelInformation.Add(nameof(AuthAlexaUser.OneTimePasscode) + ":" + OneTimePasscode);
            modelInformation.Add(nameof(AuthAlexaUser.AlexaUserId) + ":" + AlexaUserId);
            modelInformation.Add(nameof(AuthAlexaUser.TimeToLive) + ":" + TimeToLive);
            modelInformation.Add(nameof(AuthAlexaUser.ModifiedDate) + ":" + ModifiedDate);

            return string.Join("|", modelInformation);
        }
    }
}
