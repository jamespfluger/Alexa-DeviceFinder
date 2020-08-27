using System;
using System.Collections.Generic;
using System.Diagnostics.Contracts;
using System.Text;
using Amazon.DynamoDBv2.DataModel;

namespace DeviceFinder.Models
{
    [DynamoDBTable("AlexaAuthUsers")]
    public class AlexaAuthUser
    {
        [DynamoDBHashKey("OneTimePassword")]
        public string OneTimePassword { get; set; }

        [DynamoDBProperty("AlexaUserID")]
        public string AlexaUserId { get; set; }

        [DynamoDBProperty("TimeToLive")]
        public long TimeToLive { get; set; }

        public AlexaAuthUser() { }

        public AlexaAuthUser(string otp, string alexaUserId, long timeToLive)
        {
            this.OneTimePassword = otp;
            this.AlexaUserId = alexaUserId;
            this.TimeToLive = timeToLive;
        }
    }
}
