using System;
using System.Threading.Tasks;
using Alexa.NET;
using Alexa.NET.Request;
using Alexa.NET.Response;
using DeviceFinder.AlexaSkill.Services;
using DeviceFinder.Models.Auth;

namespace DeviceFinder.AlexaSkill.RequestHandlers
{
    public class FindDeviceHandler : IRequestHandler
    {
        public async Task<SkillResponse> ProcessRequest(SkillRequest skillRequest)
        {
            try
            {
                // Grab the device information from Dynamo and immediately send the notification
                AuthDevice userDevice = await DynamoService.Instance.LoadItem<AuthDevice>(skillRequest.Session.User.UserId);

                // Immediately send the notification
                await FirebaseService.Instance.SendFirebaseMessage(userDevice);

                return ResponseBuilder.Tell("Successfully sent a message!");
            }
            catch (Exception ex)
            {
                Logger.Log($"Exception handling the find device intent: {ex}");
                return ResponseBuilder.Tell($"I'm sorry, I was unable to find your device. {ex.Message}.");
            }
        }
    }
}
