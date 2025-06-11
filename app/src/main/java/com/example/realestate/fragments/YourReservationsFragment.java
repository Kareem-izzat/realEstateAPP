package com.example.realestate.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.realestate.R;
import com.example.realestate.models.ReservationAdapter;
import com.example.realestate.models.ReservedProperty;
import com.example.realestate.utils.DataBaseHelper;
import com.example.realestate.utils.SharedPrefManager;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class YourReservationsFragment extends Fragment {

    public YourReservationsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setTitle("Your Reservations");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_reservations, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recycler_reservations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        TextView emptyMessage = view.findViewById(R.id.empty_message);

        String currentUserEmail = SharedPrefManager.getInstance(getContext()).readString("user_email", "");
        DataBaseHelper dbHelper = new DataBaseHelper(getContext(), "Project_DB", null, 1);
        List<ReservedProperty> reservedList = dbHelper.getReservationsForUser(currentUserEmail);

        if (reservedList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyMessage.setVisibility(View.VISIBLE);
            emptyMessage.setText(getString(R.string.no_reservations_found));
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyMessage.setVisibility(View.GONE);
        }

        ReservationAdapter adapter = new ReservationAdapter(reservedList, dbHelper);
        recyclerView.setAdapter(adapter);

        return view;
    }
}