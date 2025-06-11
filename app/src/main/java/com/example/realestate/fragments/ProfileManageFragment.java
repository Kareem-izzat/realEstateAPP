package com.example.realestate.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.realestate.R;
import com.example.realestate.activites.HomeActivity;
import com.example.realestate.utils.DataBaseHelper;
import com.example.realestate.utils.SharedPrefManager;
import com.google.android.material.button.MaterialButton;

public class ProfileManageFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 101;

    EditText etFirstName, etLastName, etPhone, etPassword, etConfirmPassword, etCurrentPassword;
    MaterialButton btnUpdate;
    ImageView imgProfile;
    Uri selectedImageUri;
    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setTitle("Profile management");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_manage, container, false);

        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);
        etPhone = view.findViewById(R.id.etPhone);
        etPassword = view.findViewById(R.id.etPassword);
        etConfirmPassword = view.findViewById(R.id.etConfirmPassword);
        etCurrentPassword = view.findViewById(R.id.etCurrentPassword);
        btnUpdate = view.findViewById(R.id.btnUpdate);
        imgProfile = view.findViewById(R.id.imgProfile);
        Button btnChangePhoto = view.findViewById(R.id.btnChangePhoto);

        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(requireContext());
        String email = sharedPrefManager.readString("email", "");

        if (email != null) {
            DataBaseHelper dbHelper = new DataBaseHelper(getContext(), "Project_DB", null, 1);
            Cursor cursor = dbHelper.getUserByEmail(email);
            if (cursor != null && cursor.moveToFirst()) {
                etFirstName.setText(cursor.getString(cursor.getColumnIndexOrThrow("first_name")));
                etLastName.setText(cursor.getString(cursor.getColumnIndexOrThrow("last_name")));
                etPhone.setText(cursor.getString(cursor.getColumnIndexOrThrow("phone")));

                String imageUriStr = cursor.getString(cursor.getColumnIndexOrThrow("profile_image"));
                if (imageUriStr != null && !imageUriStr.isEmpty()) {
                    try {
                        selectedImageUri = Uri.parse(imageUriStr);
                        Glide.with(requireContext())
                            .load(selectedImageUri)
                            .placeholder(R.drawable.no_picture)
                            .error(R.drawable.no_picture)
                            .circleCrop()
                            .into(imgProfile);
                    } catch (Exception e) {
                        Glide.with(requireContext())
                            .load(R.drawable.no_picture)
                            .circleCrop()
                            .into(imgProfile);
                    }
                } else {
                    Glide.with(requireContext())
                        .load(R.drawable.no_picture)
                        .circleCrop()
                        .into(imgProfile);
                }

                cursor.close();
            }
        }

        btnChangePhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);

        });

        btnUpdate.setOnClickListener(v -> {
            String first = etFirstName.getText().toString().trim();
            String last = etLastName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String pass = etPassword.getText().toString();
            String confirmPass = etConfirmPassword.getText().toString();
            String currentPass = etCurrentPassword.getText().toString();

            if (first.length() < 3 || last.length() < 3) {
                Toast.makeText(getContext(), "Names must be at least 3 characters.", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] allowedPrefixes = {"+970", "+962", "+210"};
            boolean valid = false;
            for (String prefix : allowedPrefixes) {
                if (phone.startsWith(prefix)) {
                    String rest = phone.substring(prefix.length());
                    if (rest.matches("\\d{9}")) {
                        valid = true;
                    }
                    break;
                }
            }
            if (!valid) {
                Toast.makeText(getContext(), "Phone must start with a valid country code and be followed by exactly 9 digits.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pass.isEmpty()) {
                if (currentPass.isEmpty()) {
                    Toast.makeText(getContext(), "Please enter your current password to change it.", Toast.LENGTH_SHORT).show();
                    return;
                }
                DataBaseHelper dbHelper = new DataBaseHelper(getContext(), "Project_DB", null, 1);
                if (!dbHelper.isValidUser(email, currentPass)) {
                    Toast.makeText(getContext(), "Current password is incorrect.", Toast.LENGTH_SHORT).show();
                    return;
                }
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
            boolean updated = dbHelper.updateUser(email, first, last, phone,
                    pass.isEmpty() ? null : pass,
                    selectedImageUri != null ? selectedImageUri.toString() : null);

            Toast.makeText(getContext(), updated ? "Profile updated successfully." : "Update failed.", Toast.LENGTH_SHORT).show();
            ((HomeActivity) getActivity()).refreshDrawerHeader();

        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            requireContext().getContentResolver().takePersistableUriPermission(
                    selectedImageUri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
            );

            if (selectedImageUri != null) {
                imgProfile.setImageURI(selectedImageUri);


            }
        }
    }
}
