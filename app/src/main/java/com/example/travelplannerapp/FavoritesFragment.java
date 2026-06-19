package com.example.travelplannerapp;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.travelplannerapp.adapters.FavoriteAdapter;
import com.example.travelplannerapp.database.DatabaseHelper;
import com.example.travelplannerapp.models.Trip;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class FavoritesFragment extends Fragment {

    RecyclerView recyclerFavorites;
    TextView tvEmptyFavorites;

    DatabaseHelper databaseHelper;

    List<Trip> favoriteTrips;

    FavoriteAdapter adapter;

    String currentUserEmail;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_favorites,
                container,
                false);

        recyclerFavorites =
                view.findViewById(R.id.recyclerFavorites);

        tvEmptyFavorites =
                view.findViewById(R.id.tvEmptyFavorites);

        recyclerFavorites.setLayoutManager(
                new LinearLayoutManager(getContext()));

        databaseHelper =
                new DatabaseHelper(getContext());

        SharedPreferences prefs =
                requireActivity().getSharedPreferences(
                        "TravelPlannerPrefs",
                        getContext().MODE_PRIVATE);

        currentUserEmail =
                prefs.getString("currentUserEmail", "");

        favoriteTrips =
                databaseHelper.getFavoriteTrips(
                        currentUserEmail);

        adapter = new FavoriteAdapter(
                favoriteTrips,
                new FavoriteAdapter.OnFavoriteActionListener() {

                    @Override
                    public void onDetailsClick(Trip trip) {
                        showTripDetails(trip);
                    }

                    @Override
                    public void onReserveClick(Trip trip) {
                        showReservationDialog(trip);
                    }

                    @Override
                    public void onRemoveClick(Trip trip,
                                              int position) {

                        databaseHelper.removeFavorite(
                                currentUserEmail,
                                trip.getId());

                        favoriteTrips.remove(position);

                        adapter.notifyItemRemoved(position);

                        Toast.makeText(
                                        getContext(),
                                        "Removed from favorites",
                                        Toast.LENGTH_SHORT)
                                .show();

                        checkEmptyState();
                    }
                });

        recyclerFavorites.setAdapter(adapter);

        checkEmptyState();

        return view;
    }

    private void checkEmptyState() {

        if (favoriteTrips.isEmpty()) {

            tvEmptyFavorites.setVisibility(View.VISIBLE);

            recyclerFavorites.setVisibility(View.GONE);

        } else {

            tvEmptyFavorites.setVisibility(View.GONE);

            recyclerFavorites.setVisibility(View.VISIBLE);
        }
    }

    private void showTripDetails(Trip trip) {

        new AlertDialog.Builder(getContext())
                .setTitle(trip.getDestination())
                .setMessage(
                        "Country: " + trip.getCountry()
                                + "\n\nDuration: "
                                + trip.getDurationDays()
                                + " days"
                                + "\n\nPrice: $"
                                + trip.getPrice()
                                + "\n\nRating: "
                                + trip.getRating()
                                + "\n\nDescription:\n"
                                + trip.getDescription())
                .setPositiveButton(
                        "Close",
                        null)
                .show();
    }

    private void showReservationDialog(Trip trip) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getContext());

        builder.setTitle(
                "Reserve: "
                        + trip.getDestination());

        View dialogView =
                LayoutInflater.from(getContext())
                        .inflate(
                                R.layout.dialog_reservation,
                                null);

        builder.setView(dialogView);

        EditText etQuantity =
                dialogView.findViewById(
                        R.id.etQuantity);

        Spinner spinnerType =
                dialogView.findViewById(
                        R.id.spinnerType);

        ArrayAdapter<String> typeAdapter =
                new ArrayAdapter<>(
                        getContext(),
                        android.R.layout.simple_spinner_item,
                        new String[]{
                                "Standard",
                                "Premium",
                                "VIP"
                        });

        typeAdapter.setDropDownViewResource(
                android.R.layout
                        .simple_spinner_dropdown_item);

        spinnerType.setAdapter(typeAdapter);

        builder.setPositiveButton(
                "Confirm",
                (dialog, which) -> {

                    String quantityStr =
                            etQuantity
                                    .getText()
                                    .toString()
                                    .trim();

                    if (quantityStr.isEmpty()) {

                        Toast.makeText(
                                        getContext(),
                                        "Enter quantity",
                                        Toast.LENGTH_SHORT)
                                .show();

                        return;
                    }

                    int quantity =
                            Integer.parseInt(
                                    quantityStr);

                    String type =
                            spinnerType
                                    .getSelectedItem()
                                    .toString();

                    String date =
                            DateFormat
                                    .getDateInstance()
                                    .format(new Date());

                    boolean success =
                            databaseHelper
                                    .addReservation(
                                            currentUserEmail,
                                            trip.getId(),
                                            date,
                                            quantity,
                                            type);

                    if (success) {

                        Toast.makeText(
                                        getContext(),
                                        "Reservation confirmed!",
                                        Toast.LENGTH_SHORT)
                                .show();

                    } else {

                        Toast.makeText(
                                        getContext(),
                                        "Reservation failed",
                                        Toast.LENGTH_SHORT)
                                .show();
                    }
                });

        builder.setNegativeButton(
                "Cancel",
                null);

        builder.show();
    }
}