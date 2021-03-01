using System;
using System.Threading.Tasks;
using Amazon;
using Amazon.DynamoDBv2;
using Amazon.DynamoDBv2.DataModel;
using DeviceFinder.Models.Auth;
using DeviceFinder.Models.Devices;
using Microsoft.AspNetCore.Mvc;

namespace DeviceFinder.DeviceApi.Controllers
{
    /// <summary>
    /// ASP.NET Core controller acting as a DynamoDB Proxy.
    /// </summary>
    [ApiController]
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
                if (authDevice == null || string.IsNullOrEmpty(authDevice.DeviceId) || string.IsNullOrEmpty(authDevice.LoginUserId))
                    return BadRequest($"Error in add: AuthUserDevice body is missing ({authDevice == null}) or malformed: {authDevice}");

                //await context.DeleteAsync<AuthDevice>(authDevice.AmazonUserId);
                AlexaUser alexaUser = await context.LoadAsync<AlexaUser>(authDevice.OneTimePasscode);

                if (alexaUser == null)
                {
                    return NotFound("Linked device could not be found in the database.");
                }
                else if (alexaUser.TimeToLive <= DateTimeOffset.UtcNow.ToUnixTimeSeconds())
                {
                    return Unauthorized("The entered code has expired. Please ask Alexa for a new one.");
                }
                else
                {
                    Device fullUserDevice = new Device(alexaUser, authDevice);
                    Task saveResult = context.SaveAsync(fullUserDevice);
                    //Task deleteAuthDeviceResult = context.DeleteAsync<AuthDevice>(authDevice.OneTimePasscode);
                    //Task deleteAlexaAuthResult = context.DeleteAsync<AuthAlexaUser>(alexaUser.AlexaUserId);

                    //Task.WaitAll(saveResult, deleteAuthDeviceResult, deleteAlexaAuthResult);
                    Task.WaitAll(saveResult);

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
