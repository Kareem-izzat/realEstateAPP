package com.example.realestate.activites;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.realestate.R;
import com.example.realestate.utils.DataBaseHelper;
import com.example.realestate.utils.SharedPrefManager;

public class LoginActivity extends AppCompatActivity {
    SharedPrefManager sharedPrefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Views
        TextView tvRegisterLink = findViewById(R.id.tvRegisterLink);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        CheckBox cbRememberMe = findViewById(R.id.cbRememberMe);
        cbRememberMe.setChecked(true);
        Button btnLogin = findViewById(R.id.btnLogin);

        DataBaseHelper dbHelper = new DataBaseHelper(this, "Project_DB", null, 1);

        // Shared Preferences
        sharedPrefManager = SharedPrefManager.getInstance(this);
        String savedEmail = sharedPrefManager.readString("email", "");
        etEmail.setText(savedEmail);

        tvRegisterLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });




        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();
            sharedPrefManager.writeString("user_email", email); // always save email in shared preferences

            if (cbRememberMe.isChecked()) {
                sharedPrefManager.writeString("email", email);
            } else {
                sharedPrefManager.writeString("email", "");
            }

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and password are required", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.isValidUser(email, password)) {
                String role = dbHelper.getUserRole(email);
                if (role != null) {


                    if (role.equals("admin")) {
                        Toast.makeText(this, "Admin login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
                    } else {
                        Toast.makeText(this, "User login successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                    }
                    finish();
                } else {
                    Toast.makeText(this, "Failed to determine user role", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });


    }
}