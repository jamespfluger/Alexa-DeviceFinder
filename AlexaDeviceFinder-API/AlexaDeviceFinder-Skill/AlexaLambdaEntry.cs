using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Alexa.NET;
using Alexa.NET.Request;
using Alexa.NET.Request.Type;
using Alexa.NET.Response;
using AlexaDeviceFinderSkill.Models;
using AlexaDeviceFinderSkill.Utils;
using Amazon.Lambda.Core;
using Amazon.Lambda.Serialization.Json;

// Assembly attribute to enable the Lambda function's JSON input to be converted into a .NET class.
[assembly: LambdaSerializer(typeof(Amazon.Lambda.Serialization.Json.JsonSerializer))]

namespace AlexaDeviceFinderSkill
{
    public class AlexaLambdaEntry
    {
        public static ILambdaLogger Logger;

        public SkillResponse AlexaHandler(SkillRequest input, ILambdaContext context)
        {
            Logger = context.Logger;

            AlexaLambdaEntry.Logger.LogLine($"{input.Session.SessionId} is {(input.Session.New ? "New" : "Old")} ");
            AlexaLambdaEntry.Logger.LogLine($"{System.Text.Json.JsonSerializer.Serialize<SkillRequest>(input)}");
            AlexaLambdaEntry.Logger.LogLine($"UserId: {input.Session.User.UserId}");
            AlexaLambdaEntry.Logger.LogLine($"AccessToken: {input?.Session?.User?.AccessToken}");

            switch (input.Request)
            {
                case LaunchRequest launchRequest:
                    return HandleLaunch(launchRequest);
                case IntentRequest intentRequest:
                    return HandleIntent(intentRequest, input.Session.User.UserId);
                default:
                    throw new NotImplementedException("Unknown request type.");
            }
        }

        private void FindDevice(IntentRequest intentRequest, string userId)
        {
            try
            {
                DynamoDbUtil dynamoUtil = new DynamoDbUtil();
                UserDevice device = dynamoUtil.GetDevice(userId);

                FirebaseUtil firebaseUtil = new FirebaseUtil();
                firebaseUtil.SendMessage(device.DeviceId);
            }
            catch (Exception ex)
            {
                AlexaLambdaEntry.Logger.LogLine($"We had an exception getting or finding the device: " + ex.Message + System.Environment.NewLine + ex);
            }

        }

        private SkillResponse HandleLaunch(LaunchRequest launchRequest)
        {
            AlexaLambdaEntry.Logger.LogLine($"LaunchRequest made");

            var outputSpeech = new PlainTextOutputSpeech();
            outputSpeech.Text = "Welcome! You can ask me to say hello world.";

            return ResponseBuilder.Tell(outputSpeech);
        }

        private SkillResponse HandleIntent(IntentRequest intentRequest, string userId)
        {
            try
            {
                AlexaLambdaEntry.Logger.LogLine($"IntentRequest {intentRequest.Intent.Name} made");

                FindDevice(intentRequest, userId);

                AlexaLambdaEntry.Logger.LogLine("Left FindDevice");
                string responseText = "Hello world!";
                var responseSpeech = new PlainTextOutputSpeech(responseText);

                AlexaLambdaEntry.Logger.LogLine("About to tell user something!");
                return ResponseBuilder.Tell(responseSpeech);
            }
            catch (Exception ex)
            {
                AlexaLambdaEntry.Logger.LogLine($"We had an exception handling the intent: " + ex.Message + System.Environment.NewLine + ex);
            }

            return ResponseBuilder.Tell("Uh oh error oh");

        }
    }
}
