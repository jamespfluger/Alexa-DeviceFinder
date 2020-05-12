using System;
using System.Collections.Generic;
using System.Threading.Tasks;
using AlexaDeviceFinderAuth.Models;
using Amazon;
using Amazon.DynamoDBv2;
using Amazon.DynamoDBv2.DataModel;
using Microsoft.AspNetCore.Mvc;

namespace AlexaDeviceFinderAuth.Controllers
{
    /// <summary>
    /// ASP.NET Core controller acting as a DynamoDB Proxy.
    /// </summary>
    [Route("devicefinder/[controller]")]
    public class AuthController : Controller
    {
        public DynamoDBContext Context { get; set; }

        public AuthController()
        {
            AmazonDynamoDBClient client = new AmazonDynamoDBClient(RegionEndpoint.USWest2);
            Context = new DynamoDBContext(client);
        }

        /// <summary>
        /// Gets a user/device pair
        /// </summary>
        /// <param name="userId">Amazon user ID</param>
        /// <param name="deviceId">Android device ID</param>
        [HttpGet("users/{userid}/devices/{deviceid}")]
        public async Task<ActionResult<UserDevice>> GetUserDevice([FromRoute] string userId, [FromRoute] string deviceId)
        {
            UserDevice foundDevice = await Context.LoadAsync<UserDevice>(userId, deviceId);

            if (foundDevice != null)
                return Ok(foundDevice);
            else
                return NotFound();
        }

        /// <summary>
        /// Gets the device IDs associated with a user
        /// </summary>
        /// <param name="userId">Amazon user ID</param>
        [HttpGet("users/{userid}")]
        public async Task<ActionResult<IEnumerable<UserDevice>>> GetUserDevices([FromRoute] string userId)
        {
            List<UserDevice> allUserDevices = await Context.QueryAsync<UserDevice>(userId).GetRemainingAsync();

            if(allUserDevices != null)
                return Ok(allUserDevices);
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
                await Context.SaveAsync(userDevice);
                return Ok();
            }
            catch(Exception e)
            {
                return BadRequest(e);
            }
        }

        /// <summary>
        /// Deletes a specific Android device ID
        /// </summary>
        /// <param name="userId">Amazon user ID</param>
        /// <param name="deviceId">Android device ID</param>
        [HttpDelete("users/{userid}/devices/{deviceid}")]
        public async Task<ActionResult> DeleteDevice([FromRoute] string userId, [FromRoute] string deviceId)
        {
            try
            {
                await Context.DeleteAsync<UserDevice>(userId, deviceId);
                return Ok();
            }
            catch(Exception e)
            {
                return BadRequest(e);
            }
        }

        /// <summary>
        /// Deletes all device IDs associated with a given Amazon user
        /// </summary>
        /// <param name="userId">Amazon user ID</param>
        [HttpDelete("users/{userid}")]
        public async Task<ActionResult> DeleteUser([FromRoute] string userId)
        {
            List<UserDevice> allUserDevices = await Context.QueryAsync<UserDevice>(userId).GetRemainingAsync();
            List<Task> deleteTasks = new List<Task>();

            foreach(UserDevice userDevice in allUserDevices)
            {
                deleteTasks.Add(Context.DeleteAsync<UserDevice>(userDevice));
            }

            await Task.WhenAll(deleteTasks);

            if (allUserDevices != null)
                return Ok();
            else
                return NotFound();
        }
    }
}
