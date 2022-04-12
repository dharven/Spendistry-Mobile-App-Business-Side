package com.shashank.spendistrybusiness.Activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.AuthViewModel;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("deprecation")
public class LoginActivity extends AppCompatActivity {
    private EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);
        Button loginButton = findViewById(R.id.login);
        TextView reg = findViewById(R.id.reg);
        TextInputLayout emailLayout = findViewById(R.id.email_login_field);
        TextInputLayout passLayout = findViewById(R.id.pass_login_field);
        TextView forgot = findViewById(R.id.forgot);
        LinearLayout linearLayout = findViewById(R.id.login_layout);

        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

        SharedPreferences sharedPreferences = getSharedPreferences("loggedIn", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Window window = getWindow();
        window.setNavigationBarColor(ContextCompat.getColor(this, R.color.windowBlue));
        window.setBackgroundDrawableResource(R.color.cardBlue);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.cardBlue));


        forgot.setOnClickListener(view -> {
            if (!isEmailValid(email.getText().toString())) {
                emailLayout.setError("Enter valid email");
                new Handler().postDelayed(() -> emailLayout.setErrorEnabled(false), 2500);
            } else if (email.getText().toString().isEmpty()) {
                emailLayout.setError("Enter Email");
                new Handler().postDelayed(() -> emailLayout.setErrorEnabled(false), 2500);
            } else {
                Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                intent.putExtra("email", email.getText().toString());
                startActivity(intent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }
        });


        reg.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        });

        loginButton.setOnClickListener(view -> {
            if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                if (email.getText().toString().isEmpty()) {
                    emailLayout.setError("Email is required");
                    new Handler().postDelayed(() -> emailLayout.setErrorEnabled(false), 2500);
                }
                if (password.getText().toString().isEmpty()) {
                    passLayout.setError("Password is required");
                    new Handler().postDelayed(() -> passLayout.setErrorEnabled(false), 2500);
                }
                return;
            }
            if (isEmailValid(email.getText().toString())) {
                Dialog dialog = new Dialog(LoginActivity.this);
                dialog.setContentView(R.layout.loading_layout);
                dialog.setCancelable(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

                authViewModel.getAuth(LoginActivity.this, linearLayout, email.getText().toString(), password.getText().toString()).observe(LoginActivity.this, token -> {
                    if (!token.equals("")) {
                        //
                        editor.putBoolean("loggedIn", true);
                        editor.putString("token", token);
                        editor.putString("email", email.getText().toString());
                        editor.apply();
                        //
                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        dialog.dismiss();
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                        finish();
                    } else {
                        Snackbar snackbar = Snackbar.make(linearLayout, "Something went wrong!!", Snackbar.LENGTH_SHORT);
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.setBackgroundTint(getResources().getColor(R.color.red));
                        snackbar.show();
                    }
                });

            } else {
                emailLayout.setError("Invalid email");
                new Handler().postDelayed(() -> emailLayout.setErrorEnabled(false), 2500);
            }
        });

    }

    public static boolean isEmailValid(String emailAddress) {
        Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(emailAddress);
        return matcher.find();
    }

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
        this.finishAffinity();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    }
}