package com.example.realestate.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.realestate.R;
import com.example.realestate.models.JsonParser;
import com.example.realestate.models.Property;
import com.example.realestate.models.PropertyAdapter;
import com.example.realestate.utils.DataBaseHelper;
import com.example.realestate.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeaturedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeaturedFragment extends Fragment {

    private RecyclerView recyclerView;
    private PropertyAdapter adapter;
    private List<Property> fullList = new ArrayList<>();
    private List<Property> filteredList = new ArrayList<>();

    private Spinner typeSpinner;
    private EditText priceInput;
    private EditText locationInput;

    private String currentUserEmail;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FeaturedFragment() {
        // Required empty public constructor
    }
    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setTitle("Featured Properties");
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeaturedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeaturedFragment newInstance(String param1, String param2) {
        FeaturedFragment fragment = new FeaturedFragment();
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


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_properties, container, false);

        recyclerView = view.findViewById(R.id.recycler_properties);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        typeSpinner = view.findViewById(R.id.spinner_type);
        priceInput = view.findViewById(R.id.et_price);
        locationInput = view.findViewById(R.id.et_location);

        currentUserEmail = SharedPrefManager.getInstance(getContext()).readString("user_email", "");
        DataBaseHelper dbHelper = new DataBaseHelper(getContext(), "Project_DB", null, 1);

        // 🔥 Filter only offered properties
        fullList.clear();
        for (Property p : JsonParser.properties) {
            if (dbHelper.isOffered(p.getId())) {
                fullList.add(p);
            }
        }

        filteredList.clear();
        filteredList.addAll(fullList);

        adapter = new PropertyAdapter(filteredList, getContext(), dbHelper, currentUserEmail);
        recyclerView.setAdapter(adapter);

        setupFilters(); // setup filters as usual

        return view;
    }


    private void setupFilters() {
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
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                filterProperties();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
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
    }

    private void filterProperties() {
        String selectedType = typeSpinner.getSelectedItem().toString();
        String maxPriceStr = priceInput.getText().toString();
        String locationQuery = locationInput.getText().toString().toLowerCase();

        int maxPrice = Integer.MAX_VALUE;
        try { maxPrice = Integer.parseInt(maxPriceStr); } catch (NumberFormatException ignored) { }

        filteredList.clear();
        for (Property p : fullList) {
            boolean matchesType = selectedType.equals("All") || p.getType().equalsIgnoreCase(selectedType);
            boolean matchesPrice = p.getPrice() <= maxPrice;
            boolean matchesLocation = p.getLocation().toLowerCase().contains(locationQuery);
            if (matchesType && matchesPrice && matchesLocation) {
                filteredList.add(p);
            }
        }
        adapter.notifyDataSetChanged();
    }
}