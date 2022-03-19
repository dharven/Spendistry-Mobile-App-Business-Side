package com.shashank.spendistrybusiness.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.kevinschildhorn.otpview.OTPView;
import com.shashank.spendistrybusiness.Constants.Global;
import com.shashank.spendistrybusiness.Models.Dashboard;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.AuthViewModel;

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
        Toolbar toolbar = findViewById(R.id.Otp_toolbar);
        TextView resend = findViewById(R.id.resend);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("");
        }

        LinearLayout optLayout = findViewById(R.id.optLayout);
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);


        authViewModel.sendOTP(optLayout, email);


        otpView.setOnFinishListener(s -> {
            authViewModel.verifyOTP(optLayout, email, Integer.parseInt(s)).observe(this, response -> {
                if (response.equals("201")) {
                    Intent intent1 = new Intent(OtpActivity.this, ForgotPasswordActivity.class);
                    intent1.putExtra("email", email);
                    startActivity(intent1);
                } else if (response.equals("401")) {
                    Snackbar snackbar = Snackbar.make(optLayout, "Invalid OTP!", Snackbar.LENGTH_SHORT);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.red));
                    snackbar.show();
                }
            });
            return null;
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authViewModel.sendOTP(optLayout, email);
            }
        });


        //send otp to email

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(OtpActivity.this, DashboardActivity.class));
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}