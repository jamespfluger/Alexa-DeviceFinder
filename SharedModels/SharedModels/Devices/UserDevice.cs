using System;
using System.Collections.Generic;
using Amazon.DynamoDBv2.DataModel;
using DeviceFinder.Models.Auth;

namespace DeviceFinder.Models.Devices
{
    // Instead of using a separate table for DeviceSettings, we reduce read capacity with one object
    [DynamoDBTable("DeviceFinder_UserDevices")]
    public class UserDevice
    {
        [DynamoDBHashKey("AlexaUserID")]
        public string AlexaUserId { get; set; }

        [DynamoDBRangeKey("DeviceID")]
        public string DeviceId { get; set; }

        [DynamoDBProperty("LoginUserID")]
        public string LoginUserId { get; set; }

        [DynamoDBProperty("AlexaDeviceID")]
        public string AlexaDeviceId { get; set; }

        [DynamoDBProperty("DeviceName")]
        public string DeviceName { get; set; }

        [DynamoDBProperty("DeviceOS")]
        public DeviceOperatingSystem DeviceOs { get; set; }

        [DynamoDBProperty("ModifiedDate")]
        public DateTime ModifiedDate { get; set; }

        [DynamoDBIgnore()]
        public DeviceSettings DeviceSettings { get; set; }

        public UserDevice() { this.ModifiedDate = DateTime.UtcNow; }

        public UserDevice(AuthAlexaUser alexaUser, AuthDevice authDevice)
        {
            AlexaUserId = alexaUser.AlexaUserId;
            AlexaDeviceId = alexaUser.AlexaDeviceId;
            DeviceId = authDevice.DeviceId;
            LoginUserId = authDevice.LoginUserId;
            ModifiedDate = DateTime.UtcNow;
            DeviceSettings = new DeviceSettings();
        }

        public override string ToString()
        {
            List<string> modelInformation = new List<string>();

            modelInformation.Add(nameof(UserDevice.AlexaUserId) + ":" + AlexaUserId);
            modelInformation.Add(nameof(UserDevice.DeviceId) + ":" + DeviceId);
            modelInformation.Add(nameof(UserDevice.LoginUserId) + ":" + LoginUserId);
            modelInformation.Add(nameof(UserDevice.DeviceName) + ":" + DeviceName);
            modelInformation.Add(nameof(UserDevice.DeviceOs) + ":" + DeviceOs.ToString());
            modelInformation.Add(nameof(UserDevice.ModifiedDate) + ":" + ModifiedDate);

            return string.Join("|", modelInformation);
        }
    }
}
