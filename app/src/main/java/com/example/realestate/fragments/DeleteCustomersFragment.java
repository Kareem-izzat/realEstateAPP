package com.example.realestate.fragments;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.realestate.R;
import com.example.realestate.utils.DataBaseHelper;

import java.util.ArrayList;

public class DeleteCustomersFragment extends Fragment {

    private DataBaseHelper dbHelper;
    private ArrayList<String> customers;      // filtered list
    private ArrayList<String> allCustomers;   // full list
    private ArrayAdapter<String> adapter;
    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setTitle("DeleteCustomers");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_customers, container, false);

        ListView listView = view.findViewById(R.id.customerListView);
        EditText searchBar = view.findViewById(R.id.search_bar);
        dbHelper = new DataBaseHelper(getContext(), "Project_DB", null, 1);

        // Fetch all customers
        allCustomers = new ArrayList<>();
        customers = new ArrayList<>();
        Cursor cursor = dbHelper.getAllCustomers();
        while (cursor.moveToNext()) {
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("first_name")) + " " +
                    cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            String gender = cursor.getString(cursor.getColumnIndexOrThrow("gender"));
            String country = cursor.getString(cursor.getColumnIndexOrThrow("country"));
            String city = cursor.getString(cursor.getColumnIndexOrThrow("city"));

            String userInfo = "Name: " + name +
                    "\nEmail: " + email +
                    "\nPhone: " + phone +
                    "\nGender: " + gender +
                    "\nLocation: " + city + ", " + country;

            allCustomers.add(userInfo);
            customers.add(userInfo);
        }
        cursor.close();

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, customers);
        listView.setAdapter(adapter);

        // Filter
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                customers.clear();
                String query = s.toString().toLowerCase();
                for (String user : allCustomers) {
                    if (user.toLowerCase().contains(query)) {
                        customers.add(user);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

        // Deletion logic
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            String selected = customers.get(position);
            String email = "";
            for (String line : selected.split("\n")) {
                if (line.startsWith("Email: ")) {
                    email = line.replace("Email: ", "").trim();
                    break;
                }
            }

            if (!email.isEmpty()) {
                String finalEmail = email;
                new AlertDialog.Builder(getContext())
                        .setTitle("Delete User")
                        .setMessage("Are you sure you want to delete this customer?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            boolean deleted = dbHelper.deleteUserByEmail(finalEmail);
                            if (deleted) {
                                allCustomers.remove(selected);
                                customers.remove(position);
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), "Customer deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getContext(), "Error deleting user", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            } else {
                Toast.makeText(getContext(), "Error extracting email", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
