package com.jamespfluger.devicefinder.controls;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.AppCompatEditText;

import com.jamespfluger.devicefinder.R;

import java.util.Locale;

public class OtpEditText extends AppCompatEditText {
    private final ColorStateList defaultTextColors = getTextColors();

    public OtpEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public OtpEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public OtpEditText(Context context) {
        super(context);
    }

    public void setErrorState() {
        getBackground().mutate().setColorFilter(getResources().getColor(R.color.red_error), PorterDuff.Mode.SRC_ATOP);
        setTextColor(getResources().getColor(R.color.red_error));
    }

    public void clearErrorState() {
        getBackground().mutate().clearColorFilter();
        setTextColor(defaultTextColors);
    }

    @Override
    protected void onFocusChanged(boolean hasFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(hasFocus, direction, previouslyFocusedRect);

        if (hasFocus) {
            requestFocus();
            setText("");

            ViewGroup viewGroup = (ViewGroup) getParent();

            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                if (child instanceof OtpEditText) {
                    ((OtpEditText) child).clearErrorState();
                }
            }
        }
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new OtpInputConnection(super.onCreateInputConnection(outAttrs), true);
    }

    private class OtpInputConnection extends InputConnectionWrapper {
        public OtpInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean sendKeyEvent(KeyEvent event) {
            boolean keyEventResult = super.sendKeyEvent(event);

            if (!hasFocus()) {
                return keyEventResult;
            }

            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                View nextLeftFocus = focusSearch(FOCUS_LEFT);
                setText("");

                if (nextLeftFocus != null) {
                    nextLeftFocus.requestFocus();
                }
            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                if (!Character.isDigit(event.getNumber())) {
                    return false;
                }

                View nextRightFocus = focusSearch(FOCUS_RIGHT);
                setText(String.format(Locale.ENGLISH, "%c", event.getNumber()));

                if (getText() != null && getText().length() > 0 && nextRightFocus != null) {
                    nextRightFocus.requestFocus();
                } else if (nextRightFocus == null) {
                    InputMethodManager imm = (InputMethodManager) getContext().getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindowToken(), 0);
                    clearFocus();
                }
            }

            return keyEventResult;
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            if (beforeLength == 1 && afterLength == 0) {
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            } else {
                return super.deleteSurroundingText(beforeLength, afterLength);
            }
        }
    }
}
