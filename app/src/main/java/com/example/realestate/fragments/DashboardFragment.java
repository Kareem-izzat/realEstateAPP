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

public class DashboardFragment extends Fragment {

    private TextView tvUsersCount, tvMalePercent, tvFemalePercent;
    private DataBaseHelper db;

    public DashboardFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        tvUsersCount = view.findViewById(R.id.tvTotalUsers);
        tvMalePercent = view.findViewById(R.id.tvMalePercentage);
        tvFemalePercent = view.findViewById(R.id.tvFemalePercentage);

        db = new DataBaseHelper(getContext(), "Project_DB", null, 1);
        loadStatistics();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadStatistics();
    }

    private void loadStatistics() {
        List<User> users = db.getAllUsers();

        tvUsersCount.setText("Total Users: " + users.size());

        int male = 0, female = 0;

        for (User user : users) {
            String gender = user.getGender();
            if (gender != null) {
                if (gender.equalsIgnoreCase("male")) male++;
                else if (gender.equalsIgnoreCase("female")) female++;
            }
        }

        int totalGender = male + female;
        if (totalGender > 0) {
            int malePercent = (male * 100) / totalGender;
            int femalePercent = 100 - malePercent;

            tvMalePercent.setText("Men: " + malePercent + "%");
            tvFemalePercent.setText("Women: " + femalePercent + "%");
        } else {
            tvMalePercent.setText("Men: N/A");
            tvFemalePercent.setText("Women: N/A");
        }
    }
}
