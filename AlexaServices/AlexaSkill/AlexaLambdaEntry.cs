using System.Threading.Tasks;
using Alexa.NET;
using Alexa.NET.Request;
using Alexa.NET.Request.Type;
using Alexa.NET.Response;
using Amazon.Lambda.Core;
using DeviceFinder.AlexaSkill.RequestHandlers;
using DeviceFinder.AlexaSkill.Utility;

// Assembly attribute to enable the Lambda function's JSON input to be converted into a .NET class.
[assembly: LambdaSerializer(typeof(Amazon.Lambda.Serialization.Json.JsonSerializer))]

namespace DeviceFinder.AlexaSkill
{
    public class AlexaLambdaEntry
    {
        public async Task<SkillResponse> AlexaHandler(SkillRequest skillRequest, ILambdaContext lambdaContext)
        {
            if (skillRequest.Request is IntentRequest && skillRequest.Request.RequestId == "HEARTBEAT")
                return HeartbeatService.SendHeartbeat();

            Logger.Init(lambdaContext);

            if (skillRequest.Request is IntentRequest intentRequest)
                return await HandleIntent(intentRequest.Intent, skillRequest.Session.User.UserId);
            else if (skillRequest.Request is SkillEventRequest)
                return await HandleSkillEvent(skillRequest);
            else
                return ResponseBuilder.Tell("I'm sorry, I couldn't understand your request. Please rephrase it or try again later.");
        }

        private async Task<SkillResponse> HandleIntent(Intent intent, string userId)
        {
            IRequestHandler requestHandler;

            if (intent.Name == "FindDevice")
                requestHandler = new FindDeviceHandler();
            else
                requestHandler = new AddDeviceHandler();

            return await requestHandler.ProcessRequest(intent, userId);
        }

        private async Task<SkillResponse> HandleSkillEvent(SkillRequest skillEventRequest)
        {
            return ResponseBuilder.Tell("Skill event handled.");
        }
    }
}
