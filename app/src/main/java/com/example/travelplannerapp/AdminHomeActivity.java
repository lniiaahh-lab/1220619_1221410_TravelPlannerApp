package com.example.travelplannerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

public class AdminHomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        TextView tvName =
                navigationView.getHeaderView(0)
                        .findViewById(R.id.tvNavUserName);

        TextView tvEmail =
                navigationView.getHeaderView(0)
                        .findViewById(R.id.tvNavUserEmail);

        tvName.setText("Administrator");
        tvEmail.setText("admin@admin.com");

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(
                        this,
                        drawerLayout,
                        toolbar,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {

            loadFragment(new AdminDashboardFragment());

            navigationView.setCheckedItem(
                    R.id.nav_admin_dashboard);
        }
    }

    private void loadFragment(Fragment fragment) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(
            @NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_admin_dashboard) {

            loadFragment(
                    new AdminDashboardFragment());

        } else if (id == R.id.nav_view_users) {

            loadFragment(
                    new AdminUsersFragment());

        } else if (id == R.id.nav_add_admin) {

            loadFragment(
                    new AddAdminFragment());

        } else if (id == R.id.nav_manage_trips) {

            loadFragment(
                    new ManageTripsFragment());

        } else if (id == R.id.nav_view_reservations) {

            loadFragment(
                    new AdminReservationsFragment());

        } else if (id == R.id.nav_admin_logout) {

            logout();
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void logout() {

        SharedPreferences prefs =
                getSharedPreferences(
                        "TravelPlannerPrefs",
                        MODE_PRIVATE);

        prefs.edit()
                .remove("currentUserEmail")
                .apply();

        Intent intent =
                new Intent(
                        AdminHomeActivity.this,
                        LoginActivity.class);

        intent.setFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);

        finish();
    }
}