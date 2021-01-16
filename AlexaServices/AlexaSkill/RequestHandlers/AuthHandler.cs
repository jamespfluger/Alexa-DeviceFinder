using System;
using System.Text;
using System.Threading.Tasks;
using Alexa.NET;
using Alexa.NET.Request;
using Alexa.NET.Request.Type;
using Alexa.NET.Response;
using DeviceFinder.AlexaSkill.Services;
using DeviceFinder.Models.Auth;
using OtpNet;

namespace DeviceFinder.AlexaSkill.RequestHandlers
{
    public class AuthHandler : IRequestHandler
    {
        public async Task<SkillResponse> ProcessRequest(Intent request, AlexaSystem system)
        {
            try
            {
                string computedOtp = null;
                int numAttempts = 0;

                do
                {
                    computedOtp = GenerateOtp();

                    AuthAlexaUser queriedAuthUser = await DynamoService.Instance.LoadItem<AuthAlexaUser>(computedOtp);

                    if (queriedAuthUser != null)
                    {
                        Logger.Log($"OTP value '{computedOtp}' already exists. Attempting to generate a new one.");
                        numAttempts++;
                        continue;
                    }
                    else
                    {
                        AuthAlexaUser newAlexaAuthUser = new AuthAlexaUser();
                        newAlexaAuthUser.OneTimePassword = computedOtp;
                        newAlexaAuthUser.AlexaUserId = system.User.UserId;
                        newAlexaAuthUser.AlexaDeviceId = system.Device.DeviceID;
                        newAlexaAuthUser.TimeToLive = DateTimeOffset.UtcNow.AddHours(2).ToUnixTimeSeconds();

                        bool didSaveSucceed = await DynamoService.Instance.SaveItem<AuthAlexaUser>(newAlexaAuthUser);

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

            StringBuilder otpSpeech = new StringBuilder();
            for (int i = 0; i < 6; i++)
            {
                otpSpeech.Append(computedOtp[i]);
                otpSpeech.Append("<break time=\"500ms\"/>");
            }

            ssmlSpeech.Append(otpSpeech.ToString());
            ssmlSpeech.Append(". Again, the code is: ");
            ssmlSpeech.Append(otpSpeech.ToString() + ".");

            ssmlSpeech.Append("This code will expire in 2 minutes. ");
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
