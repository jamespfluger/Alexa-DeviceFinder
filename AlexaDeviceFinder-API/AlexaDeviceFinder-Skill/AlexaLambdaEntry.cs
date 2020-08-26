using System;
using System.Diagnostics;
using System.IO;
using System.Reflection;
using System.Threading.Tasks;
using Alexa.NET;
using Alexa.NET.Request;
using Alexa.NET.Request.Type;
using Alexa.NET.Response;
using AlexaDeviceFinderSkill.Models;
using AlexaDeviceFinderSkill.RequestHandlers;
using AlexaDeviceFinderSkill.Services;
using Amazon.Lambda.Core;

// Assembly attribute to enable the Lambda function's JSON input to be converted into a .NET class.
[assembly: LambdaSerializer(typeof(Amazon.Lambda.Serialization.Json.JsonSerializer))]

namespace AlexaDeviceFinderSkill
{
    public class AlexaLambdaEntry
    {
        public async Task<SkillResponse> AlexaHandler(SkillRequest input, ILambdaContext context)
        {
            if (input.Request is IntentRequest && input.Request.RequestId == "HEARTBEAT")
                return HeartbeatUtil.SendHeartbeat();

            Stopwatch s = Stopwatch.StartNew();
            Logger.Init(context);
            s.Stop();

            if (input.Request is IntentRequest)
                return await HandleIntent(input);
            else if (input.Request is SkillEventRequest)
                return await HandleSkillEvent(input);
            else
                return ResponseBuilder.Tell("I'm sorry, I couldn't understand your request. Please rephrase it or try again later.");
        }

        private async Task<SkillResponse> HandleIntent(SkillRequest request)
        {
            IRequestHandler requestHandler;

            IntentRequest intentRequest = request.Request as IntentRequest;

            if (intentRequest.Intent.Name == "FindDevice")
                requestHandler = new FindDeviceHandler();
            else
                requestHandler = new AuthHandler();

            return await requestHandler.ProcessRequest(request);
        }

        private async Task<SkillResponse> HandleSkillEvent(SkillRequest skillEventRequest)
        {


            return ResponseBuilder.Tell("Skill event handled.");
        }
    }
}
