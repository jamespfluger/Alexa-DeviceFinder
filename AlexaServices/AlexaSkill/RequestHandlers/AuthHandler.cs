using System;
using System.Text;
using System.Threading.Tasks;
using Alexa.NET;
using Alexa.NET.Request;
using Alexa.NET.Response;
using DeviceFinder.AlexaSkill.Services;
using DeviceFinder.Models;
using OtpNet;

namespace DeviceFinder.AlexaSkill.RequestHandlers
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
