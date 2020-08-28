package com.jamespfluger.alexadevicefinder.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.jamespfluger.alexadevicefinder.R;

public class OtpActivity extends AppCompatActivity {
    private EditText otpField1;
    private EditText otpField2;
    private EditText otpField3;
    private EditText otpField4;
    private EditText otpField5;
    private EditText otpField6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        WindowManager.LayoutParams p = new WindowManager.LayoutParams();


        findViewById(R.id.otpActivity).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (getCurrentFocus() == null)
                    return false;

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                getCurrentFocus().clearFocus();

                return true;
            }});


        otpField1 = findViewById(R.id.otpField1);
        otpField2 = findViewById(R.id.otpField2);
        otpField3 = findViewById(R.id.otpField3);
        otpField4 = findViewById(R.id.otpField4);
        otpField5 = findViewById(R.id.otpField5);
        otpField6 = findViewById(R.id.otpField6);


        otpField1.addTextChangedListener(getTextChangedListener(otpField1, otpField2));
        otpField2.addTextChangedListener(getTextChangedListener(otpField1, otpField3));
        otpField3.addTextChangedListener(getTextChangedListener(otpField2, otpField4));
        otpField4.addTextChangedListener(getTextChangedListener(otpField3, otpField5));
        otpField5.addTextChangedListener(getTextChangedListener(otpField4, otpField6));
        otpField6.addTextChangedListener(getTextChangedListener(otpField5, otpField6));

        addCursorPositionFocusListener(otpField1);
        addCursorPositionFocusListener(otpField2);
        addCursorPositionFocusListener(otpField3);
        addCursorPositionFocusListener(otpField4);
        addCursorPositionFocusListener(otpField5);
        addCursorPositionFocusListener(otpField6);
    }

    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }*/

    public void stealFocusFromEditTexts(View view) {
        int a = 3;
    }

    private TextWatcher getTextChangedListener(final EditText previousField, final EditText nextField) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable text) {
                if (text.length() == 0)
                {
                    previousField.requestFocus();
                }
                else
                {
                    nextField.requestFocus();
                }
            }
        };
    }

    private void addCursorPositionFocusListener(final EditText field){
        field.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    field.post(new Runnable() {
                        @Override
                        public void run() {
                            field.setSelection(field.getText().length());
                        }
                    });
                }
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
