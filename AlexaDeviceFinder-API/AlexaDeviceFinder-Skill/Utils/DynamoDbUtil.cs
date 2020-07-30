using System;
using System.Collections.Generic;
using System.Linq;
using AlexaDeviceFinderSkill.Models;
using Amazon;
using Amazon.DynamoDBv2;
using Amazon.DynamoDBv2.DataModel;

namespace AlexaDeviceFinderSkill
{
    public class DynamoDbUtil
    {
        public DynamoDBContext Context { get; private set; }

        public UserDevice GetDevice(string userId)
        {
            List<UserDevice> allUserDevices = new List<UserDevice>();

            try
            {
                AmazonDynamoDBClient client = new AmazonDynamoDBClient(RegionEndpoint.USWest2);
                Context = new DynamoDBContext(client);

                AlexaLambdaEntry.Logger.Log($"Getting the device for user {userId}");

                allUserDevices = Context.QueryAsync<UserDevice>(userId).GetRemainingAsync().GetAwaiter().GetResult();

                if (allUserDevices.Count == 0)
                {
                    AlexaLambdaEntry.Logger.Log($"We didn't find any devices for user {userId}.");
                    throw new Exception($"We didn't find any devices for user {userId}.");
                }
                else
                {
                    List<string> allDevices = new List<string>();
                    allUserDevices.ForEach(f => allDevices.Add(f.ToString()));

                    AlexaLambdaEntry.Logger.Log($"We found these devices: " + string.Join(',', allDevices));
                }
            }
            catch (Exception ex)
            {
                AlexaLambdaEntry.Logger.Log($"We had an exception loading devices: {ex}");
                throw;
            }

            return allUserDevices.First();
        }
    }
}
