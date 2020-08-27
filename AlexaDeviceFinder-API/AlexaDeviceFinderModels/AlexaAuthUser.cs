using System;
using Amazon.DynamoDBv2.DataModel;

namespace DeviceFinder.Models
{
    /// <summary>
    /// OTP model sent to DynamoDB
    /// </summary>
    [DynamoDBTable("AlexaAuthUsers")]
    public class AlexaAuthUser
    {
        [DynamoDBHashKey("OneTimePassword")]
        public int OneTimePassword { get; set; }

        [DynamoDBProperty("AlexaUserID")]
        public string AlexaUserId { get; set; }

        [DynamoDBProperty("TimeToLive")]
        public long TimeToLive { get; set; }

        [DynamoDBProperty("ModifiedDate")]
        public DateTime ModifiedDate { get; set; }

        public AlexaAuthUser() { this.ModifiedDate = DateTime.UtcNow; }
    }
}
