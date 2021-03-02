using System;
using System.Collections.Generic;
using System.Text;
using Amazon.DynamoDBv2.DataModel;
using DeviceFinder.Models.Devices;

namespace DeviceFinder.Models.Auth
{
    public class AuthDevice
    {
        public string DeviceId { get; set; }

        public string LoginUserId { get; set; }

        public string OneTimePasscode { get; set; }

        public string DeviceName { get; set; }

        public DeviceOperatingSystem DeviceOs { get; set; }

        public override string ToString()
        {
            List<string> modelInfo = new List<string>
            {
                nameof(LoginUserId) + ":" + LoginUserId,
                nameof(DeviceId) + ":" + DeviceId,
                nameof(OneTimePasscode) + ":" + OneTimePasscode
            };

            return string.Join("|", modelInfo);
        }

        public bool IsModelValid()
        {
            return !string.IsNullOrEmpty(DeviceId) && !string.IsNullOrEmpty(LoginUserId) &&
                   !string.IsNullOrEmpty(OneTimePasscode) && !string.IsNullOrEmpty(DeviceName);
        }
    }
}
