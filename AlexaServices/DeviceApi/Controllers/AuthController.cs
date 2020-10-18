using System;
using System.Text.Json.Serialization;
using System.Threading.Tasks;
using Amazon;
using Amazon.DynamoDBv2;
using Amazon.DynamoDBv2.DataModel;
using Amazon.DynamoDBv2.DocumentModel;
using Amazon.DynamoDBv2.Model;
using Amazon.Lambda.Core;
using Amazon.Runtime.Internal.Util;
using DeviceFinder.Models.Auth;
using DeviceFinder.Models.Devices;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using Newtonsoft.Json;

namespace DeviceFinder.DeviceApi.Controllers
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
        /// Inserts/Updates a Amazon user ID and Android device ID pair
        /// </summary>
        /// <param name="authDevice">Pair of User and Android IDs</param>
        /// TODO: rename this to "devices"
        [HttpPost("users")]
        public async Task<ActionResult> AddNewAuthDevice([FromBody] AuthDevice authDevice)
        {
            try
            {
                if (authDevice == null || string.IsNullOrEmpty(authDevice.AmazonUserId) || string.IsNullOrEmpty(authDevice.DeviceId))
                    return BadRequest($"Error in add: AuthUserDevice body is missing ({authDevice == null}) or malformed: {authDevice.ToString()}");

                await context.DeleteAsync<AuthDevice>(authDevice.AmazonUserId);
                AuthAlexaUser alexaUser = await context.LoadAsync<AuthAlexaUser>(authDevice.OneTimePassword);

                if (alexaUser == null)
                {
                    return NotFound("Linked device could not be found in the database.");
                }
                else if (alexaUser.TimeToLive >= DateTimeOffset.UtcNow.ToUnixTimeSeconds())
                {
                    return Unauthorized("The entered code has expired. Please ask Alexa for a new one.");
                }
                else
                {
                    UserDevice fullUserDevice = new UserDevice(alexaUser, authDevice);
                    Task saveResult = context.SaveAsync<UserDevice>(fullUserDevice);
                    Task deleteResult = context.DeleteAsync<AuthDevice>(authDevice.OneTimePassword);

                    Task.WaitAll(saveResult, deleteResult);

                    return Created(nameof(AddNewAuthDevice), fullUserDevice);
                }
            }
            catch (Exception ex)
            {
                return BadRequest(ex.ToString() + Environment.NewLine + authDevice.ToString());
            }
        }
    }
}
