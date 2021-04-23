using System;
using System.Collections.Generic;
using Amazon.DynamoDBv2.DataModel;

namespace DeviceFinder.Models.Devices
{
    [DynamoDBTable("DeviceFinder_DeviceSettings")]
    public class DeviceSettings : IModel
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

        public bool IsModelValid()
        {
            return !string.IsNullOrEmpty(AlexaUserId) && !string.IsNullOrEmpty(DeviceId);
        }

        public override string ToString()
        {
            List<string> modelInformation = new List<string>
            {
                $"{nameof(DeviceSettings.AlexaUserId)}:{AlexaUserId}",
                $"{nameof(DeviceSettings.DeviceId)}:{DeviceId}",
                $"{nameof(DeviceSettings.DeviceName)}:{DeviceName}",
                $"{nameof(DeviceSettings.UseFlashlight)}:{UseFlashlight}",
                $"{nameof(DeviceSettings.UseVibrate)}:{UseVibrate}",
                $"{nameof(DeviceSettings.ShouldLimitToWifi)}:{ShouldLimitToWifi}",
                $"{nameof(DeviceSettings.ConfiguredWifiSsid)}:{ConfiguredWifiSsid}",
                $"{nameof(DeviceSettings.UseVolumeOverride)}:{UseVolumeOverride}",
                $"{nameof(DeviceSettings.OverriddenVolumeValue)}:{OverriddenVolumeValue}"
            };

            return string.Join("|", modelInformation);
        }
    }
}
