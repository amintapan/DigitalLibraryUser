package com.example.tapan.dllogin.activity.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.tapan.dllogin.R;
import com.example.tapan.dllogin.activity.login.LoginActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class SplashScreenActivity extends AppCompatActivity{

    private static int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:867246553999:android:87b5f93a4581b371") // Required for Analytics.
                .setApiKey("AIzaSyB6BLBcXEqRunKyhdJE7L4BlOqT_NPbM5s") // Required for Auth.
                .setDatabaseUrl("https://digitallibraryadmin.firebaseio.com/") // Required for RTDB.
                .build();
        FirebaseApp.initializeApp(this /*Context*/, options, getString(R.string.secondary));

        //gets error

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}