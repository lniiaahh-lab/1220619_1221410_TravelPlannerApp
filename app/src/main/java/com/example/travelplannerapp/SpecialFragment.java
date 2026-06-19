package com.example.travelplannerapp;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplannerapp.adapters.SpecialAdapter;
import com.example.travelplannerapp.database.DatabaseHelper;
import com.example.travelplannerapp.models.Trip;

import java.util.ArrayList;
import java.util.List;

public class SpecialFragment extends Fragment {

    RecyclerView recyclerSpecial;
    TextView tvEmptySpecial;

    DatabaseHelper databaseHelper;

    List<Trip> recommendedTrips;

    String currentUserEmail;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_special,
                container,
                false);

        recyclerSpecial =
                view.findViewById(R.id.recyclerSpecial);

        tvEmptySpecial =
                view.findViewById(R.id.tvEmptySpecial);

        recyclerSpecial.setLayoutManager(
                new LinearLayoutManager(getContext()));

        databaseHelper =
                new DatabaseHelper(getContext());

        SharedPreferences prefs =
                requireActivity().getSharedPreferences(
                        "TravelPlannerPrefs",
                        getContext().MODE_PRIVATE);

        currentUserEmail =
                prefs.getString("currentUserEmail", "");

        List<Trip> allTrips =
                databaseHelper.getAllTrips();

        recommendedTrips = new ArrayList<>();

        for (Trip trip : allTrips) {

            if (trip.getRating() >= 4.5) {
                recommendedTrips.add(trip);
            }
        }

        SpecialAdapter adapter =
                new SpecialAdapter(
                        recommendedTrips,
                        new SpecialAdapter.OnSpecialActionListener() {

                            @Override
                            public void onDetailsClick(Trip trip) {
                                showTripDetails(trip);
                            }

                            @Override
                            public void onFavoriteClick(Trip trip) {

                                if (databaseHelper.isFavorite(
                                        currentUserEmail,
                                        trip.getId())) {

                                    Toast.makeText(
                                                    getContext(),
                                                    "Already in favorites",
                                                    Toast.LENGTH_SHORT)
                                            .show();

                                } else {

                                    databaseHelper.addFavorite(
                                            currentUserEmail,
                                            trip.getId());

                                    Toast.makeText(
                                                    getContext(),
                                                    "Added to favorites",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        });

        recyclerSpecial.setAdapter(adapter);

        if (recommendedTrips.isEmpty()) {

            tvEmptySpecial.setVisibility(View.VISIBLE);
            recyclerSpecial.setVisibility(View.GONE);

        } else {

            tvEmptySpecial.setVisibility(View.GONE);
            recyclerSpecial.setVisibility(View.VISIBLE);
        }

        return view;
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
}