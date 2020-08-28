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
                string computedOtp = null;
                int numAttempts = 0;

                do
                {
                    computedOtp = GenerateOtp();

                    AlexaAuthUser queriedAuthUser = await DynamoService.Instance.LoadItem<AlexaAuthUser>(int.Parse(computedOtp));

                    if (queriedAuthUser != null)
                    {
                        Logger.Log($"OTP value '{computedOtp}' already exists. Attempting to generate a new one.");
                        numAttempts++;
                        continue;
                    }
                    else
                    {
                        AlexaAuthUser newAlexaAuthUser = new AlexaAuthUser();
                        newAlexaAuthUser.OneTimePassword = int.Parse(computedOtp);
                        newAlexaAuthUser.AlexaUserId = request.Context.System.User.UserId;
                        newAlexaAuthUser.TimeToLive = DateTimeOffset.UtcNow.AddSeconds(120).ToUnixTimeSeconds();

                        bool didSaveSucceed = await DynamoService.Instance.SaveItem<AlexaAuthUser>(newAlexaAuthUser);

                        if (didSaveSucceed)
                            break;
                        else
                            Logger.Log($"Error saving OTP value '{computedOtp}'. Attempting to generate a new one.");

                        numAttempts++;
                    }
                } while (numAttempts < 10);

                SsmlOutputSpeech responseMessage = BuildSsmlResponseMessage(computedOtp);

                if (computedOtp != null)
                    return ResponseBuilder.Tell(responseMessage);
                else
                    return ResponseBuilder.Tell($"I'm sorry, I was unable to process your authentication.");
            }
            catch (Exception ex)
            {
                Logger.Log($"Exception handling the authorization intent: {ex}");
                return ResponseBuilder.Tell($"I'm sorry, I was unable to process your authentication. {ex.Message}.");
            }
        }

        private static SsmlOutputSpeech BuildSsmlResponseMessage(string computedOtp)
        {
            StringBuilder ssmlSpeech = new StringBuilder();

            ssmlSpeech.Append("<speak>");
            ssmlSpeech.Append("Enter this code into the app: ");

            for (int i = 0; i < 6; i++)
            {
                ssmlSpeech.Append(computedOtp[i]);
                ssmlSpeech.Append("<break time=\"500ms\"/>");
            }
            ssmlSpeech.Append("This code will expire in 60 seconds. ");
            ssmlSpeech.Append("Again, enter this code into the app: ");
            for (int i = 0; i < 6; i++)
            {
                ssmlSpeech.Append(computedOtp[i]);
                ssmlSpeech.Append(" <break time=\"500ms\"/>");
            }
            ssmlSpeech.Append("</speak>");

            return new SsmlOutputSpeech(ssmlSpeech.ToString());
        }

        private string GenerateOtp()
        {
            byte[] otpKey = Encoding.UTF8.GetBytes(Guid.NewGuid().ToString());
            Totp totp = new Totp(otpKey, 120);

            return totp.ComputeTotp();
        }
    }
}
