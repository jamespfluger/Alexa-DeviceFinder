using Android.Content;
using Android.Content.Res;
using Android.Graphics;
using Android.Util;
using Android.Views;
using Android.Views.InputMethods;
using AndroidX.AppCompat.Widget;
using AndroidX.Core.Content;

namespace DeviceFinder.Droid.Utilities
{
    public class OtpEditText : AppCompatEditText
    {
        private ColorStateList defaultTextColors;
        private Context _context;

        public OtpEditText(Context context, IAttributeSet attrs, int defStyle) : base(context, attrs, defStyle)
        {
            defaultTextColors = GetTextColors(context, null);
        }

        public OtpEditText(Context context, IAttributeSet attrs) : base(context, attrs)
        {
            defaultTextColors = GetTextColors(context, null);
        }

        public OtpEditText(Context context) : base(context)
        {
            defaultTextColors = GetTextColors(context, null);
        }

        public void SetErrorState()
        {
            Color errorColor = new Color(ContextCompat.GetColor(Context, Resource.Color.errorred));
            
            Background.Mutate().SetColorFilter(errorColor, PorterDuff.Mode.SrcAtop);
            SetTextColor(errorColor);
        }

        public void ClearErrorState()
        {
            Background.Mutate().ClearColorFilter();
            SetTextColor(defaultTextColors);
        }

        protected void OnFocusChanged(bool hasFocus, FocusSearchDirection direction, Rect previouslyFocusedRect)
        {
            base.OnFocusChanged(hasFocus, direction, previouslyFocusedRect);

            if (hasFocus)
            {
                RequestFocus();
                Text = "";

                ViewGroup viewGroup = Parent as ViewGroup;

                for (int i = 0; i < viewGroup.ChildCount; i++)
                {
                    View child = viewGroup.GetChildAt(i);
                    if (child is OtpEditText)
                        ((OtpEditText)child).ClearErrorState();
                }
            }
        }

        public override IInputConnection OnCreateInputConnection(EditorInfo outAttrs)
        {
            return new OtpInputConnection(base.OnCreateInputConnection(outAttrs), true, this);
        }

        private class OtpInputConnection : InputConnectionWrapper
        {
            private OtpEditText _parentEditText;

            public OtpInputConnection(IInputConnection target, bool mutable, OtpEditText editText) : base(target, mutable)
            {
                this._parentEditText = editText;
            }

            public override bool SendKeyEvent(KeyEvent keyEvent)
            {
                bool keyEventResult = base.SendKeyEvent(keyEvent);

                if (!_parentEditText.HasFocus)
                    return keyEventResult;

                if (keyEvent.Action == KeyEventActions.Down && keyEvent.KeyCode == Keycode.Del)
                {
                    View nextLeftFocus = _parentEditText.FocusSearch(FocusSearchDirection.Left);
                    _parentEditText.Text = ("");

                    if (nextLeftFocus != null)
                        nextLeftFocus.RequestFocus();
                }
                else if (keyEvent.Action == KeyEventActions.Up)
                {
                    if (!char.IsDigit(keyEvent.Number))
                        return false;

                    View nextRightFocus = _parentEditText.FocusSearch(FocusSearchDirection.Right);
                    _parentEditText.Text = (keyEvent.Number + "");

                    if (_parentEditText.Length() > 0 && nextRightFocus != null)
                    {
                        nextRightFocus.RequestFocus();
                    }
                    else if (nextRightFocus == null)
                    {
                        InputMethodManager imm = (InputMethodManager) _parentEditText.Context.GetSystemService(Context.InputMethodService);
                        imm.HideSoftInputFromWindow(_parentEditText.WindowToken, 0);
                        _parentEditText.ClearFocus();
                    }
                }

                return keyEventResult;
            }

            public bool deleteSurroundingText(int beforeLength, int afterLength)
            {
                if (beforeLength == 1 && afterLength == 0)
                {
                    return SendKeyEvent(new KeyEvent(KeyEventActions.Down, Keycode.Del))
                            && SendKeyEvent(new KeyEvent(KeyEventActions.Up, Keycode.Del));
                }
                else
                {
                    return base.DeleteSurroundingText(beforeLength, afterLength);
                }
            }
        }
    }
}