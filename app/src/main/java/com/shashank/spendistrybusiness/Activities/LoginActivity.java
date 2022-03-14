package com.shashank.spendistrybusiness.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.AuthViewModel;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

         email = findViewById(R.id.email_login);
         password = findViewById(R.id.pass_login);
         loginButton = findViewById(R.id.login);
         reg = findViewById(R.id.reg);


         AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);

         sharedPreferences = getSharedPreferences("loggedIn", MODE_PRIVATE);
         SharedPreferences.Editor editor = sharedPreferences.edit();

         Window window = getWindow();
         window.setNavigationBarColor(ContextCompat.getColor(this,R.color.windowBlue));
         window.setBackgroundDrawableResource(R.color.cardBlue);
         window.setStatusBarColor(ContextCompat.getColor(this,R.color.cardBlue));


         reg.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                 startActivity(intent);
             }
         });

         loginButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if (isEmailValid(email.getText().toString())) {
                     if (password.getText().toString().length()>5){
                         authViewModel.getAuth(email.getText().toString(), password.getText().toString()).observe(LoginActivity.this, new Observer<String>() {
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
                                     }
//                                  else {
//                                     authViewModel.deleteAccount(email.getText().toString());
//                                     Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
//                                     startActivity(intent);
//                                 }
                             }

                         });
                     } else {
                         Toast.makeText(LoginActivity.this, "Password must be at " +
                                 "least 6 characters long", Toast.LENGTH_SHORT).show();
                     }
                 } else {
                     Toast.makeText(LoginActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                 }
             }
         });

     }
    public static boolean isEmailValid(String emailAddress) {
        Pattern pattern = Pattern.compile("^[A-Z0-9_.!#$%&'*+/=?`{|}~^-]+(?:\\.[A-Z0-9_.!#$%&'*+/=?`{|}~^-]+↵\n" +
                ")*@[A-Z0-9-]+(?:\\.[A-Z0-9-]+)*$", Pattern.CASE_INSENSITIVE);
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
    }
}