using System;
using System.Threading.Tasks;
using AlexaDeviceFinderSkill.Models;
using Amazon;
using Amazon.DynamoDBv2;
using Amazon.DynamoDBv2.DataModel;

namespace AlexaDeviceFinderSkill
{
    public class DynamoDbUtil
    {
        public async Task<UserDevice> GetDevice(string userId)
        {
            try
            {
                AmazonDynamoDBClient client = new AmazonDynamoDBClient(RegionEndpoint.USWest2);
                DynamoDBContext context = new DynamoDBContext(client);

                AlexaLambdaEntry.Logger.Log($"Getting the device for user {userId}");

                UserDevice userDevice = await context.LoadAsync<UserDevice>(userId);

                return userDevice;
            }
            catch (Exception ex)
            {
                AlexaLambdaEntry.Logger.Log($"We had an exception loading devices: {ex}");
                throw;
            }
        }
    }
}
