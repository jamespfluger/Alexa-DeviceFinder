using System.Collections.Generic;
using DeviceFinder.Droid.Models;
using RestSharp;

namespace DeviceFinder.Droid.API
{
    public class ApiService
    {
        private static string API_URL = "https://qsbrgmx8u1.Execute-api.Us-west-2.Amazonaws.Com/prd/devicefinder/";
        private RestClient client = new RestClient(API_URL);

        public ApiService()
        {
            RestClient client = new RestClient(API_URL);
        }

        public AuthUserDevice AddAuthUserDevice()
        {
            return new AuthUserDevice();
        }

        public UserDevice AddUserDevice()
        {
            return new UserDevice();
        }

        public List<UserDevice> GetAllUserDevices(string userId)
        {
            return new List<UserDevice>();
        }

        public bool SaveDeviceSettings(DeviceSettings deviceSettings)
        {
            return true;
        }
    }
}