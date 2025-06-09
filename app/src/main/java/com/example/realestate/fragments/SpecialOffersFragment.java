package com.example.realestate.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.realestate.R;
import com.example.realestate.models.AdminPropertyAdapter;
import com.example.realestate.models.JsonParser;
import com.example.realestate.models.Property;

import java.util.ArrayList;
import java.util.List;

public class SpecialOffersFragment extends Fragment {

    private RecyclerView recyclerView;
    private AdminPropertyAdapter adapter;
    private List<Property> fullList = new ArrayList<>();
    private List<Property> filteredList = new ArrayList<>();

    private Spinner typeSpinner;
    private EditText priceInput, locationInput;
    private CheckBox offerCheckbox;

    public SpecialOffersFragment() {}

    public static SpecialOffersFragment newInstance() {
        return new SpecialOffersFragment();
    }
    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setTitle("Special offers");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_special_offers, container, false);

        recyclerView = view.findViewById(R.id.recycler_special_offers);
        typeSpinner = view.findViewById(R.id.spinner_type);
        priceInput = view.findViewById(R.id.et_max_price);
        locationInput = view.findViewById(R.id.et_location);
        offerCheckbox = view.findViewById(R.id.checkbox_offer);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        for (Property p : JsonParser.properties) {
            fullList.add(p);
        }

        filteredList.addAll(fullList); // show all at first
        adapter = new AdminPropertyAdapter(getContext(), filteredList);
        recyclerView.setAdapter(adapter);

        setupFilters();

        return view;
    }

    private void setupFilters() {
        // Fill spinner dynamically with unique types
        List<String> types = new ArrayList<>();
        types.add("All");
        for (Property p : fullList) {
            if (!types.contains(p.getType())) {
                types.add(p.getType());
            }
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, types);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(spinnerAdapter);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterProperties();
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        TextWatcher filterWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterProperties();
            }
            @Override public void afterTextChanged(Editable s) {}
        };

        priceInput.addTextChangedListener(filterWatcher);
        locationInput.addTextChangedListener(filterWatcher);

        offerCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> filterProperties());
    }

    private void filterProperties() {
        String selectedType = typeSpinner.getSelectedItem().toString();
        String maxPriceStr = priceInput.getText().toString().trim();
        String locationQuery = locationInput.getText().toString().trim().toLowerCase();
        boolean requireOffer = offerCheckbox.isChecked();

        int maxPrice = Integer.MAX_VALUE;
        try { maxPrice = Integer.parseInt(maxPriceStr); } catch (NumberFormatException ignored) {}

        filteredList.clear();
        for (Property p : fullList) {
            boolean matchesType = selectedType.equals("All") || p.getType().equalsIgnoreCase(selectedType);
            boolean matchesPrice = p.getPrice() <= maxPrice;
            boolean matchesLocation = p.getLocation().toLowerCase().contains(locationQuery);
            boolean matchesOffer = !requireOffer || (p.isFeatured() && p.getDiscountPrice() != null);

            if (matchesType && matchesPrice && matchesLocation && matchesOffer) {
                filteredList.add(p);
            }
        }

        adapter.notifyDataSetChanged();
    }
}
