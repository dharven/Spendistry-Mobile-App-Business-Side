package com.shashank.spendistrybusiness.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.utils.widget.MotionLabel;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;
import com.shashank.spendistrybusiness.Constants.Constants;
import com.shashank.spendistrybusiness.R;
import com.shashank.spendistrybusiness.ViewModels.AuthViewModel;

import java.net.InetAddress;

public class SplashScreenActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private MotionLayout motionLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        AuthViewModel authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        sharedPreferences = getSharedPreferences("loggedIn", MODE_PRIVATE);
        motionLayout = findViewById(R.id.motionLayout);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        //change background colour from getWindow()
        getWindow().setBackgroundDrawableResource(R.color.mainBlue);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        motionLayout.addTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int startId, int endId) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int startId, int endId, float progress) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                if (isConnected()) {
                    sharedPreferences.edit().putBoolean("internet", true).apply();
                    if (sharedPreferences.getBoolean("loggedIn", false)) {
                        String email = sharedPreferences.getString("email", "not");
                        Intent intent = new Intent(SplashScreenActivity.this, DashboardActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                } else {
                    sharedPreferences.edit().putBoolean("internet", false).apply();
                    if (sharedPreferences.getBoolean("loggedIn", false)) {
                        String email = sharedPreferences.getString("email", "not");
                        Intent intent = new Intent(SplashScreenActivity.this, DashboardActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int triggerId, boolean positive, float progress) {

            }
        });
    }

    public boolean isConnected()  {
        try {
            String command = "";
            command = "ping -c 1 " + Constants.URL_API.replace("https://", "").replace("/", "");
            return Runtime.getRuntime().exec(command).waitFor() == 0;
        } catch (Exception e) {
            return false;
        }
    }

}
