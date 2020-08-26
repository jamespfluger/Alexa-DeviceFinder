using System;
using System.Diagnostics;
using System.Threading.Tasks;
using Alexa.NET;
using Alexa.NET.Request;
using Alexa.NET.Request.Type;
using Alexa.NET.Response;
using AlexaDeviceFinderSkill.Models;
using AlexaDeviceFinderSkill.Services;
using Amazon;
using Amazon.DynamoDBv2;
using Amazon.DynamoDBv2.DataModel;

namespace AlexaDeviceFinderSkill.RequestHandlers
{
    public class AuthHandler : IRequestHandler
    {
        public async Task<SkillResponse> ProcessRequest(SkillRequest request)
        {
            try
            {
                string otp = await GenerateOtp();

                return ResponseBuilder.Tell($"Here is your one time code: {otp}");
            }
            catch (Exception ex)
            {
                Logger.Log($"Exception handling the authorization intent: {ex}");
                return ResponseBuilder.Tell($"I'm sorry, I was unable to process your authentication. {ex.Message}.");
            }
        }

        private async Task<string> GenerateOtp()
        {
            string otp = "";


            return otp;
        }
    }
}
