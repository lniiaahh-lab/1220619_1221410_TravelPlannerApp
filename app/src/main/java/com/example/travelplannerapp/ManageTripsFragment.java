package com.example.travelplannerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplannerapp.adapters.TripAdapter;
import com.example.travelplannerapp.database.DatabaseHelper;
import com.example.travelplannerapp.models.Trip;

import java.util.List;

public class ManageTripsFragment extends Fragment {

    private RecyclerView recyclerTrips;
    private Button btnAddTrip;

    private DatabaseHelper databaseHelper;

    private List<Trip> tripList;

    private TripAdapter adapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_manage_trips,
                container,
                false);

        recyclerTrips =
                view.findViewById(R.id.recyclerTrips);

        btnAddTrip =
                view.findViewById(R.id.btnAddTrip);

        databaseHelper =
                new DatabaseHelper(getContext());

        recyclerTrips.setLayoutManager(
                new LinearLayoutManager(getContext()));

        loadTrips();

        btnAddTrip.setOnClickListener(v ->
                showAddTripDialog());

        return view;
    }

    private void loadTrips() {

        tripList =
                databaseHelper.getAllTrips();

        adapter =
                new TripAdapter(
                        getContext(),
                        tripList,
                        new TripAdapter.OnTripClickListener() {

                            @Override
                            public void onFavoriteClick(Trip trip) {

                                // Edit Trip
                                EditTripDialog.show(
                                        getContext(),
                                        databaseHelper,
                                        trip,
                                        ManageTripsFragment.this::loadTrips);
                            }

                            @Override
                            public void onReserveClick(Trip trip) {

                                // Delete Trip
                                databaseHelper.deleteTrip(
                                        trip.getId());

                                loadTrips();
                            }

                            @Override
                            public void onTripClick(Trip trip) {

                                // Optional:
                                // Show details dialog later if you want
                            }
                        },
                        true // Admin Mode
                );

        recyclerTrips.setAdapter(adapter);
    }

    private void showAddTripDialog() {

        AddTripDialog.show(
                getContext(),
                databaseHelper,
                this::loadTrips);
    }
}