package com.example.realestate.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.realestate.R;
import com.example.realestate.models.User;
import com.example.realestate.utils.DataBaseHelper;

import java.util.List;
import java.util.Map;

public class DashboardFragment extends Fragment {

    private TextView tvUsersCount, tvMalePercent, tvFemalePercent,tvReservationsCount,tvTopCountries;
    private DataBaseHelper db;

    public DashboardFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        tvUsersCount = view.findViewById(R.id.tvTotalUsers);
        tvMalePercent = view.findViewById(R.id.tvMalePercentage);
        tvFemalePercent = view.findViewById(R.id.tvFemalePercentage);
        tvReservationsCount = view.findViewById(R.id.tvReservedProperties);
        tvTopCountries = view.findViewById(R.id.tvTopCountry);


        db = new DataBaseHelper(getContext(), "Project_DB", null, 1);
        loadStatistics();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setTitle("Dashboard");
       loadStatistics();
    }

    private void loadStatistics() {
        int userCount = db.getUserCount();
        int reservationCount = db.getReservationCount();
        Map<String, Integer> genderStats = db.getGenderStats();
        Map<String, Integer> topCountries = db.getMostReservingCountries();

        tvUsersCount.setText("Total Users: " + userCount);
        tvReservationsCount.setText("Total Reservations: " + reservationCount);

        int male = genderStats.getOrDefault("male", 0);
        int female = genderStats.getOrDefault("female", 0);
        int total = male + female;

        if (total > 0) {
            int malePercent = (male * 100) / total;
            int femalePercent = 100 - malePercent;
            tvMalePercent.setText("Men: " + malePercent + "%");
            tvFemalePercent.setText("Women: " + femalePercent + "%");
        } else {
            tvMalePercent.setText("Men: N/A");
            tvFemalePercent.setText("Women: N/A");
        }

        StringBuilder countryStats = new StringBuilder();
        for (Map.Entry<String, Integer> entry : topCountries.entrySet()) {
            countryStats.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }
        tvTopCountries.setText("Top Countries:\n" + countryStats.toString().trim());
    }


}
