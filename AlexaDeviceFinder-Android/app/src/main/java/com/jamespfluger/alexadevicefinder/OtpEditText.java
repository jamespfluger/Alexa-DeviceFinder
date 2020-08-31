package com.jamespfluger.alexadevicefinder;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.AppCompatEditText;

import java.util.Random;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class OtpEditText  extends AppCompatEditText {
    private OtpEditText otpField1 = findViewById(R.id.otpField1);
    private OtpEditText otpField2 = findViewById(R.id.otpField2);
    private OtpEditText otpField3 = findViewById(R.id.otpField3);
    private OtpEditText otpField4 = findViewById(R.id.otpField4);
    private OtpEditText otpField5 = findViewById(R.id.otpField5);
    private OtpEditText otpField6 = findViewById(R.id.otpField6);

    private final ColorStateList defaultTextColors;
    private final ColorStateList defaultBackgroundTintList;

    public OtpEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        defaultTextColors = getTextColors();
        defaultBackgroundTintList = getDefaultBackgroundTintList();
    }

    public OtpEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        defaultTextColors = getTextColors();
        defaultBackgroundTintList = getDefaultBackgroundTintList();
    }

    public OtpEditText(Context context) {
        super(context);
        defaultTextColors = getTextColors();
        defaultBackgroundTintList = getDefaultBackgroundTintList();
    }

    public ColorStateList getDefaultTextColors(){
        return defaultTextColors;
    }

    public ColorStateList getDefaultBackgroundTintList(){
        return defaultBackgroundTintList;
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
            Boolean keyEventResult = super.sendKeyEvent(event);

            OtpEditText currentView = findViewById(getId());

            if (!hasFocus())
                return keyEventResult;

            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
                View nextLeftFocus = currentView.focusSearch(currentView.FOCUS_LEFT);
                currentView.setText("");

                if (nextLeftFocus != null)
                    nextLeftFocus.requestFocus();
            }
            else if (event.getAction() == KeyEvent.ACTION_UP) {
                View nextRightFocus = currentView.focusSearch(currentView.FOCUS_RIGHT);

                if (currentView.getText().length() > 0 && nextRightFocus != null) {
                    nextRightFocus.requestFocus();
                }
                else if (nextRightFocus == null){
                    InputMethodManager imm = (InputMethodManager) getContext().getApplicationContext().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(currentView.getWindowToken(), 0);
                    currentView.clearFocus();
                }
            }

            return keyEventResult;
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            if (beforeLength == 1 && afterLength == 0) {
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL))
                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }
            else{
                return super.deleteSurroundingText(beforeLength, afterLength);
            }
        }
    }
}
