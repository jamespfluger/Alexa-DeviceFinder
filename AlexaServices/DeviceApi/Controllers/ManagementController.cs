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
                    return BadRequest($"Error in add: AuthUserDevice body is missing (IsNull={authData == null}) or malformed: {authData}");

                // Find the user created when interacting with Alexa
                AlexaUser alexaUser = await context.LoadAsync<AlexaUser>(authData.OneTimePasscode);

                // Ensure the user exists AND that the OTP has not expired
                if (alexaUser?.TimeToLive <= DateTimeOffset.UtcNow.ToUnixTimeSeconds())
                    return Unauthorized("The entered code has expired.");
                else if (alexaUser == null)
                    return NotFound();

                Device newDevice = new Device
                {
                    AlexaUserId = alexaUser.AlexaUserId,
                    DeviceId = Guid.NewGuid().ToString(),
                    FirebaseToken = authData.FirebaseToken,
                    DeviceName = authData.DeviceName
                };

                // Save the new device, and delete the old AlexaUser entry
                Task saveResult = context.SaveAsync(newDevice);
                Task deleteAlexaAuthResult = context.DeleteAsync<AlexaUser>(alexaUser.AlexaUserId);

                Task.WaitAll(saveResult, deleteAlexaAuthResult);

                return CreatedAtAction(nameof(AddNewDevice), newDevice);
            }
            catch (Exception ex)
            {
                string errorMessage = $"{ex.Message}\n\n{ex}\n\n{authData}";
                return BadRequest(errorMessage);
            }
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
                //Task<List<Device>> deviceSearchResults = context.QueryAsync<Device>(userId).GetRemainingAsync();
                //Task<List<DeviceSettings>> settingsSearchResults = context.QueryAsync<DeviceSettings>(userId).GetRemainingAsync();
                //await Task.WhenAll(deviceSearchResults, settingsSearchResults);
                //List<Device> devices = deviceSearchResults.Result;
                //List<DeviceSettings> settings = settingsSearchResults.Result;

                List<Device> devices = await context.QueryAsync<Device>(userId).GetRemainingAsync();

                foreach (Device device in devices)
                {
                    device.DeviceSettings ??= new DeviceSettings();
                }

                if (devices.Any())
                    return Ok(devices);
                else
                    return NotFound($"No devices found for user {userId}");
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
                if (device == null || string.IsNullOrEmpty(device.AlexaUserId) || string.IsNullOrEmpty(device.FirebaseToken))
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
                if (deviceSettings == null || string.IsNullOrEmpty(deviceSettings.AlexaUserId) || string.IsNullOrEmpty(deviceSettings.DeviceID))
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