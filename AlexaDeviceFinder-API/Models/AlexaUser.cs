using System;
using Amazon.DynamoDBv2.DataModel;

namespace DeviceFinder.Models
{
    /// <summary>
    /// Model formed from Alexa inputs
    /// </summary>
    [DynamoDBTable("AlexaUsers")]
    public class AlexaUser
    {
        [DynamoDBHashKey("AlexaUserID")]
        public string AlexaUserId { get; set; }

        [DynamoDBProperty("AlexaDeviceID")]
        public string AlexaDeviceId { get; set; }

        [DynamoDBProperty("OneTimePassword")]
        public string OneTimePassword { get; set; }

        [DynamoDBProperty("ModifiedDate")]
        public DateTime ModifiedDate { get; set; }

        public AlexaUser() { this.ModifiedDate = DateTime.UtcNow; }
    }
}
