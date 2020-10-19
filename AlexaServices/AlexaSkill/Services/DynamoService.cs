using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Threading.Tasks;
using Amazon;
using Amazon.DynamoDBv2;
using Amazon.DynamoDBv2.DataModel;

namespace DeviceFinder.AlexaSkill.Services
{
    public class DynamoService
    {
        public static DynamoService Instance { get { return lazyDynamoService.Value; } }

        private static readonly Lazy<DynamoService> lazyDynamoService = new Lazy<DynamoService>(() => new DynamoService());
        private readonly AmazonDynamoDBClient client;
        private readonly DynamoDBContext context;

        public DynamoService()
        {
            client = new AmazonDynamoDBClient(RegionEndpoint.USWest2);
            context = new DynamoDBContext(client);
        }

        public async Task<T> LoadItem<T>(object hashKey)
        {
            try
            {
                Stopwatch s = Stopwatch.StartNew();
                T item = await context.LoadAsync<T>(hashKey);
                Logger.Log($"Dynamo load time: {s.ElapsedMilliseconds}ms");

                return item;
            }
            catch (Exception ex)
            {
                Logger.Log($"Failure when loading object: {ex}");
                return default;
            }
        }

        public async Task<T> LoadItem<T>(object hashKey, object rangeKey)
        {
            try
            {
                Stopwatch s = Stopwatch.StartNew();
                T item = await context.LoadAsync<T>(hashKey, rangeKey);
                Logger.Log($"Dynamo load time: {s.ElapsedMilliseconds}ms");

                return item;
            }
            catch (Exception ex)
            {
                Logger.Log($"Failure when loading object: {ex}");
                return default;
            }
        }

        public async Task<List<T>> QueryItems<T>(object hashKey)
        {
            try
            {
                Stopwatch s = Stopwatch.StartNew();
                List<T> items = await context.QueryAsync<T>(hashKey).GetRemainingAsync();
                Logger.Log($"Dynamo load time: {s.ElapsedMilliseconds}ms");

                return items;
            }
            catch (Exception ex)
            {
                Logger.Log($"Failure when loading object: {ex}");
                return default;
            }
        }

        public async Task<bool> SaveItem<T>(T objectToCreate)
        {
            try
            {
                Stopwatch stopwatch = Stopwatch.StartNew();
                await context.SaveAsync<T>(objectToCreate);
                Logger.Log($"Dynamo save time: {stopwatch.ElapsedMilliseconds}ms");

                return true;
            }
            catch (Exception ex)
            {
                Logger.Log($"Failure when saving object: {ex}");
                return false;
            }
        }
    }
}
