using System;
using System.Text;
using Alexa.NET;
using Alexa.NET.Request;
using Alexa.NET.Request.Type;
using Alexa.NET.Response;
using AlexaDeviceFinderSkill.Models;
using Amazon.Lambda.Core;

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
            SkillResponse response;

            StringBuilder initialParameters = new StringBuilder();
            initialParameters.Append($"{input.Session.SessionId} is {(input.Session.New ? "New" : "Old")}" + Environment.NewLine);
            initialParameters.Append($"UserId: {input?.Session?.User?.UserId}" + Environment.NewLine);
            initialParameters.Append($"AccessToken: {input?.Session?.User?.AccessToken}" + Environment.NewLine);

            AlexaLambdaEntry.Logger.LogLine(initialParameters.ToString());
            AlexaLambdaEntry.Logger.LogLine($"{System.Text.Json.JsonSerializer.Serialize<SkillRequest>(input)}" + Environment.NewLine);

            if (input.Request is IntentRequest)
            {
                response = HandleIntent(input.Request as IntentRequest, input.Session.User.UserId);
            }
            else
            {
                response = new SkillResponse();
                response.Response = new ResponseBody();
                response.Response.OutputSpeech = new PlainTextOutputSpeech("I'm sorry, I couldn't understand your request. Please rephrase it or try again.");
            }

            return response;
        }

        private SkillResponse HandleIntent(IntentRequest intentRequest, string userId)
        {
            try
            {
                AlexaLambdaEntry.Logger.LogLine($"Processing the {intentRequest.Intent.Name} IntentRequest");
                
                string output = FindDevice(intentRequest, userId);
                return ResponseBuilder.Tell("Successfully sent a message!");
            }
            catch (Exception ex)
            {
                AlexaLambdaEntry.Logger.LogLine($"We had an exception handling the intent: {ex}");
                return ResponseBuilder.Tell("I'm sorry, I was unable to find your device.");
            }
        }

        private string FindDevice(IntentRequest intentRequest, string userId)
        {
            try
            {
                DynamoDbUtil dynamoUtil = new DynamoDbUtil();
                UserDevice device = dynamoUtil.GetDevice(userId);

                FirebaseUtil firebaseUtil = new FirebaseUtil();
                return firebaseUtil.SendMessage(device.DeviceId);
            }
            catch (Exception ex)
            {
                AlexaLambdaEntry.Logger.LogLine($"We had an exception getting or finding the device: " + ex.Message + System.Environment.NewLine + ex);
                throw;
            }
        }
    }
}
