using Amazon.Lambda.Core;

namespace DeviceFinder.AlexaSkill.Services
{
    public static class Logger
    {
        private static ILambdaContext lambdaContext;

        public static void Init(ILambdaContext lambdaContextInit)
        {
            lambdaContext = lambdaContextInit;
        }

        public static void Log(string message)
        {
            lambdaContext.Logger.Log(message);
        }

        public static void LogLine(string message)
        {
            lambdaContext.Logger.LogLine(message);
        }
    }
}
