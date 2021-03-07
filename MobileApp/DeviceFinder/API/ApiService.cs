using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Threading.Tasks;
using DeviceFinder.Models.Auth;
using DeviceFinder.Models.Devices;
using Newtonsoft.Json;
using RestSharp;

namespace DeviceFinder.API
{
    public class ApiService
    {
        public static ApiService Instance => lazyApi.Value;

        private readonly IRestClient client = new RestClient(baseUrl);
        private const string baseUrl = "https://qsbrgmx8u1.execute-api.us-west-2.amazonaws.com/prd/devicefinder/";
        
        private static readonly Lazy<ApiService> lazyApi = new Lazy<ApiService>(() => new ApiService());
        private static readonly IReadOnlyDictionary<Type, string> saveResourceEndpoints;
        private static readonly IReadOnlyDictionary<Type, string> updateResourceEndpoints;
        private static readonly IReadOnlyDictionary<Type, string> getResourceEndpoints;

        static ApiService()
        {
            saveResourceEndpoints = new Dictionary<Type, string>
            {
                [typeof(AuthData)] = "auth/users",
                [typeof(Device)] = "",
                [typeof(DeviceSettings)] = ""
            };
            updateResourceEndpoints = new Dictionary<Type, string>
            {
                [typeof(AuthData)] = "users/{userid}",
                [typeof(Device)] = "",
                [typeof(DeviceSettings)] = ""
            };
            getResourceEndpoints = new Dictionary<Type, string>
            {
                [typeof(AuthData)] = "auth/users",
                [typeof(Device)] = "",
                [typeof(DeviceSettings)] = ""
            };
        }

        public async Task<IRestResponse> CreateDevice(AuthData authData)
        {
            RestRequest addRequest = new RestRequest();
            addRequest.Method = Method.POST;
            addRequest.Resource = "management/users";
            addRequest.AddJsonBody(authData);

            return await client.ExecuteAsync(addRequest);
        }

        public IRestResponse SaveDeviceSettings(DeviceSettings deviceSettings)
        {
            RestRequest saveSettingsRequest = new RestRequest();
            saveSettingsRequest.Method = Method.POST;
            saveSettingsRequest.Resource = "users/{userid}/devices/{deviceid}";
            saveSettingsRequest.AddJsonBody(deviceSettings);

            return client.Execute(saveSettingsRequest);
        }

        public List<Device> GetAllUserDevices(string alexaUserId)
        {
            RestClient client = new RestClient(baseUrl);
            RestRequest getDevicesRequest = new RestRequest("management/users/{userid}", Method.GET);
            getDevicesRequest.AddUrlSegment("userid", alexaUserId);

            IRestResponse response = client.Execute(getDevicesRequest);
            List<Device> userDevices = JsonConvert.DeserializeObject<List<Device>>(response.Content);

            return userDevices;
        }

        private IRestResponse Save<T>(T modelToSave, EndpointInfo endpointInfo)
        {
            RestRequest addRequest = new RestRequest();
            addRequest.Method = endpointInfo.Method;
            addRequest.Resource = endpointInfo.Resource;
            addRequest.AddJsonBody(modelToSave);

            return client.Execute(addRequest);
        }

        private T Get<T>(string resource)
        {
            RestRequest getRequest = new RestRequest();
            getRequest.Method = Method.GET;
            getRequest.Resource = resource;

            IRestResponse response = client.Execute(getRequest);

            return JsonConvert.DeserializeObject<T>(response.Content);
        }
    }
}
