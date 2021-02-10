namespace DeviceFinder.Abstractions
{
    public interface IAuthManager
    {
        void SignIn();

        void SignOut();

        void RefreshUser();

        void RefreshToken();
    }
}
