using System;
using Amazon.DynamoDBv2.DataModel;

namespace DeviceFinder.Models.Devices
{
    [DynamoDBTable("DeviceFinder_DeviceSettings")]
    public class DeviceSettings
    {
        [DynamoDBHashKey("AlexaUserID")]
        public string AlexaUserId { get; set; }

        [DynamoDBRangeKey("DeviceID")]
        public string DeviceId { get; set; }

        [DynamoDBProperty("DeviceName")]
        public string DeviceName { get; set; }

        [DynamoDBProperty("UseFlashlight")]
        public bool UseFlashlight { get; set; }

        [DynamoDBProperty("UseVibrate")]
        public bool UseVibrate { get; set; }

        [DynamoDBProperty("ShouldLimitToWifi")]
        public bool ShouldLimitToWifi { get; set; }

        [DynamoDBProperty("ConfiguredWifiSsid")]
        public string ConfiguredWifiSsid { get; set; }

        [DynamoDBProperty("UseVolumeOverride")]
        public bool UseVolumeOverride { get; set; }

        [DynamoDBProperty("OverriddenVolumeValue")]
        public int OverriddenVolumeValue { get; set; }

        [DynamoDBProperty("ModifiedDate")]
        public DateTime ModifiedDate { get; }

        public DeviceSettings()
        {
            ModifiedDate = DateTime.UtcNow;
        }
    }
}
