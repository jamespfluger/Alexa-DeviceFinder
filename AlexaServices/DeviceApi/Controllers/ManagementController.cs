using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Amazon;
using Amazon.DynamoDBv2;
using Amazon.DynamoDBv2.DataModel;
using DeviceFinder.Models.Auth;
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

        [HttpPost("users")]
        public async Task<ActionResult> AddNewDevice([FromBody] AuthData authData)
        {
            try
            {
                // Verify the body we've received has the correct contents
                if (authData == null || !authData.IsModelValid())
                    return BadRequest($"Error in add: {nameof(AuthData)} body is malformed: {authData?.ToString() ?? "NULL"}");

                // Find the user created when interacting with Alexa
                AlexaUser alexaUser = await context.LoadAsync<AlexaUser>(authData.OneTimePasscode);

                if (alexaUser == null || alexaUser.TimeToLive <= DateTimeOffset.UtcNow.ToUnixTimeSeconds())
                    return NotFound();

                Device newDevice = new Device
                {
                    AlexaUserId = alexaUser.AlexaUserId,
                    DeviceId = Guid.NewGuid().ToString(),
                    FirebaseToken = authData.FirebaseToken,
                    DeviceName = authData.DeviceName
                };

                // Save the new device, and delete the old AlexaUser entry
                Task newDeviceSaveResult = context.SaveAsync(newDevice);
                Task deleteAlexaAuthResult = context.DeleteAsync<AlexaUser>(alexaUser.AlexaUserId);

                Task.WaitAll(newDeviceSaveResult, deleteAlexaAuthResult);

                return CreatedAtAction(nameof(AddNewDevice), newDevice);
            }
            catch (Exception ex)
            {
                return BadRequest(ex);
            }
        }

        /// TODO: add documentation
        [HttpGet("users/{alexauserid}")]
        public async Task<ActionResult<List<Device>>> GetAllUserDevices([FromRoute] string alexaUserId)
        {
            try
            {
                Task<List<Device>> deviceSearchResults = context.QueryAsync<Device>(alexaUserId).GetRemainingAsync();
                Task<List<DeviceSettings>> settingsSearchResults = context.QueryAsync<DeviceSettings>(alexaUserId).GetRemainingAsync();
                await Task.WhenAll(deviceSearchResults, settingsSearchResults);
                
                List<Device> devices = deviceSearchResults.Result;
                List<DeviceSettings> settings = settingsSearchResults.Result;

                if (!devices.Any())
                    return NotFound($"No devices found for user {alexaUserId}");

                foreach (Device device in devices)
                {
                    device.DeviceSettings = settings.FirstOrDefault(s => device.DeviceId == s.DeviceId);
                    device.DeviceSettings ??= new DeviceSettings();
                }

                return Ok(devices);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString());
            }
        }

        /// TODO: add documentation
        [HttpPut("/users/{alexauserid}/devices/{deviceid}")]
        public async Task<ActionResult> SaveUserDevice([FromBody] Device device)
        {
            try
            {
                if (!device.IsModelValid())
                    return BadRequest($"Error in settings save: UserDevice body is missing ({device == null}) or malformed: {device}");

                await context.SaveAsync(device);

                return Ok();
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString() + Environment.NewLine + device.ToString());
            }
        }

        /// TODO: add documentation
        [HttpPut("users/{alexauserid}/devices/{deviceid}/settings")]
        public async Task<ActionResult> SaveDeviceSettings([FromBody] DeviceSettings deviceSettings, [FromRoute] string alexaUserId, [FromRoute] string deviceId)
        {
            try
            {
                if (deviceSettings == null || alexaUserId == null || deviceId == null)
                    return BadRequest($"Error in settings save: DeviceSettings body is malformed: {deviceSettings}");

                deviceSettings.AlexaUserId = alexaUserId;
                deviceSettings.DeviceId = deviceId;
                await context.SaveAsync(deviceSettings);

                return Ok();
            }
            catch (Exception ex)
            {
                return BadRequest(ex);
            }
        }
    }
}
