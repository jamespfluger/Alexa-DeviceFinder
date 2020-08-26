using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using AlexaDeviceFinderAuth.Models;
using Amazon;
using Amazon.DynamoDBv2;
using Amazon.DynamoDBv2.DataModel;
using Microsoft.AspNetCore.Mvc;
using OtpNet;

namespace AlexaDeviceFinderAuth.Controllers
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
        public async Task<ActionResult<UserDevice>> GetUserDevice([FromRoute] string userId, [FromRoute] string deviceId)
        {
            UserDevice foundDevice = await context.LoadAsync<UserDevice>(userId, deviceId);

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
        public async Task<ActionResult> AddUserDevice([FromBody] UserDevice userDevice)
        {
            try
            {
                await context.SaveAsync(userDevice);
                return Ok();
            }
            catch(Exception e)
            {
                return BadRequest(e);
            }
        }
    }
}
