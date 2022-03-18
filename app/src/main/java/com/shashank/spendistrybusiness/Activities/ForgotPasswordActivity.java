package com.shashank.spendistrybusiness.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.textfield.TextInputLayout;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.AuthViewModel;

public class ForgotPasswordActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_forgot_password);
        EditText password = findViewById(R.id.password_forgot);
        EditText confirmPassword = findViewById(R.id.repassword_forgot);
        TextInputLayout passwordLayout = findViewById(R.id.pass_forgot);
        TextInputLayout confirmPasswordLayout = findViewById(R.id.repass_forgot);
        LinearLayout linearLayout = findViewById(R.id.linearlayout_forgot);
        Button submit = findViewById(R.id.update_password);
        Intent intent = getIntent();
        final String email = intent.getStringExtra("email");
        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!password.getText().toString().equals(confirmPassword.getText().toString())) {
                    passwordLayout.setError("Password does not match");
                    confirmPasswordLayout.setError("Password does not match");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            passwordLayout.setErrorEnabled(false);
                            confirmPasswordLayout.setErrorEnabled(false);
                        }
                    },2000);
                } else if(password.getText().toString().length() < 6) {
                    passwordLayout.setError("Password must be atleast 6 characters");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            passwordLayout.setErrorEnabled(false);
                        }
                    },2000);
                } else if (password.getText().toString().isEmpty() || confirmPassword.getText().toString().isEmpty()) {
                    if (password.getText().toString().isEmpty()) {
                        passwordLayout.setError("Password cannot be empty");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                passwordLayout.setErrorEnabled(false);
                            }
                        },2000);
                    }
                    if (confirmPassword.getText().toString().isEmpty()) {
                        confirmPasswordLayout.setError("Password cannot be empty");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                confirmPasswordLayout.setErrorEnabled(false);
                            }
                        },2000);
                    }
                } else {
                    authViewModel.setNewPassword(ForgotPasswordActivity.this,linearLayout,email,password.getText().toString());

                }
            }
        });
    }
}