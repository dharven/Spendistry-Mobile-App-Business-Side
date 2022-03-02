package com.shashank.spendistrybusiness.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.AuthViewModel;

import java.net.InetAddress;

public class SplashScreenActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private InetAddress ipAddr = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        sharedPreferences = getSharedPreferences("loggedIn", MODE_PRIVATE);

//        authViewModel.getAuth(email).observe(this, new Observer<Auth>() {
//            @Override
//            public void onChanged(Auth auth) {
        isInternetAvailable();
    }

    public void isInternetAvailable() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    ipAddr = InetAddress.getByName("google.com");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Post the result to the main thread
                        if (ipAddr != null) {
                            Toast.makeText(SplashScreenActivity.this, "Internet is available", Toast.LENGTH_SHORT).show();
                            if (sharedPreferences.getBoolean("loggedIn", false)) {
                                String email = sharedPreferences.getString("email", "not");
                                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(SplashScreenActivity.this, "Internet is not available", Toast.LENGTH_SHORT).show();
                            if (sharedPreferences.getBoolean("loggedIn", false)) {
                                String email = sharedPreferences.getString("email", "not");
                                Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                                intent.putExtra("email", email);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                    }
                });
            }
        }).start();

    }

}
