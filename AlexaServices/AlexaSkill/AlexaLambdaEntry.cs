using System;
using System.Diagnostics;
using System.Text;
using Alexa.NET;
using Alexa.NET.Request;
using Alexa.NET.Request.Type;
using Alexa.NET.Response;
using AlexaDeviceFinderSkill.Models;
using AlexaDeviceFinderSkill.Utils;
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

            AlexaLambdaEntry.Logger.Log($"AccessToken: {input?.Session?.User?.AccessToken}");

            if (input.Request is IntentRequest)
                return HandleIntent(input.Request as IntentRequest, input.Session.User.UserId);
            else
                return ResponseBuilder.Tell("I'm sorry, I couldn't understand your request. Please rephrase it or try again later.");
        }

        private SkillResponse HandleIntent(IntentRequest intentRequest, string userId)
        {
            AlexaLambdaEntry.Logger.Log($"Processing the {intentRequest.Intent.Name} IntentRequest");

            try
            {
                string output = FindDevice(intentRequest, userId);
                return ResponseBuilder.Tell("Successfully sent a message!");
            }
            catch (Exception ex)
            {
                AlexaLambdaEntry.Logger.Log($"We had an exception handling the intent: {ex}");

                return ResponseBuilder.Tell($"I'm sorry, I was unable to find your device. {ex.Message}.");
            }
        }

        private string FindDevice(IntentRequest intentRequest, string userId)
        {
            try
            {
                // Get the user's device info from DynamoDB
                DynamoDbUtil dynamoUtil = new DynamoDbUtil();
                UserDevice device = dynamoUtil.GetDevice(userId);

                // Send an FCM notification to that device
                FirebaseUtil firebaseUtil = new FirebaseUtil();
                string result = firebaseUtil.SendMessage(device.DeviceId);
                return result;
            }
            catch (Exception ex)
            {
                AlexaLambdaEntry.Logger.Log($"We had an exception getting or finding the device: " + ex.Message + System.Environment.NewLine + ex);
                throw;
            }
        }
    }
}
