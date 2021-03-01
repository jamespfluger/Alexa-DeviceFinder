using System;
using System.Collections.Generic;
using Amazon.DynamoDBv2.DataModel;
using DeviceFinder.Models.Auth;

namespace DeviceFinder.Models.Devices
{
    [DynamoDBTable("DeviceFinder_UserDevices")]
    public class Device
    {
        [DynamoDBHashKey("AlexaUserID")]
        public string AlexaUserId { get; set; }

        [DynamoDBRangeKey("DeviceID")]
        public string DeviceId { get; set; }

        [DynamoDBProperty("LoginUserID")]
        public string LoginUserId { get; set; }

        [DynamoDBProperty("DeviceName")]
        public string DeviceName { get; set; }

        [DynamoDBProperty("DeviceOS")]
        public DeviceOperatingSystem DeviceOs { get; set; }

        [DynamoDBProperty("ModifiedDate")]
        public DateTime ModifiedDate { get; set; }

        [DynamoDBIgnore()]
        public DeviceSettings DeviceSettings { get; set; }

        public Device() { ModifiedDate = DateTime.UtcNow; }

        public Device(AlexaUser alexaUser, AuthDevice authDevice)
        {
            AlexaUserId = alexaUser.AlexaUserId;
            DeviceId = authDevice.DeviceId;
            LoginUserId = authDevice.LoginUserId;
            ModifiedDate = DateTime.UtcNow;
            DeviceSettings = new DeviceSettings();
        }

        public override string ToString()
        {
            List<string> modelInformation = new List<string>();

            modelInformation.Add(nameof(Device.AlexaUserId) + ":" + AlexaUserId);
            modelInformation.Add(nameof(Device.DeviceId) + ":" + DeviceId);
            modelInformation.Add(nameof(Device.LoginUserId) + ":" + LoginUserId);
            modelInformation.Add(nameof(Device.DeviceName) + ":" + DeviceName);
            modelInformation.Add(nameof(Device.DeviceOs) + ":" + DeviceOs.ToString());
            modelInformation.Add(nameof(Device.ModifiedDate) + ":" + ModifiedDate);

            return string.Join("|", modelInformation);
        }
    }
}
