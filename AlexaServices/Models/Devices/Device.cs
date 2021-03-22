using System;
using System.Collections.Generic;
using Amazon.DynamoDBv2.DataModel;
using DeviceFinder.Models.Auth;

namespace DeviceFinder.Models.Devices
{
    [DynamoDBTable("DeviceFinder_Devices")]
    public class Device
    {
        [DynamoDBHashKey("AlexaUserID")]
        public string AlexaUserId { get; set; }

        [DynamoDBRangeKey("DeviceID")]
        public string DeviceId { get; set; }

        [DynamoDBProperty("LoginUserID")]
        public string LoginUserId { get; set; }

        [DynamoDBProperty("FirebaseToken")]
        public string FirebaseToken { get; set; }

        [DynamoDBProperty("DeviceName")]
        public string DeviceName { get; set; }

        [DynamoDBProperty("ModifiedDate")]
        public DateTime ModifiedDate { get; }

        [DynamoDBIgnore()]
        public DeviceSettings DeviceSettings { get; set; }

        public Device()
        {
            ModifiedDate = DateTime.UtcNow;
            DeviceSettings = new DeviceSettings();
        }

        public Device(AlexaUser alexaUser, AuthData authData)
        {
            AlexaUserId = alexaUser.AlexaUserId;
            DeviceName = authData.DeviceName;
            FirebaseToken = authData.FirebaseToken;
            ModifiedDate = DateTime.UtcNow;
            DeviceSettings = new DeviceSettings();
        }

        public override string ToString()
        {
            List<string> modelInformation = new List<string>
            {
                $"{nameof(Device.DeviceName)}:{DeviceName}",
                $"{nameof(Device.AlexaUserId)}:{AlexaUserId}",
                $"{nameof(Device.DeviceId)}:{DeviceId}",
                $"{nameof(Device.LoginUserId)}:{LoginUserId}",
                $"{nameof(Device.FirebaseToken)}:{FirebaseToken}",
                $"{nameof(Device.ModifiedDate)}:{ModifiedDate}"
            };

            return string.Join("|", modelInformation);
        }
    }
}
