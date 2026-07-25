package com.example.simongame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TextView welcomeText = findViewById(R.id.welcomeText);
        ImageView logo = findViewById(R.id.splashLogo);
        TextView title = findViewById(R.id.appTitle);

        // Create fade-in animations with different delays
        AlphaAnimation fadeInWelcome = new AlphaAnimation(0.0f, 1.0f);
        fadeInWelcome.setDuration(1000);
        fadeInWelcome.setStartOffset(500);
        fadeInWelcome.setFillAfter(true);

        AlphaAnimation fadeInLogo = new AlphaAnimation(0.0f, 1.0f);
        fadeInLogo.setDuration(1000);
        fadeInLogo.setStartOffset(1000);
        fadeInLogo.setFillAfter(true);

        AlphaAnimation fadeInTitle = new AlphaAnimation(0.0f, 1.0f);
        fadeInTitle.setDuration(1000);
        fadeInTitle.setStartOffset(1500);
        fadeInTitle.setFillAfter(true);

        // Apply animations
        welcomeText.startAnimation(fadeInWelcome);
        logo.startAnimation(fadeInLogo);
        title.startAnimation(fadeInTitle);

        // Navigate to WelcomeActivity after animations
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
            finish();
        }, 3000);
    }
}
