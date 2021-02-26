namespace DeviceFinder.Abstractions
{
    public interface IToaster
    {
        void ShowShortToast(string message);
        void ShowLongToast(string message);
    }
}
