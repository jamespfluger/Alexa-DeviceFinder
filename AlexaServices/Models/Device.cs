using System;
using System.Collections.Generic;
using Amazon.DynamoDBv2.DataModel;

namespace DeviceFinder.Models
{
    [DynamoDBTable("DeviceFinder_Devices")]
    public class Device : IModel
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

        [DynamoDBProperty("UseFlashlight")]
        public bool UseFlashlight { get; set; }

        [DynamoDBProperty("UseVibrate")]
        public bool UseVibrate { get; set; }

        [DynamoDBProperty("UseOnWifiOnly")]
        public bool UseOnWifiOnly { get; set; }

        [DynamoDBProperty("ConfiguredWifiSsid")]
        public string ConfiguredWifiSsid { get; set; }

        [DynamoDBProperty("UseVolumeOverride")]
        public bool UseVolumeOverride { get; set; }

        [DynamoDBProperty("VolumeOverrideValue")]
        public int VolumeOverrideValue { get; set; }

        [DynamoDBProperty("CreatedDate")]
        public DateTime CreatedDate { get; set; }

        [DynamoDBProperty("ModifiedDate")]
        public DateTime ModifiedDate { get; set; }

        public bool IsModelValid()
        {
            return !string.IsNullOrEmpty(AlexaUserId)
                && !string.IsNullOrEmpty(FirebaseToken)
                && !string.IsNullOrEmpty(DeviceName);
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
