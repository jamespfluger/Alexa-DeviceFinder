using System;
using System.Collections.Generic;
using System.Text;
using DeviceFinder.Models.Devices;
using Newtonsoft.Json;
using RestSharp;

namespace DeviceFinder.API
{
    public class ApiService
    {
        public ApiService Instance { get { return lazyApi.Value; } }

        private static readonly Lazy<ApiService> lazyApi = new Lazy<ApiService> (() => new ApiService());
        private const string baseUrl = "https://qsbrgmx8u1.execute-api.us-west-2.amazonaws.com/prd/devicefinder/";

        public IRestResponse SaveDevice(UserDevice newDevice)
        {
            RestClient client = new RestClient(baseUrl);
            RestRequest addRequest = new RestRequest("", Method.POST);
            addRequest.AddJsonBody(newDevice);

            return client.Execute(addRequest);
        }

        public List<UserDevice> GetAllUserDevices(string alexaUserId)
        {
            RestClient client = new RestClient(baseUrl);
            RestRequest getDevicesRequest = new RestRequest("management/users/{userid}", Method.GET);
            getDevicesRequest.AddParameter("userid", alexaUserId);

            IRestResponse response = client.Execute(getDevicesRequest);
            List<UserDevice> userDevices = JsonConvert.DeserializeObject<List<UserDevice>>(response.Content);

            return userDevices;
        }

        public IRestResponse SaveDeviceSettings(DeviceSettings settings)
        {
            RestClient client = new RestClient(baseUrl);
            RestRequest addRequest = new RestRequest("", Method.POST);
            addRequest.AddJsonBody(settings);

            return client.Execute(addRequest);
        }
    }
}
