using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Amazon;
using Amazon.DynamoDBv2;
using Amazon.DynamoDBv2.DataModel;
using DeviceFinder.Models;
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
        public async Task<ActionResult> CreateNewDevice([FromBody] AuthData authData)
        {
            try
            {
                if (authData == null || !authData.IsModelValid())
                    return BadRequest($"Error in device create: {nameof(AuthData)} body is malformed: {authData}");

                // Find the user created when interacting with Alexa
                AlexaUser alexaUser = await context.LoadAsync<AlexaUser>(authData.OneTimePasscode);

                if (alexaUser == null || alexaUser.TimeToLive <= DateTimeOffset.UtcNow.ToUnixTimeSeconds())
                    return NotFound("Could not locate user.");

                Device newDevice = new Device
                {
                    AlexaUserId = alexaUser.AlexaUserId,
                    DeviceId = Guid.NewGuid().ToString(),
                    FirebaseToken = authData.FirebaseToken,
                    DeviceName = authData.DeviceName,
                    ModifiedDate = DateTime.UtcNow,
                    CreatedDate = DateTime.UtcNow
                };

                // Save the new device, and delete the old AlexaUser entry
                Task newDeviceSaveResult = context.SaveAsync(newDevice);
                Task deleteAlexaAuthResult = context.DeleteAsync<AlexaUser>(alexaUser.AlexaUserId);

                await Task.WhenAll(newDeviceSaveResult, deleteAlexaAuthResult);

                return CreatedAtAction(nameof(CreateNewDevice), newDevice);
            }
            catch (Exception ex)
            {
                return BadRequest($"{authData}\n{ex}");
            }
        }

        /// TODO: add documentation
        [HttpPut("users/{alexauserid}/devices/{deviceid}")]
        public async Task<ActionResult> UpdateDevice([FromBody] Device device, [FromRoute] string alexaUserId, [FromRoute] string deviceId)
        {
            try
            {
                if (!device.IsModelValid())
                    return BadRequest($"Error in device update. {nameof(Device)} body is malformed: {device}");

                Device existingDevice = await context.LoadAsync<Device>(alexaUserId, deviceId);
                
                if (existingDevice == null)
                    return NotFound($"No devices found for Device {alexaUserId}:{deviceId}");

                device.ModifiedDate = DateTime.UtcNow;
                device.CreatedDate = existingDevice.CreatedDate;
                await context.SaveAsync(device);

                return Ok(device);
            }
            catch (Exception ex)
            {
                return BadRequest($"{device}\n{ex}");
            }
        }

        /// TODO: add documentation
        [HttpGet("users/{alexaUserId}")]
        public async Task<ActionResult<List<Device>>> GetAllUserDevices([FromRoute] string alexaUserId)
        {
            try
            {
                List<Device> devices = await context.QueryAsync<Device>(alexaUserId).GetRemainingAsync();
                
                if (devices == null || !devices.Any())
                    return NotFound($"No devices found for user {alexaUserId}");
                else
                    return Ok(devices);
            }
            catch (Exception ex)
            {
                return BadRequest($"{alexaUserId}\n{ex}");
            }
        }

        /// TODO: add documentation
        [HttpDelete("users/{alexaUserId}/devices/{deviceId}")]
        public async Task<ActionResult> DeleteDevice([FromRoute] string alexaUserId, [FromRoute] string deviceId)
        {
            try
            {
                Device existingDevice = await context.LoadAsync<Device>(alexaUserId, deviceId);

                if (existingDevice == null)
                    return NotFound($"No devices found for Device {alexaUserId}:{deviceId}");

                await context.DeleteAsync<Device>(alexaUserId, deviceId);

                return Ok();
            }
            catch (Exception ex)
            {
                return BadRequest($"{alexaUserId}:{deviceId}\n{ex}");
            }
        }

    }
}
