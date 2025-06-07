package com.example.realestate.activites;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.realestate.R;
import com.example.realestate.fragments.AddAdminFragment;
import com.example.realestate.fragments.AllReservationsFragment;
import com.example.realestate.fragments.DashboardFragment;
import com.example.realestate.fragments.DeleteCustomersFragment;
import com.example.realestate.fragments.SpecialOffersFragment;
import com.example.realestate.utils.SharedPrefManager;
import com.google.android.material.navigation.NavigationView;

public class AdminHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        // Set admin name in header
        View headerView = navigationView.getHeaderView(0);
        TextView tvAdminName = headerView.findViewById(R.id.tvAdminName);
        tvAdminName.setText("Welcome, Admin");

        if (savedInstanceState == null) {
            // load dashboard or default fragment
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new DashboardFragment()) // you will implement this later
                    .commit();
            navigationView.setCheckedItem(R.id.nav_dashboard);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;
        int id = item.getItemId();

        if (id == R.id.nav_dashboard) {
            selectedFragment = new DashboardFragment();
        } else if (id == R.id.nav_add_admin) {
            selectedFragment = new AddAdminFragment();
        } else if (id == R.id.nav_all_reservations) {
            selectedFragment = new AllReservationsFragment();
        } else if (id == R.id.nav_delete_customers) {
            selectedFragment = new DeleteCustomersFragment();
        } else if (id == R.id.nav_special_offers) {
            selectedFragment = new SpecialOffersFragment();
        } else if (id == R.id.nav_logout) {
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }

        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
