﻿using System;
using System.Collections.Generic;
using Amazon.DynamoDBv2.DataModel;
using DeviceFinder.Models.Auth;

namespace DeviceFinder.Models.Devices
{
    public class UserDevice
    {
        [DynamoDBHashKey("AlexaUserID")]
        public string AlexaUserId { get; set; }

        [DynamoDBProperty("DeviceID")]
        public string DeviceId { get; set; }

        [DynamoDBProperty("AmazonUserID")]
        public string AmazonUserId { get; set; }

        [DynamoDBProperty("AlexaDeviceID")]
        public string AlexaDeviceId { get; set; }

        [DynamoDBProperty("DeviceName")]
        public string DeviceName { get; set; }

        [DynamoDBProperty("DeviceOS")]
        public DeviceOperatingSystem DeviceOs { get; set; }

        [DynamoDBProperty("ModifiedDate")]
        public DateTime ModifiedDate { get; set; }

        public UserDevice() { this.ModifiedDate = DateTime.UtcNow; }


        public UserDevice(AuthAlexaUser alexaUser, AuthDevice userDevice)
        {
            UserDevice newFullUserDevice = new UserDevice()
            {
                AlexaUserId = alexaUser.AlexaUserId,
                AlexaDeviceId = alexaUser.AlexaDeviceId,
                DeviceId = userDevice.DeviceId,
                AmazonUserId = userDevice.AmazonUserId,
                DeviceName = userDevice.DeviceName,
                DeviceOs = userDevice.DeviceOs,
            };
        }
        public override string ToString()
        {
            List<string> modelInformation = new List<string>();

            modelInformation.Add(nameof(UserDevice.AlexaUserId) + ":" + AlexaUserId);
            modelInformation.Add(nameof(UserDevice.DeviceId) + ":" + DeviceId);
            modelInformation.Add(nameof(UserDevice.AmazonUserId) + ":" + AmazonUserId);
            modelInformation.Add(nameof(UserDevice.DeviceName) + ":" + DeviceName);
            modelInformation.Add(nameof(UserDevice.DeviceOs) + ":" + DeviceOs.ToString());
            modelInformation.Add(nameof(UserDevice.ModifiedDate) + ":" + ModifiedDate);

            return string.Join('|', modelInformation);
        }
    }
}