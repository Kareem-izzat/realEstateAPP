package com.example.realestate.fragments;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.realestate.R;
import com.example.realestate.models.Reservation;
import com.example.realestate.models.ReservationAdapter;
import com.example.realestate.utils.DataBaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllReservationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllReservationsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllReservationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllReservationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllReservationsFragment newInstance(String param1, String param2) {
        AllReservationsFragment fragment = new AllReservationsFragment();
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
    public void onResume() {
        super.onResume();
        requireActivity().setTitle("All Reservations");
    }

    private RecyclerView recyclerView;
    private com.example.realestate.models.AdminReservationAdapter adapter;
    private List<Reservation> reservationList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_reservations, container, false);

        recyclerView = view.findViewById(R.id.recyclerReservations);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DataBaseHelper db = new DataBaseHelper(getContext(), "Project_DB", null, 1);
        reservationList = db.getAllReservations();  // you must have this function implemented

        adapter = new com.example.realestate.models.AdminReservationAdapter(getContext(), reservationList);
        recyclerView.setAdapter(adapter);

        return view;
    }

}