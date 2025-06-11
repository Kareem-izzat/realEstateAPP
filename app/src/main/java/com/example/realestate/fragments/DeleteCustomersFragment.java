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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realestate.R;
import com.example.realestate.utils.DataBaseHelper;

import java.util.ArrayList;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DeleteCustomersFragment extends Fragment {

    private DataBaseHelper dbHelper;
    private ArrayList<java.util.Map<String, String>> customers;      // filtered list
    private ArrayList<java.util.Map<String, String>> allCustomers;   // full list
    private CustomerRecyclerAdapter adapter;
    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setTitle("DeleteCustomers");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete_customers, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_customers);
        EditText searchBar = view.findViewById(R.id.search_bar);
        dbHelper = new DataBaseHelper(getContext(), "Project_DB", null, 1);
        TextView emptyMessage = view.findViewById(R.id.empty_message);

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
            String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));
            if (role != null && role.equalsIgnoreCase("customer")) {
                java.util.HashMap<String, String> userMap = new java.util.HashMap<>();
                userMap.put("email", email);
                userMap.put("name", name);
                userMap.put("phone", phone);
                userMap.put("gender", gender);
                userMap.put("country", country);
                userMap.put("city", city);
                allCustomers.add(userMap);
                customers.add(userMap);
            }
        }
        cursor.close();

        adapter = new CustomerRecyclerAdapter(customers, getContext(), position -> {
            String email = customers.get(position).get("email");
            // Delete from dependent tables first to avoid foreign key constraint errors
            dbHelper.getWritableDatabase().delete("favorites", "user_email = ?", new String[]{email});
            dbHelper.getWritableDatabase().delete("reservations", "user_email = ?", new String[]{email});
            boolean deleted = dbHelper.deleteUserByEmail(email);
            if (deleted) {
                java.util.Map<String, String> toDelete = customers.remove(position);
                allCustomers.remove(toDelete);
                adapter.notifyItemRemoved(position);
                if (customers.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyMessage.setVisibility(View.VISIBLE);
                }
            }
        });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Filter
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                customers.clear();
                String query = s.toString().toLowerCase();
                for (java.util.Map<String, String> user : allCustomers) {
                    if (user.get("name").toLowerCase().contains(query) || user.get("email").toLowerCase().contains(query)) {
                        customers.add(user);
                    }
                }
                adapter.notifyDataSetChanged();
                if (customers.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyMessage.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyMessage.setVisibility(View.GONE);
                }
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}
        });

        if (customers.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyMessage.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyMessage.setVisibility(View.GONE);
        }

        return view;
    }
}
