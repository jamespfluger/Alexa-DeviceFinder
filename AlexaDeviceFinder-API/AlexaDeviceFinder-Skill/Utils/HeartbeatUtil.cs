using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.IO;
using System.Text;
using Alexa.NET;
using Alexa.NET.Response;
using AlexaDeviceFinderSkill.Models;
using Amazon;
using Amazon.DynamoDBv2;
using Amazon.DynamoDBv2.DataModel;
using FirebaseAdmin;
using FirebaseAdmin.Messaging;
using Google.Apis.Auth.OAuth2;

namespace AlexaDeviceFinderSkill.Utils
{
    public static class HeartbeatUtil
    {
        public static SkillResponse SendHeartbeat()
        {
            Stopwatch stopwatch = Stopwatch.StartNew();
            char successfulDynamo = LoadDynamo() ? 'Y' : 'N';
            stopwatch.Stop();
            long dynamoTime = stopwatch.ElapsedMilliseconds;

            stopwatch.Restart();
            char successfulFirebase = LoadFirebase() ? 'Y' : 'N';
            stopwatch.Stop();
            long firebaseTime = stopwatch.ElapsedMilliseconds;

            return ResponseBuilder.Tell(@$"Heartbeat complete | DynamoDB={successfulDynamo}:Firebase={successfulFirebase} | Dynamo={dynamoTime}ms:Firebase={firebaseTime}ms");
        }

        private static bool LoadDynamo()
        {
            try
            {
                AmazonDynamoDBClient client = new AmazonDynamoDBClient(RegionEndpoint.USWest2);
                DynamoDBContext context = new DynamoDBContext(client);
                context.QueryAsync<UserDevice>("HEARTBEAT");

                return true;
            }
            catch (Exception ex)
            {
                return false;
            }
        }

        private static bool LoadFirebase()
        {
            try
            {
                if (FirebaseApp.DefaultInstance == null)
                {
                    AppOptions appOptions = new AppOptions();
                    appOptions.Credential = GoogleCredential.FromFile(Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "key.json"));
                    FirebaseApp firebaseApp = FirebaseApp.Create(appOptions);
                }

                FirebaseMessaging.DefaultInstance.SendAsync(new Message());
                return true;
            }
            catch (Exception ex)
            {
                return false;
            }
                
        }
    }
}
