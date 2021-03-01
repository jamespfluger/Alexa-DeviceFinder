using System;
using System.Collections.Generic;
using System.Text;
using RestSharp;

namespace DeviceFinder.API
{
    public record EndpointInfo
    {
        public string Resource { get; init; }
        public Method Method { get; init; }
        public string Parameter { get; init; }

        public EndpointInfo(string resource, Method method)
        {
            this.Resource = resource;
            this.Method = method;
        }
    }
}
