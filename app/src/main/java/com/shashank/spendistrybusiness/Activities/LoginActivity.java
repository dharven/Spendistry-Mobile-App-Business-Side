package com.shashank.spendistrybusiness.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.AuthViewModel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private Button loginButton;
    private SharedPreferences sharedPreferences;
    private TextView reg;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

         email = findViewById(R.id.password_forgot);
         password = findViewById(R.id.repassword_forgot);
         loginButton = findViewById(R.id.update_password);
         reg = findViewById(R.id.reg);
         TextInputLayout emailLayout = findViewById(R.id.pass_forgot);
         TextInputLayout passLayout = findViewById(R.id.repass_forgot);
         TextView forgot = findViewById(R.id.forgot);
         LinearLayout linearLayout = findViewById(R.id.login_layout);

         AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

         sharedPreferences = getSharedPreferences("loggedIn", MODE_PRIVATE);
         SharedPreferences.Editor editor = sharedPreferences.edit();

         Window window = getWindow();
         window.setNavigationBarColor(ContextCompat.getColor(this,R.color.windowBlue));
         window.setBackgroundDrawableResource(R.color.cardBlue);
         window.setStatusBarColor(ContextCompat.getColor(this,R.color.cardBlue));



         forgot.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (!isEmailValid(email.getText().toString())) {
                     emailLayout.setError("Enter valid email");
                     new Handler().postDelayed(new Runnable() {
                         @Override
                         public void run() {
                             emailLayout.setErrorEnabled(false);
                         }
                     }, 2500);
                 }else if(email.getText().toString().isEmpty()){
                     emailLayout.setError("Enter Email");
                     new Handler().postDelayed(new Runnable() {
                         @Override
                         public void run() {
                             emailLayout.setErrorEnabled(false);
                         }
                     }, 2500);
                 } else {
                     Intent intent = new Intent(LoginActivity.this, OtpActivity.class);
                     intent.putExtra("email", email.getText().toString());
                     startActivity(intent);
                     overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                 }
             }
         });


         reg.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                 startActivity(intent);
                 overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
             }
         });

         loginButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                     if (email.getText().toString().isEmpty()) {
                         emailLayout.setError("Email is required");
                         new Handler().postDelayed(new Runnable() {
                             @Override
                             public void run() {
                                 emailLayout.setErrorEnabled(false);
                             }
                         },2500);
                     }
                     if (password.getText().toString().isEmpty()) {
                         passLayout.setError("Password is required");
                         new Handler().postDelayed(new Runnable() {
                             @Override
                             public void run() {
                                 passLayout.setErrorEnabled(false);
                             }
                         },2500);
                     }
                     return;
                 }
                 if (isEmailValid(email.getText().toString())) {

                         authViewModel.getAuth(LoginActivity.this, linearLayout,email.getText().toString(), password.getText().toString()).observe(LoginActivity.this, new Observer<String>() {
                             @Override
                             public void onChanged(String token) {
                                 if (!token.equals("")) {
                                         //
                                         editor.putBoolean("loggedIn", true);
                                         editor.putString("token", token);
                                         editor.putString("email", email.getText().toString());
                                         editor.apply();
                                         //
                                         Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                         startActivity(intent);
                                         finish();
                                     overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                                     }
                             }

                         });

                 } else {
                     emailLayout.setError("Invalid email");
                     new Handler().postDelayed(new Runnable() {
                         @Override
                         public void run() {
                             emailLayout.setErrorEnabled(false);
                         }
                     },2500);
                 }
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