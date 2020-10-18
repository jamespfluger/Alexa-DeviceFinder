using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Amazon;
using Amazon.DynamoDBv2;
using Amazon.DynamoDBv2.DataModel;
using DeviceFinder.Models.Auth;
using DeviceFinder.Models.Devices;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;

namespace DeviceFinder.DeviceApi.Controllers
{
    [Route("devicefinder/[controller]")]
    [ApiController]
    public class DevicesController : ControllerBase
    {

        private readonly DynamoDBContext context;

        public DevicesController()
        {
            AmazonDynamoDBClient client = new AmazonDynamoDBClient(RegionEndpoint.USWest2);
            context = new DynamoDBContext(client);
        }

        /// <summary>
        /// Gets a user/device pair
        /// </summary>
        /// <param name="userId">Amazon user ID</param>
        /// <param name="deviceId">Android device ID</param>
        [HttpGet("users/{userid}/devices/{deviceid}")]
        public async Task<ActionResult<UserDevice>> GetSingleUserDevice([FromRoute] string userId, [FromRoute] string deviceId)
        {
            UserDevice foundDevice = await context.LoadAsync<UserDevice>(userId, deviceId);

            if (foundDevice != null)
                return Ok(foundDevice);
            else
                return NotFound();
        }

        /// <summary>
        /// Gets a user/device pair
        /// </summary>
        /// <param name="userId">Amazon user ID</param>
        /// <param name="deviceId">Android device ID</param>
        [HttpGet("users/{userid}")]
        public async Task<ActionResult<List<UserDevice>>> GetAllUserDevices([FromRoute] string userId)
        {
            try
            {
                AsyncSearch<UserDevice> searchResults = context.QueryAsync<UserDevice>(userId);
                List<UserDevice> allUserDevices = await searchResults.GetRemainingAsync();

                return Ok(allUserDevices);
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString() + Environment.NewLine + userId);
            }

        }

        /// <summary>
        /// Inserts/Updates a Amazon user ID and Android device ID pair
        /// </summary>
        /// <param name="authDevice">Pair of User and Android IDs</param>
        /// TODO: rename this to "devices"
        [HttpPost("users")]
        public async Task<ActionResult> DeleteAuthDevice([FromQuery] string amazonUserId)
        {
            try
            {
                await context.DeleteAsync<AuthDevice>(amazonUserId);
                return Ok("Device deleted.");
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString() + Environment.NewLine + amazonUserId);
            }
        }
    }
}