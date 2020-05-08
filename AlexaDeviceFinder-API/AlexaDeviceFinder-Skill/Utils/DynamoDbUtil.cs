using System;
using System.Collections.Generic;
using System.Linq;
using System.Linq.Expressions;
using System.Text;
using System.Threading.Tasks;
using AlexaDeviceFinderSkill.Models;
using Amazon;
using Amazon.DynamoDBv2;
using Amazon.DynamoDBv2.DataModel;

namespace AlexaDeviceFinderSkill
{
    public class DynamoDbUtil
    {
        public DynamoDBContext Context { get; set; }

        public DynamoDbUtil()
        {
            AmazonDynamoDBClient client = new AmazonDynamoDBClient(RegionEndpoint.USWest2);
            Context = new DynamoDBContext(client);
        }

        public UserDevice GetDevice(string userId)
        {
            List <UserDevice> allUserDevices = new List<UserDevice>();

            try
            {
                AlexaLambdaEntry.Logger.LogLine($"Getting the device for user {userId}");
                allUserDevices = Context.QueryAsync<UserDevice>(userId).GetRemainingAsync().GetAwaiter().GetResult();
                
                if (allUserDevices == null)
                {
                    AlexaLambdaEntry.Logger.LogLine("User devices query came back null. Something went wrong trying to get them.");
                }
                else if (allUserDevices.Count == 0)
                {
                    AlexaLambdaEntry.Logger.LogLine($"We didn't find any devices for user {userId}.");
                }
                else
                {
                    List<string> allDevices = new List<string>();
                    allUserDevices.ForEach(f => allDevices.Add(f.ToString()));

                    AlexaLambdaEntry.Logger.LogLine($"We found these devices: " + string.Join(',', allDevices));
                }
            }
            catch(Exception e)
            {
                AlexaLambdaEntry.Logger.LogLine($"We had an exception loading devices: {e.Message}{System.Environment.NewLine}{e.ToString()}");
            }

            return allUserDevices.First();
        }
    }
}
