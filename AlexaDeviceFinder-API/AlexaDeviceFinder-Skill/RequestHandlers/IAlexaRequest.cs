using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using Alexa.NET;
using Alexa.NET.Request;
using Alexa.NET.Request.Type;
using Alexa.NET.Response;

namespace AlexaDeviceFinderSkill.RequestHandlers
{
    public interface IRequestHandler
    {
        public Task<SkillResponse> ProcessRequest(SkillRequest request);
    }
}
