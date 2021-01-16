using System.Collections.Generic;
using System.Text;

namespace DeviceFinder.AlexaSkill.Utility
{
    public class SsmlBuilder
    {
        private readonly StringBuilder ssmlSpeech;

        public SsmlBuilder()
        {
            this.ssmlSpeech = new StringBuilder();
        }

        public void StartSpeech()
        {
            ssmlSpeech.Append("<speak>");
        }

        public void AddSpeech(string speechToAdd)
        {
            ssmlSpeech.Append(speechToAdd);
        }

        public void AddSpeechWithBreak(string speechToAdd, long milliseconds)
        {
            ssmlSpeech.Append(speechToAdd);
            AddBreak(milliseconds);
        }

        public void AddSpeechWithBreaks(IEnumerable<char> speechChars, long milliseconds)
        {
            foreach (char speechChar in speechChars)
            {
                ssmlSpeech.Append(speechChars);
                AddBreak(milliseconds);
            }
            ssmlSpeech.Append('.');
        }

        public void AddBreak(long milliseconds)
        {
            ssmlSpeech.Append($"<break time=\"{milliseconds}ms\"/>");
        }

        public void EndSpeech()
        {
            ssmlSpeech.Append("</speak>");
        }

        public string Speak()
        {
            return ssmlSpeech.ToString();
        }

        public override string ToString()
        {
            return ssmlSpeech.ToString();
        }
    }
}
