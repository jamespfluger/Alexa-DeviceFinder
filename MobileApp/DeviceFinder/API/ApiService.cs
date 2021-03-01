using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using DeviceFinder.Models.Auth;
using DeviceFinder.Models.Devices;
using Newtonsoft.Json;
using RestSharp;

namespace DeviceFinder.API
{
    public class ApiService
    {
        public ApiService Instance => lazyApi.Value;

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
                [typeof(AuthDevice)] = "auth/users",
                [typeof(Device)] = "",
                [typeof(DeviceSettings)] = ""
            };
            updateResourceEndpoints = new Dictionary<Type, string>
            {
                [typeof(AuthDevice)] = "users/{userid}",
                [typeof(Device)] = "",
                [typeof(DeviceSettings)] = ""
            };
            getResourceEndpoints = new Dictionary<Type, string>
            {
                [typeof(AuthDevice)] = "auth/users",
                [typeof(Device)] = "",
                [typeof(DeviceSettings)] = ""
            };
        }

        public IRestResponse SaveDevice(AuthDevice newDevice)
        {
            RestRequest addRequest = new RestRequest();
            addRequest.Method = Method.POST;
            addRequest.Resource = "auth/users";
            addRequest.AddJsonBody(newDevice);

            return client.Execute(addRequest);
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
            getDevicesRequest.AddParameter("userid", alexaUserId);

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
