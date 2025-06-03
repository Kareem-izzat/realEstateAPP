package com.example.realestate.activites;

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

import com.example.realestate.R;
import com.example.realestate.utils.ConnectionAsyncTask;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_welcome);

        Toast.makeText(this, "Welcome Screen", Toast.LENGTH_SHORT).show();

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
            String URL  = "https://raw.githubusercontent.com/Kareem-izzat/real-estate-data/main/properties.json";
            new ConnectionAsyncTask(WelcomeActivity.this).execute(URL);
        });


    }
}