using System;
using System.Collections.Generic;
using System.IO;
using System.Text;

namespace AlexaDeviceFinderSkill.Utils
{
    public class DebugUtils
    {
        public static List<String> GetSubFiles(string directory)
        {
            List<String> files = new List<String>();
            try
            {
                foreach (string f in Directory.GetFiles(directory))
                {
                    files.Add(f);
                }
            }
            catch (Exception ex)
            {
                AlexaLambdaEntry.Logger.LogLine($"Error getting files at {directory}: {ex}");
            }

            try
            {
                foreach (string d in Directory.GetDirectories(directory))
                {
                    files.AddRange(GetSubFiles(d));
                }
            }
            catch (System.Exception ex)
            {
                AlexaLambdaEntry.Logger.LogLine($"Error getting directories at {directory}: {ex}");
            }

            return files;
        }


        public static IEnumerable<string> GetEverySingleFileEverywhere(string path)
        {
            List<string> literallyAllTheFiles = new List<string>();

            Queue<string> queue = new Queue<string>();
            queue.Enqueue(path);
            while (queue.Count > 0)
            {
                path = queue.Dequeue();
                try
                {
                    foreach (string subDir in Directory.GetDirectories(path))
                    {
                        queue.Enqueue(subDir);
                    }
                }
                catch (Exception ex)
                {
                    AlexaLambdaEntry.Logger.LogLine($"Error getting root directories at {path}: {ex}");
                }
                string[] files = null;
                try
                {
                    files = Directory.GetFiles(path);
                }
                catch (Exception ex)
                {
                    AlexaLambdaEntry.Logger.LogLine($"Error getting root files at {path}: {ex}");
                }
                if (files != null)
                {
                    for (int i = 0; i < files.Length; i++)
                    {
                        literallyAllTheFiles.Add(files[i]);
                    }
                }
            }

            return literallyAllTheFiles;
        }
    }
}
