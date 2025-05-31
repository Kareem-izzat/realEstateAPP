package com.example.realestate.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.realestate.MainActivity;
import com.example.realestate.R;
import com.example.realestate.utils.HttpManager;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        Toast.makeText(this, "Welcome Screen", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_welcome);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ImageView logo = findViewById(R.id.logoImage);
        Animation logoAnim = AnimationUtils.loadAnimation(this, R.anim.logo_appear);
        logo.startAnimation(logoAnim);
        Button btnConnect = findViewById(R.id.btnGetStarted);

        btnConnect.setOnClickListener(v -> {
            new Thread(() -> {
                // API URL
                String response = HttpManager.getData("https://github.com/Kareem-izzat/real-estate-data/blob/main/properties.json");

                // Switch to UI thread to update screen
                runOnUiThread(() -> {
                    if (response != null) {
                        Toast.makeText(this, "API Success!", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(this, MainActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Failed to connect to API", Toast.LENGTH_LONG).show();
                    }
                });
            }).start();
        });


    }
}