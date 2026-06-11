package com.example.travelplannerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class IntroductionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_introduction);

        // Handle system bars padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Views
        ImageView introImage = findViewById(R.id.introImage);
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvDescription = findViewById(R.id.tvDescription);
        Button btnGetStarted = findViewById(R.id.btnConnect);

        // Load Animation
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        
        // Apply staggered animations
        introImage.startAnimation(slideUp);
        
        Animation titleAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        titleAnim.setStartOffset(200);
        tvTitle.startAnimation(titleAnim);
        
        Animation descAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        descAnim.setStartOffset(400);
        tvDescription.startAnimation(descAnim);
        
        Animation btnAnim = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        btnAnim.setStartOffset(600);
        btnGetStarted.startAnimation(btnAnim);

        // Button Click Listener
        btnGetStarted.setOnClickListener(v -> {
            Intent intent = new Intent(IntroductionActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        });
    }
}
