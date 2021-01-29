namespace DeviceFinder.Abstractions
{
    public interface IAmazonAuthManager
    {
        void SignIn();

        void SignOut();

        void RefreshUser();

        void RefreshToken();
    }
}
