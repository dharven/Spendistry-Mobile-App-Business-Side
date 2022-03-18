package com.shashank.spendistrybusiness.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.kevinschildhorn.otpview.OTPView;
import com.shashank.spendistrybusiness.Constants.Global;
import com.shashank.spendistrybusiness.R;

public class OtpActivity extends AppCompatActivity {

    /**
     * Called when the activity has detected the user's press of the back
     * key. The {@link #getOnBackPressedDispatcher() OnBackPressedDispatcher} will be given a
     * chance to handle the back button before the default behavior of
     * {@link Activity#onBackPressed()} is invoked.
     *
     * @see #getOnBackPressedDispatcher()
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        OTPView otpView = findViewById(R.id.otp_view);
        TextView errorTextView = findViewById(R.id.error);

        LinearLayout optLayout = findViewById(R.id.optLayout);
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        //create random with 4 digits
        String otp = Global.sendOTP(this,optLayout,email);

        otpView.setOnFinishListener(s -> {
            if (s.equals(otp.toString())) {
                Intent intent1 = new Intent(OtpActivity.this, ForgotPasswordActivity.class);
                intent1.putExtra("email", email);
                startActivity(intent1);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return null;
            } else {
                Snackbar snackbar = Snackbar.make(optLayout, "Wrong OTP", Snackbar.LENGTH_LONG);
                snackbar.setBackgroundTint(ContextCompat.getColor(OtpActivity.this,R.color.red));
                snackbar.setTextColor(ContextCompat.getColor(OtpActivity.this,R.color.material_white));
                snackbar.show();
                return null;
            }
        });


        //send otp to email

    }
}