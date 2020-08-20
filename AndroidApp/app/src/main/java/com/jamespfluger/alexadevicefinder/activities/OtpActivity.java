package com.jamespfluger.alexadevicefinder.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.jamespfluger.alexadevicefinder.R;

public class OtpActivity extends Activity {
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

        otpField1 = findViewById(R.id.otpField1);
        otpField2 = findViewById(R.id.otpField2);
        otpField3 = findViewById(R.id.otpField3);
        otpField4 = findViewById(R.id.otpField4);
        otpField5 = findViewById(R.id.otpField5);
        otpField6 = findViewById(R.id.otpField6);

        otpField1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable text) {
                if(text.length() == 1)
                {
                    otpField2.requestFocus();
                }
                else if(text.length() == 0)
                {
                    otpField1.clearFocus();
                }
            }
        });

        otpField2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable text) {
                if(text.length() == 1)
                {
                    otpField3.requestFocus();
                }
                else if(text.length() == 0)
                {
                    otpField1.requestFocus();
                }
            }
        });

        otpField3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable text) {
                if(text.length() == 1)
                {
                    otpField4.requestFocus();
                }
                else if(text.length() == 0)
                {
                    otpField2.requestFocus();
                }
            }
        });

        otpField4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable text) {
                if(text.length() == 1)
                {
                    otpField5.requestFocus();
                }
                else if(text.length() == 0)
                {
                    otpField3.requestFocus();
                }
            }
        });

        otpField5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable text) {
                if(text.length() == 1)
                {
                    otpField6.requestFocus();
                }
                else if(text.length() == 0)
                {
                    otpField4.requestFocus();
                }
            }
        });

        otpField6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable text) {
                if(text.length() == 1)
                {
                    otpField6.clearFocus();
                }
                else if(text.length() == 0)
                {
                    otpField5.requestFocus();
                }
            }
        });

        View.OnFocusChangeListener hideKeyboardFocus = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        };

        otpField1.setOnFocusChangeListener(hideKeyboardFocus);
        otpField2.setOnFocusChangeListener(hideKeyboardFocus);
        otpField3.setOnFocusChangeListener(hideKeyboardFocus);
        otpField4.setOnFocusChangeListener(hideKeyboardFocus);
        otpField5.setOnFocusChangeListener(hideKeyboardFocus);
        otpField6.setOnFocusChangeListener(hideKeyboardFocus);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(ConfigActivity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
