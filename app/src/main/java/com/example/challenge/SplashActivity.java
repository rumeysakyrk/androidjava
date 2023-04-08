package com.example.challenge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    Handler h= new Handler();
    private boolean isFirstTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i= new Intent(SplashActivity.this, MainPageActivity.class);
                startActivity(i);
                finish();
            }
        }, 5000);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        isFirstTime = sharedPref.getBoolean("isFirstTime", true);

        TextView welcomeTextView = findViewById(R.id.welcome_text_view);

        if (isFirstTime) {
            welcomeTextView.setText("Welcome");
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("isFirstTime", false);
            editor.apply();
        } else {
            welcomeTextView.setText("Hello");
        }
    }
}