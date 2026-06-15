package com.example.travelplannerapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplannerapp.adapters.TripAdapter;
import com.example.travelplannerapp.database.DatabaseHelper;
import com.example.travelplannerapp.models.Trip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TripsFragment extends Fragment {

    RecyclerView recyclerView;
    TripAdapter adapter;
    List<Trip> allTrips, filteredTrips;
    DatabaseHelper databaseHelper;
    EditText etSearch;
    String currentUserEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trips, container, false);

        databaseHelper = new DatabaseHelper(getContext());

        SharedPreferences prefs = requireActivity()
                .getSharedPreferences("TravelPlannerPrefs", getContext().MODE_PRIVATE);
        currentUserEmail = prefs.getString("currentUserEmail", "");

        etSearch = view.findViewById(R.id.etSearch);
        recyclerView = view.findViewById(R.id.recyclerViewTrips);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        allTrips = databaseHelper.getAllTrips();
        filteredTrips = new ArrayList<>(allTrips);

        adapter = new TripAdapter(getContext(), filteredTrips, new TripAdapter.OnTripClickListener() {
            @Override
            public void onFavoriteClick(Trip trip) {
                if (databaseHelper.isFavorite(currentUserEmail, trip.getId())) {
                    databaseHelper.removeFavorite(currentUserEmail, trip.getId());
                    Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                } else {
                    databaseHelper.addFavorite(currentUserEmail, trip.getId());
                    Toast.makeText(getContext(), "Added to favorites!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onReserveClick(Trip trip) {
                showReservationDialog(trip);
            }

            @Override
            public void onTripClick(Trip trip) {
                Toast.makeText(getContext(), trip.getDestination(), Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);

        // Search
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterTrips(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Filter buttons
        Button btnAll = view.findViewById(R.id.btnFilterAll);
        Button btnPrice = view.findViewById(R.id.btnFilterPrice);
        Button btnRating = view.findViewById(R.id.btnFilterRating);
        Button btnDuration = view.findViewById(R.id.btnFilterDuration);

        btnAll.setOnClickListener(v -> {
            filteredTrips.clear();
            filteredTrips.addAll(allTrips);
            adapter.notifyDataSetChanged();
        });

        btnPrice.setOnClickListener(v -> {
            Collections.sort(filteredTrips, (a, b) -> Double.compare(a.getPrice(), b.getPrice()));
            adapter.notifyDataSetChanged();
        });

        btnRating.setOnClickListener(v -> {
            Collections.sort(filteredTrips, (a, b) -> Double.compare(b.getRating(), a.getRating()));
            adapter.notifyDataSetChanged();
        });

        btnDuration.setOnClickListener(v -> {
            Collections.sort(filteredTrips, (a, b) -> Integer.compare(a.getDurationDays(), b.getDurationDays()));
            adapter.notifyDataSetChanged();
        });

        return view;
    }

    private void filterTrips(String query) {
        filteredTrips.clear();
        if (query.isEmpty()) {
            filteredTrips.addAll(allTrips);
        } else {
            for (Trip trip : allTrips) {
                if (trip.getDestination().toLowerCase().contains(query.toLowerCase()) ||
                        trip.getCountry().toLowerCase().contains(query.toLowerCase())) {
                    filteredTrips.add(trip);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void showReservationDialog(Trip trip) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("Reserve: " + trip.getDestination());

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_reservation, null);
        builder.setView(dialogView);

        android.widget.EditText etQuantity = dialogView.findViewById(R.id.etQuantity);
        android.widget.Spinner spinnerType = dialogView.findViewById(R.id.spinnerType);

        android.widget.ArrayAdapter<String> typeAdapter = new android.widget.ArrayAdapter<>(
                getContext(), android.R.layout.simple_spinner_item,
                new String[]{"Standard", "Premium", "VIP"});
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(typeAdapter);

        builder.setPositiveButton("Confirm", (dialog, which) -> {
            String quantityStr = etQuantity.getText().toString().trim();
            if (quantityStr.isEmpty()) {
                Toast.makeText(getContext(), "Enter quantity", Toast.LENGTH_SHORT).show();
                return;
            }
            int quantity = Integer.parseInt(quantityStr);
            String type = spinnerType.getSelectedItem().toString();
            String date = java.text.DateFormat.getDateInstance().format(new java.util.Date());

            boolean success = databaseHelper.addReservation(
                    currentUserEmail, trip.getId(), date, quantity, type);

            if (success) {
                Toast.makeText(getContext(), "Reservation confirmed!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Reservation failed", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}