package com.jamespfluger.alexadevicefinder.activities;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.jamespfluger.alexadevicefinder.OtpEditText;
import com.jamespfluger.alexadevicefinder.R;

public class OtpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        findViewById(R.id.otpControlsLayout).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (getCurrentFocus() == null)
                    return false;

                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                getCurrentFocus().clearFocus();

                return true;
            }});

        findViewById(R.id.otpVerifyButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewGroup viewGroup = (ViewGroup) findViewById(R.id.otpFieldRow);
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    View child = viewGroup.getChildAt(i);
                    if (child instanceof OtpEditText)
                    {
                        ViewCompat.setBackgroundTintList((OtpEditText)child, ColorStateList.valueOf(getResources().getColor(R.color.red)));
                        ((OtpEditText)child).setTextColor(getResources().getColor(R.color.red));
                    }
                }

                final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);
                View editTexts = findViewById(R.id.otpFieldRow);
                editTexts.startAnimation(animation);


                //findViewById(R.id.verificationPanel).setVisibility(View.VISIBLE);
                //setViewAndChildrenEnabled(findViewById(R.id.otpControlsLayout), false);

                /*
                SIMULATE VERIFICATION:
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        findViewById(R.id.verificationPanel).setVisibility(View.GONE);
                        setViewAndChildrenEnabled(findViewById(R.id.otpControlsLayout), true);
                    }
                }, 2500);
                 */
            }
        });

        addCursorPositionFocusListener((OtpEditText)findViewById(R.id.otpField1));
        addCursorPositionFocusListener((OtpEditText)findViewById(R.id.otpField2));
        addCursorPositionFocusListener((OtpEditText)findViewById(R.id.otpField3));
        addCursorPositionFocusListener((OtpEditText)findViewById(R.id.otpField4));
        addCursorPositionFocusListener((OtpEditText)findViewById(R.id.otpField5));
        addCursorPositionFocusListener((OtpEditText)findViewById(R.id.otpField6));
    }

    private static void setViewAndChildrenEnabled(View view, boolean enabled) {
        view.setEnabled(enabled);
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View child = viewGroup.getChildAt(i);
                setViewAndChildrenEnabled(child, enabled);
            }
        }
    }

    private void addCursorPositionFocusListener(final OtpEditText field){
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

                    ViewGroup viewGroup = (ViewGroup) findViewById(R.id.otpFieldRow);
                    for (int i = 0; i < viewGroup.getChildCount(); i++) {
                        View child = viewGroup.getChildAt(i);
                        if (child instanceof OtpEditText)
                        {
                            ViewCompat.setBackgroundTintList((OtpEditText)child, ((OtpEditText) child).getDefaultBackgroundTintList());
                            ((OtpEditText) child).setTextColor(((OtpEditText) child).getDefaultTextColors());
                        }
                    }
                }
            }
        });
    }
}
