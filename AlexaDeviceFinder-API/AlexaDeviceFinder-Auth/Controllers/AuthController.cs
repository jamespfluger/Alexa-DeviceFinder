using System;
using System.Threading.Tasks;
using Amazon;
using Amazon.DynamoDBv2;
using Amazon.DynamoDBv2.DataModel;
using DeviceFinder.Models;
using Microsoft.AspNetCore.Mvc;

namespace DeviceFinder.AuthApi.Controllers
{
    /// <summary>
    /// ASP.NET Core controller acting as a DynamoDB Proxy.
    /// </summary>
    [Route("devicefinder/[controller]")]
    public class AuthController : Controller
    {
        private readonly DynamoDBContext context;

        public AuthController()
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
        public async Task<ActionResult<AmazonUserDevice>> GetUserDevice([FromRoute] string userId, [FromRoute] string deviceId)
        {
            AmazonUserDevice foundDevice = await context.LoadAsync<AmazonUserDevice>(userId, deviceId);

            if (foundDevice != null)
                return Ok(foundDevice);
            else
                return NotFound();
        }

        /// <summary>
        /// Inserts/Updates a Amazon user ID and Android device ID pair
        /// </summary>
        /// <param name="userDevice">Pair of User and Android IDs</param>
        [HttpPost("users")]
        public async Task<ActionResult> AddAmazonUserDevice([FromBody] AmazonUserDevice userDevice)
        {
            try
            {
                if (userDevice == null || string.IsNullOrEmpty(userDevice.AmazonUserId) || string.IsNullOrEmpty(userDevice.DeviceId))
                    return BadRequest("AmazonUserDevice body is missing or malformed");

                await context.SaveAsync(userDevice);
                return Ok();
            }
            catch(Exception ex)
            {
                return BadRequest(ex);
            }
        }
    }
}
