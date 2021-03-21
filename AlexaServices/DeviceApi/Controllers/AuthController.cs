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
        /// <param name="authData">Pair of User and Android IDs</param>
        /// TODO: rename this to "devices"
        [HttpPost("users")]
        public async Task<ActionResult> AddNewDevice([FromBody] AuthData authData)
        {
            throw new Exception($"This endpoint is no longer supported: {nameof(AuthController)} {nameof(AddNewDevice)}");

            try
            {
                // Verify the body we've received has the correct contents
                if (authData == null || !authData.IsModelValid())
                    return BadRequest($"Error in add: AuthUserDevice body is missing (IsNull={authData == null}) or malformed: {authData}");

                // Find the user created when interacting with Alexa
                AlexaUser alexaUser = await context.LoadAsync<AlexaUser>(authData.OneTimePasscode);

                // Ensure the user exists AND that the OTP has not expired
                if (alexaUser == null)
                    return NotFound();
                else if (alexaUser?.TimeToLive <= DateTimeOffset.UtcNow.ToUnixTimeSeconds())
                    return Unauthorized("The entered code has expired.");

                Device fullUserDevice = new Device
                {
                    AlexaUserId = alexaUser.AlexaUserId,
                    FirebaseToken = authData.FirebaseToken,
                    LoginUserId = authData.LoginUserId,
                    DeviceId = Guid.NewGuid().ToString(),
                    DeviceName = authData.DeviceName
                };

                // Save the new device, and delete the old AlexaUser entry
                Task saveResult = context.SaveAsync(fullUserDevice);
                Task deleteAlexaAuthResult = context.DeleteAsync<AlexaUser>(alexaUser.AlexaUserId);

                Task.WaitAll(saveResult, deleteAlexaAuthResult);

                return CreatedAtAction(nameof(AddNewDevice), fullUserDevice);
            }
            catch (Exception ex)
            {
                string errorMessage = $"{ex.Message}\n\n{ex}\n\n{authData}";
                return BadRequest(errorMessage);
            }
        }
    }
}
