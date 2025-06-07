package com.example.realestate.activites;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.example.realestate.fragments.ContactFragment;
import com.example.realestate.fragments.FavoritesFragment;
import com.example.realestate.fragments.FeaturedFragment;
import com.example.realestate.fragments.HomeFragment;
import com.example.realestate.fragments.ProfileManageFragment;
import com.example.realestate.fragments.PropertiesFragment;

import com.example.realestate.fragments.YourReservationsFragment;
import com.example.realestate.utils.DataBaseHelper;
import com.example.realestate.utils.SharedPrefManager;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Drawer Layout
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        // Default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new HomeFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_home);

        }

        View headerView = navigationView.getHeaderView(0);
        TextView tvUserName = headerView.findViewById(R.id.tvUserName);
        ImageView imgUserProfile = headerView.findViewById(R.id.imgUserProfile);

        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(this);
        String email = sharedPrefManager.readString("email", "");

        if (email != null) {
            DataBaseHelper db = new DataBaseHelper(this, "Project_DB", null, 1);
            Cursor cursor = db.getUserByEmail(email);

            if (cursor != null && cursor.moveToFirst()) {
                String firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
                String lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
                String profileUri = cursor.getString(cursor.getColumnIndexOrThrow("profile_image"));

                tvUserName.setText("Welcome, " + firstName + " " + lastName);

                if (profileUri != null && !profileUri.isEmpty()) {
                    try {
                        imgUserProfile.setImageURI(Uri.parse(profileUri));
                    } catch (Exception e) {
                        imgUserProfile.setImageResource(R.drawable.no_picture);
                    }
                } else {
                    imgUserProfile.setImageResource(R.drawable.no_picture);
                }

                cursor.close();
            }
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment selectedFragment = null;

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            selectedFragment = new HomeFragment();
        } else if (id == R.id.nav_properties) {
            selectedFragment = new PropertiesFragment();
        } else if (id == R.id.nav_reservations) {
            selectedFragment = new YourReservationsFragment();
        } else if (id == R.id.nav_favorites) {
            selectedFragment = new FavoritesFragment();
        } else if (id == R.id.nav_featured) {
            selectedFragment = new FeaturedFragment();
        } else if (id == R.id.nav_profile) {
            selectedFragment = new ProfileManageFragment();
        } else if (id == R.id.nav_contact) {
            selectedFragment = new ContactFragment();
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
