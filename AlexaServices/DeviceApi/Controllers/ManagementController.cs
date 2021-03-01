using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Amazon;
using Amazon.DynamoDBv2;
using Amazon.DynamoDBv2.DataModel;
using DeviceFinder.Models.Devices;
using Microsoft.AspNetCore.Mvc;

namespace DeviceFinder.DeviceApi.Controllers
{
    [ApiController]
    [Route("devicefinder/[controller]")]
    public class ManagementController : ControllerBase
    {

        private readonly DynamoDBContext context;

        public ManagementController()
        {
            AmazonDynamoDBClient client = new AmazonDynamoDBClient(RegionEndpoint.USWest2);
            context = new DynamoDBContext(client);
        }

        /*/// TODO: add documentation
        //[HttpGet("users/{userid}/devices/{deviceid}")]
        //public async Task<ActionResult<UserDevice>> GetSingleUserDevice([FromRoute] string userId, [FromRoute] string deviceId)
        //{
        //    UserDevice foundDevice = await context.LoadAsync<UserDevice>(userId, deviceId);

        //    if (foundDevice != null)
        //        return Ok(foundDevice);
        //    else
        //        return NotFound();
        //}*/

        /// TODO: add documentation
        [HttpGet("users/{userid}")]
        public async Task<ActionResult<List<Device>>> GetAllUserDevices([FromRoute] string userId)
        {
            try
            {
                Task<List<Device>> deviceSearchResults = context.QueryAsync<Device>(userId).GetRemainingAsync();
                Task<List<DeviceSettings>> settingsSearchResults = context.QueryAsync<DeviceSettings>(userId).GetRemainingAsync();

                await Task.WhenAll(deviceSearchResults, settingsSearchResults);

                List<Device> devices = deviceSearchResults.Result;
                List<DeviceSettings> settings = settingsSearchResults.Result;

                foreach (Device device in devices)
                {
                    device.DeviceSettings = settings.FirstOrDefault(setting => device.DeviceId == setting.DeviceId);
                    device.DeviceSettings ??= new DeviceSettings();
                }

                if (devices.Any())
                    return Ok(devices);
                else
                    return NotFound();
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString() + Environment.NewLine + userId);
            }
        }

        /// TODO: add documentation
        [HttpPut("/users/{userid}/devices/{deviceid}")]
        public async Task<ActionResult> SaveUserDevice([FromBody] Device device)
        {
            try
            {
                if (device == null || string.IsNullOrEmpty(device.AlexaUserId) || string.IsNullOrEmpty(device.DeviceId))
                    return BadRequest($"Error in settings save: UserDevice body is missing ({device == null}) or malformed: {device.ToString()}");

                await context.SaveAsync(device);

                return Ok();
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString() + Environment.NewLine + device.ToString());
            }
        }

        /// TODO: add documentation
        [HttpPut("users/{userid}/devices/{deviceid}/settings")]
        public async Task<ActionResult> SaveDeviceSettings([FromBody] DeviceSettings deviceSettings, [FromRoute] string userId, [FromRoute] string deviceId)
        {
            try
            {
                if (deviceSettings == null || string.IsNullOrEmpty(deviceSettings.AlexaUserId) || string.IsNullOrEmpty(deviceSettings.DeviceId))
                    return BadRequest($"Error in settings save: DeviceSettings body is missing ({deviceSettings == null}) or malformed: {deviceSettings.ToString()}");

                await context.SaveAsync(deviceSettings);

                return Ok();
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString() + Environment.NewLine + deviceSettings.ToString());
            }
        }

        /* /// TODO: add documentation
        [HttpGet("devices/settings")]
        public async Task<ActionResult<List<DeviceSettings>>> GetSettingsForAllDevices([FromRoute] string userId)
        {
            try
            {
                AsyncSearch<DeviceSettings> searchResults = context.QueryAsync<DeviceSettings>(userId);
                List<DeviceSettings> allUserDevices = await searchResults.GetRemainingAsync();

                if (allUserDevices.Any())
                    return Ok(allUserDevices);
                else
                    return NotFound();
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString() + Environment.NewLine + userId);
            }
        }*/

    }
}