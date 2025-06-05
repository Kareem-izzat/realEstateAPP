package com.example.realestate.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.realestate.R;
import com.example.realestate.utils.DataBaseHelper;
import com.example.realestate.utils.SharedPrefManager;
import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileManageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileManageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileManageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileManageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileManageFragment newInstance(String param1, String param2) {
        ProfileManageFragment fragment = new ProfileManageFragment();
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
    EditText etFirstName, etLastName, etPhone, etPassword, etConfirmPassword;
    MaterialButton btnUpdate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_manage, container, false);
        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);
        etPhone = view.findViewById(R.id.etPhone);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        SharedPrefManager sharedPrefManager;
        sharedPrefManager = SharedPrefManager.getInstance(requireContext());
       String email = sharedPrefManager.readString("email", "");

        Toast.makeText(getContext(), "Email: " + email, Toast.LENGTH_SHORT).show();


        if (email != null) {
            DataBaseHelper dbHelper = new DataBaseHelper(getContext(), "Project_DB", null, 1);
            Cursor cursor = dbHelper.getUserByEmail(email);
            if (cursor != null && cursor.moveToFirst()) {
                etFirstName.setText(cursor.getString(cursor.getColumnIndexOrThrow("first_name")));
                etLastName.setText(cursor.getString(cursor.getColumnIndexOrThrow("last_name")));
                etPhone.setText(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
                cursor.close();
            }
        }

        btnUpdate.setOnClickListener(v -> {
            String first = etFirstName.getText().toString().trim();
            String last = etLastName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String pass = etPassword.getText().toString();
            String confirmPass = etConfirmPassword.getText().toString();

            if (first.length() < 3 || last.length() < 3) {
                Toast.makeText(getContext(), "Names must be at least 3 characters.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (phone.length() < 7 || !phone.matches("\\+?[0-9]+")) {
                Toast.makeText(getContext(), "Invalid phone number.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pass.isEmpty()) {
                if (pass.length() < 6 || !pass.matches(".*[a-zA-Z].*") || !pass.matches(".*\\d.*") || !pass.matches(".*[@#$%^&+=!].*")) {
                    Toast.makeText(getContext(), "Password must include a letter, number, and special character.", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!pass.equals(confirmPass)) {
                    Toast.makeText(getContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            DataBaseHelper dbHelper = new DataBaseHelper(getContext(), "Project_DB", null, 1);
            boolean updated = dbHelper.updateUser(email, first, last, phone, pass.isEmpty() ? null : pass);

            if (updated) {
                Toast.makeText(getContext(), "Profile updated successfully.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Update failed.", Toast.LENGTH_SHORT).show();
            }


            Toast.makeText(getContext(), "Profile updated!", Toast.LENGTH_SHORT).show();
        });


        return view;
    }
}