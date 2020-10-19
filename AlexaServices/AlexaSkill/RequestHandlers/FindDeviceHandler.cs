using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Alexa.NET;
using Alexa.NET.Request;
using Alexa.NET.Request.Type;
using Alexa.NET.Response;
using Amazon.DynamoDBv2.Model;
using DeviceFinder.AlexaSkill.Services;
using DeviceFinder.Models.Auth;
using DeviceFinder.Models.Devices;

namespace DeviceFinder.AlexaSkill.RequestHandlers
{
    public class FindDeviceHandler : IRequestHandler
    {
        public async Task<SkillResponse> ProcessRequest(Intent intent, AlexaSystem system)
        {
            try
            {
                // Grab the device information from Dynamo and immediately send the notification
                List<UserDevice> userDevices = await DynamoService.Instance.QueryItems<UserDevice>(system.User.UserId);
                UserDevice foundDevice = null;

                if (userDevices == null || userDevices.Count == 0)
                {
                    throw new ResourceNotFoundException("This device may not have been set up yet.");
                }
                else if (userDevices.Count == 1)
                {
                    foundDevice = userDevices.Single();
                }
                else
                {
                    if (intent.Slots["Name"].Value != null)
                    {
                        string name = intent.Slots["Name"].Value.Replace("'s","");
                        foundDevice = userDevices.SingleOrDefault(u => u.DeviceName == name);

                        if (foundDevice == null)
                        {
                            throw new ResourceNotFoundException($"I was unable to find a device for: {name}");
                        }
                    }
                    else
                    {
                        throw new ResourceNotFoundException($"There are multiple devices associated with this account, but no name was supplied.");
                    }
                }
                    

                // Immediately send the notification
                await FirebaseService.Instance.SendFirebaseMessage(foundDevice);

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
