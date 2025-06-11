package com.example.realestate.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realestate.R;
import com.example.realestate.models.Property;
import com.example.realestate.utils.DataBaseHelper;
import com.example.realestate.utils.SharedPrefManager;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ReservationDetailsFragment extends Fragment {

    private Property property;
    private String currentUserEmail;

    public ReservationDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            property = (Property) getArguments().getSerializable("property");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setTitle("Reservation details");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservation_details, container, false);

        currentUserEmail = SharedPrefManager.getInstance(getContext()).readString("user_email", "");

        ImageView imgThumbnail = view.findViewById(R.id.img_detail_thumbnail);
        TextView tvTitle = view.findViewById(R.id.tv_detail_title);
        TextView tvType = view.findViewById(R.id.tv_detail_type);
        TextView tvPrice = view.findViewById(R.id.tv_detail_price);
        TextView tvLocation = view.findViewById(R.id.tv_detail_location);
        TextView tvArea = view.findViewById(R.id.tv_detail_area);
        TextView tvBedrooms = view.findViewById(R.id.tv_detail_bedrooms);
        TextView tvBathrooms = view.findViewById(R.id.tv_detail_bathrooms);
        TextView tvDescription = view.findViewById(R.id.tv_detail_description);
        Button btnBack = view.findViewById(R.id.btn_back_to_properties);
        Button btnConfirm = view.findViewById(R.id.btn_confirm_reservation);
        EditText editTextStartDate = view.findViewById(R.id.editTextStartDate);
        EditText editTextEndDate = view.findViewById(R.id.editTextEndDate);
        final String[] startDate = {null};
        final String[] endDate = {null};

        // Populate fields
        tvTitle.setText(property.getTitle());
        tvType.setText(getString(R.string.type_format, property.getType()));
        tvPrice.setText(getString(R.string.price_format, property.getPrice()));
        tvLocation.setText(getString(R.string.location_format, property.getLocation()));
        tvArea.setText(getString(R.string.area_format, property.getArea()));
        tvBedrooms.setText(getString(R.string.bedrooms_format, property.getBedrooms()));
        tvBathrooms.setText(getString(R.string.bathrooms_format, property.getBathrooms()));
        tvDescription.setText(property.getDescription());

        com.bumptech.glide.Glide.with(requireContext())
                .load(property.getImage_url())
                .placeholder(R.drawable.icon_noimg)
                .into(imgThumbnail);

        btnBack.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        editTextStartDate.setOnClickListener(v -> {
            if (getContext() == null) return;
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
            datePickerDialog.setOnDateSetListener((view1, year, month, dayOfMonth) -> {
                String dateStr = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                editTextStartDate.setText(dateStr);
                startDate[0] = dateStr;
                // Clear end date if it's before new start date
                if (endDate[0] != null && endDate[0].compareTo(startDate[0]) < 0) {
                    editTextEndDate.setText("");
                    endDate[0] = null;
                }
            });
            datePickerDialog.show();
        });
        editTextEndDate.setOnClickListener(v -> {
            if (getContext() == null) return;
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext());
            datePickerDialog.setOnDateSetListener((view12, year, month, dayOfMonth) -> {
                String dateStr = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                // Only allow end date >= start date
                if (startDate[0] != null && dateStr.compareTo(startDate[0]) < 0) {
                    Toast.makeText(getContext(), "End date cannot be before start date.", Toast.LENGTH_SHORT).show();
                    return;
                }
                editTextEndDate.setText(dateStr);
                endDate[0] = dateStr;
            });
            datePickerDialog.show();
        });

        btnConfirm.setOnClickListener(v -> {
            if (startDate[0] == null || endDate[0] == null) {
                Toast.makeText(getContext(), "Please select both start and end dates.", Toast.LENGTH_SHORT).show();
                return;
            }
            if (endDate[0].compareTo(startDate[0]) < 0) {
                Toast.makeText(getContext(), "End date cannot be before start date.", Toast.LENGTH_SHORT).show();
                return;
            }
            try (DataBaseHelper db = new DataBaseHelper(getContext(), "Project_DB", null, 1)) {
                boolean added = db.addReservation(currentUserEmail, property.getId(), startDate[0], endDate[0]);
                if (added) {
                    Toast.makeText(getContext(), "Reservation Confirmed!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Reservation failed: Dates overlap with another reservation.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}