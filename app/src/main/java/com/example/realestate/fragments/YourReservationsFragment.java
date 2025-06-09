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

import com.example.realestate.R;
import com.example.realestate.models.ReservationAdapter;
import com.example.realestate.models.ReservedProperty;
import com.example.realestate.utils.DataBaseHelper;
import com.example.realestate.utils.SharedPrefManager;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link YourReservationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YourReservationsFragment extends Fragment {

    private RecyclerView recyclerView;
    private ReservationAdapter adapter;
    private DataBaseHelper dbHelper;
    private String currentUserEmail;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public YourReservationsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setTitle("Your Reservations");
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment YourReservationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static YourReservationsFragment newInstance(String param1, String param2) {
        YourReservationsFragment fragment = new YourReservationsFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_your_reservations, container, false);

        recyclerView = view.findViewById(R.id.recycler_reservations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        currentUserEmail = SharedPrefManager.getInstance(getContext()).readString("user_email", "");

        dbHelper = new DataBaseHelper(getContext(), "Project_DB", null, 1);
        List<ReservedProperty> reservedList = dbHelper.getReservationsForUser(currentUserEmail);

        adapter = new ReservationAdapter(reservedList, dbHelper);
        recyclerView.setAdapter(adapter);

        return view;
    }
}