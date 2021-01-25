using System;
using System.Collections.Generic;
using System.Text;

namespace DeviceFinder.Abstractions
{
    public class AuthResult
    {
        public enum Status
        {
            Unknown,
            Success,
            Error,
            Canceled
        }

        public Status ResultStatus { get; private set; }
    }
}
