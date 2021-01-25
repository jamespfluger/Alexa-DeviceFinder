using System;
using System.Collections.Generic;
using System.Text;

namespace DeviceFinder.Abstractions
{
    public interface ILoginWithAmazonManager
    {
        AuthResult SignIn();

        AuthResult SignOut();

        AmazonUser GetUser();
    }
}
