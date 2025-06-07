package com.example.realestate.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.realestate.R;
import com.example.realestate.activites.LoginActivity;
import com.example.realestate.utils.DataBaseHelper;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddAdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddAdminFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddAdminFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddAdminFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddAdminFragment newInstance(String param1, String param2) {
        AddAdminFragment fragment = new AddAdminFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private HashMap<String, String> countryCodes;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        // Init Views
        View view = inflater.inflate(R.layout.fragment_add_admin, container, false);
        EditText etFirstName = view.findViewById(R.id.etFirstName);
        EditText etLastName = view.findViewById(R.id.etLastName);
        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etPassword = view.findViewById(R.id.etPassword);
        EditText etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        EditText etPhone = view.findViewById(R.id.etPhone);
        Spinner spinnerGender = view.findViewById(R.id.spinnerGender);
        Spinner spinnerCountry = view.findViewById(R.id.spinnerCountry);
        Spinner spinnerCity = view.findViewById(R.id.spinnerCity);
        Button btnRegister = view.findViewById(R.id.btnRegister);


        DataBaseHelper dbHelper = new DataBaseHelper(requireContext(), "Project_DB", null, 1);

        // Gender Spinner
        String[] genders = {"Male", "Female", "Other"};
        spinnerGender.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, genders));

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

        spinnerCountry.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, countries));
        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item);
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

            String role = "admin";
            boolean inserted = dbHelper.insertUser(email, password, firstName, lastName, gender, country, city, phone, role);


            if (inserted) {
                Toast.makeText(requireContext(), "Registration successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(requireContext(), LoginActivity.class));

            } else {
                Toast.makeText(requireContext(), "Email already registered. Please use another email.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    private void toast(String msg) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
    }
}