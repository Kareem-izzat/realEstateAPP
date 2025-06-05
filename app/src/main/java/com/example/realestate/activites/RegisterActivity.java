package com.example.realestate.activites;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private HashMap<String, String> countryCodes;

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
        EditText etFirstName = findViewById(R.id.etFirstName);
        EditText etLastName = findViewById(R.id.etLastName);
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        EditText etConfirmPassword = findViewById(R.id.etConfirmPassword);
        EditText etPhone = findViewById(R.id.etPhone);
        Spinner spinnerGender = findViewById(R.id.spinnerGender);
        Spinner spinnerCountry = findViewById(R.id.spinnerCountry);
        Spinner spinnerCity = findViewById(R.id.spinnerCity);
        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnBack = findViewById(R.id.btnBack);

        DataBaseHelper dbHelper = new DataBaseHelper(this, "Project_DB", null, 1);

        // Gender Spinner
        String[] genders = {"Male", "Female", "Other"};
        spinnerGender.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genders));

        // Country/City/Code Setup
        String[] countries = {"Palestine", "Jordan", "Egypt"};
        HashMap<String, String[]> cityMap = new HashMap<>();
        cityMap.put("Palestine", new String[]{"Ramallah", "Nablus", "Hebron"});
        cityMap.put("Jordan", new String[]{"Amman", "Irbid", "Zarqa"});
        cityMap.put("Egypt", new String[]{"Cairo", "Alexandria", "Giza"});

        countryCodes = new HashMap<>();
        countryCodes.put("Palestine", "+970");
        countryCodes.put("Jordan", "+962");
        countryCodes.put("Egypt", "+210");

        spinnerCountry.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, countries));
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item);
        spinnerCity.setAdapter(cityAdapter);

        // Country → City & Phone Code
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String country = countries[position];
                String[] cities = cityMap.get(country);
                cityAdapter.clear();
                if (cities != null) cityAdapter.addAll(cities);
                cityAdapter.notifyDataSetChanged();
                spinnerCity.setSelection(0);

                // Auto-insert country code if missing
                String code = countryCodes.get(country);
                String phone = etPhone.getText().toString();
                if (!phone.startsWith(code)) {
                    etPhone.setText(code);
                    etPhone.setSelection(etPhone.getText().length());
                }
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Back Button
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });

        // Register Button
        btnRegister.setOnClickListener(v -> {
            String firstName = etFirstName.getText().toString().trim();
            String lastName = etLastName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();
            String phone = etPhone.getText().toString().trim();
            String gender = spinnerGender.getSelectedItem().toString();
            String country = spinnerCountry.getSelectedItem().toString();
            String city = spinnerCity.getSelectedItem().toString();
            String expectedCode = countryCodes.get(country);

            // Validations
            if (firstName.length() < 3 || lastName.length() < 3) {
                toast("First and Last Name must be at least 3 characters each."); return;
            }

            if (!email.matches("^[\\w.-]+@[\\w.-]+\\.com$")) {
                toast("Please enter a valid email ending in .com"); return;
            }

            if (password.length() < 6 || !password.matches(".*[a-zA-Z].*") || !password.matches(".*\\d.*") || !password.matches(".*[@#$%^&+=!].*")) {
                toast("Password must be at least 6 characters and include a letter, number, and special character."); return;
            }

            if (!password.equals(confirmPassword)) {
                toast("Passwords do not match."); return;
            }

            if (!phone.startsWith(expectedCode)) {
                toast("Phone must start with the country code: " + expectedCode);
                return;
            }

            String digitsAfterCode = phone.substring(expectedCode.length());
            if (!digitsAfterCode.matches("\\d{9}")) {
                toast("Phone number must be exactly 9 digits after the country code.");
                return;
            }

            // Simulate success

            boolean inserted = dbHelper.insertUser(email, password, firstName, lastName, gender, country, city, phone);

            if (inserted) {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Email already registered. Please use another email.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
