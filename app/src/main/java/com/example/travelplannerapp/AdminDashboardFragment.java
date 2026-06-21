package com.example.travelplannerapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.travelplannerapp.database.DatabaseHelper;

public class AdminDashboardFragment extends Fragment {

    DatabaseHelper db;

    TextView tvAdminEmail;
    TextView tvUsersCount;
    TextView tvTripsCount;
    TextView tvReservationsCount;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_admin_dashboard,
                container,
                false);

        tvAdminEmail =
                view.findViewById(R.id.tvAdminEmail);

        tvUsersCount =
                view.findViewById(R.id.tvUsersCount);

        tvTripsCount =
                view.findViewById(R.id.tvTripsCount);

        tvReservationsCount =
                view.findViewById(R.id.tvReservationsCount);

        db = new DatabaseHelper(getContext());

        SharedPreferences prefs =
                requireActivity().getSharedPreferences(
                        "TravelPlannerPrefs",
                        getContext().MODE_PRIVATE);

        String email =
                prefs.getString(
                        "currentUserEmail",
                        "admin@admin.com");

        tvAdminEmail.setText(email);

        tvUsersCount.setText(
                String.valueOf(
                        db.getUsersCount()));

        tvTripsCount.setText(
                String.valueOf(
                        db.getTripsCount()));

        tvReservationsCount.setText(
                String.valueOf(
                        db.getReservationsCount()));

        return view;
    }
}