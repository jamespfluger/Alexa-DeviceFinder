using System;
using System.Collections.Generic;
using System.Text;
using Amazon.DynamoDBv2.DataModel;
using DeviceFinder.Models.Devices;

namespace DeviceFinder.Models.Auth
{
    public class AuthData
    {
        public string OneTimePasscode { get; set; }

        public string FirebaseToken { get; set; }

        public string LoginUserId { get; set; }

        public string DeviceName { get; set; }

        public override string ToString()
        {
            List<string> modelInfo = new List<string>
            {
                $"{nameof(DeviceName)}:{DeviceName}",
                $"{nameof(OneTimePasscode)}:{OneTimePasscode}",
                $"{nameof(LoginUserId)}:{LoginUserId}",
                $"{nameof(FirebaseToken)}:{FirebaseToken}",
            };

            return string.Join("|", modelInfo);
        }

        public bool IsModelValid()
        {
            return !string.IsNullOrEmpty(OneTimePasscode) && !string.IsNullOrEmpty(LoginUserId)
                && !string.IsNullOrEmpty(FirebaseToken) && !string.IsNullOrEmpty(DeviceName);
        }
    }
}
