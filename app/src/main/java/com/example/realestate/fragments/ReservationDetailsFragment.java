package com.example.realestate.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.realestate.R;
import com.example.realestate.models.Property;
import com.example.realestate.utils.DataBaseHelper;
import com.example.realestate.utils.SharedPrefManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservationDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationDetailsFragment extends Fragment {

    private Property property;
    private String currentUserEmail;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReservationDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReservationDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReservationDetailsFragment newInstance(String param1, String param2) {
        ReservationDetailsFragment fragment = new ReservationDetailsFragment();
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservation_details, container, false);

        if (getArguments() != null) {
            property = (Property) getArguments().getSerializable("property");
        }

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

        // Populate fields
        tvTitle.setText(property.getTitle());
        tvType.setText("Type: " + property.getType());
        tvPrice.setText("Price: $" + property.getPrice());
        tvLocation.setText("Location: " + property.getLocation());
        tvArea.setText("Area: " + property.getArea());
        tvBedrooms.setText("Bedrooms: " + property.getBedrooms());
        tvBathrooms.setText("Bathrooms: " + property.getBathrooms());
        tvDescription.setText(property.getDescription());

        Glide.with(requireContext())
                .load(property.getImage_url())
                .placeholder(R.drawable.icon_noimg)
                .into(imgThumbnail);

        btnBack.setOnClickListener(v ->
                requireActivity().getSupportFragmentManager().popBackStack()
        );

        btnConfirm.setOnClickListener(v -> {
            DataBaseHelper db = new DataBaseHelper(getContext(), "Project_DB", null, 1);

            // Get current timestamp
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    .format(new Date());

            boolean added = db.addReservation(currentUserEmail, property.getId(), timestamp);
            if (added) {
                Toast.makeText(getContext(), "Reservation Confirmed!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "You already reserved this property.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}