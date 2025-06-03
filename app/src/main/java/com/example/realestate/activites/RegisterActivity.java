package com.example.realestate.activites;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.realestate.R;
import com.example.realestate.utils.DataBaseHelper;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // Init Views
        EditText etFullName = findViewById(R.id.etFullName);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        EditText etConfirmPassword = findViewById(R.id.etConfirmPassword);
        EditText etPhone = findViewById(R.id.etPhone);
        Spinner spinnerGender = findViewById(R.id.spinnerGender);
        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnBack = findViewById(R.id.btnBack);

        DataBaseHelper dbHelper = new DataBaseHelper(this, "Project_DB", null, 1);

        String[] options = {"Male", "Female", "Other"};
        // Gender Spinner values
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                options
        );
        spinnerGender.setAdapter(genderAdapter);

        btnBack.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Optional: prevents back-stack to this screen
        });

        // Register Button
        btnRegister.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString().trim();
            String[] nameParts = fullName.split(" ");
            String firstName = nameParts.length > 0 ? nameParts[0] : "";
            String lastName = nameParts.length > 1 ? nameParts[1] : "";

            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();
            String phone = etPhone.getText().toString().trim();
            String gender = spinnerGender.getSelectedItem().toString();

            // ✅ Validation
            if (firstName.length() < 3 || lastName.length() < 3) {
                Toast.makeText(this, "Please enter a valid full name (min 3 characters each).", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!email.contains("@") || !email.endsWith(".com")) {
                Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6 || !password.matches(".*[a-zA-Z].*") || !password.matches(".*\\d.*") || !password.matches(".*[@#$%^&+=!].*")) {
                Toast.makeText(this, "Password must be at least 6 characters and include a letter, number, and special character.", Toast.LENGTH_LONG).show();
                return;
            }

            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Insert into SQLite
            boolean inserted = dbHelper.insertUser(email, password, firstName, lastName);

            if (inserted) {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "User already exists or DB error.", Toast.LENGTH_SHORT).show();
            }
        });


    }
}