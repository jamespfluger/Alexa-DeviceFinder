using System.Threading.Tasks;
using Xamarin.Forms;

namespace DeviceFinder.Utility
{
    public class AnimationUtil
    {
        private const int shakeDuration = 50;

        /// <summary>
        /// Animates the current instance in a back-and-forth motion quickly, before slowing down and coming to a stop.
        /// </summary>
        /// <param name="element">The element to shake</param>
        /// <param name="delta">The max amount that the element will move as a percentage of its current width,
        /// A delta of 100 will move <paramref name="element"/> to the right the entire distance of its current width.
        /// A delta of 50 will move <paramref name="element"/> to the right half of its current width.
        /// A delta of 200 will move <paramref name="element"/> to the right twice its current width.
        /// </param>
        /// <example></example>
        /// <returns></returns>
        public static async Task Shake(VisualElement element, double delta = 0.9)
        {
            double shakeVector = delta * element.Width / 100.0;

            await element.TranslateTo(-shakeVector, 0, shakeDuration, Easing.SinIn);
            await element.TranslateTo(shakeVector, 0, shakeDuration);
            await element.TranslateTo(-shakeVector * .66, 0, shakeDuration);
            await element.TranslateTo(shakeVector * .66, 0, shakeDuration);
            await element.TranslateTo(-shakeVector * .33, 0, shakeDuration);
            await element.TranslateTo(shakeVector * .33, 0, shakeDuration, Easing.SinOut);

            element.TranslationX = 0;
        }
    }
}
