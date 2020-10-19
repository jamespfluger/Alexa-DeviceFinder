using System.Threading.Tasks;
using Alexa.NET.Request;
using Alexa.NET.Request.Type;
using Alexa.NET.Response;

namespace DeviceFinder.AlexaSkill.RequestHandlers
{
    public interface IRequestHandler
    {
        public Task<SkillResponse> ProcessRequest(Intent intent, AlexaSystem system);
    }
}
