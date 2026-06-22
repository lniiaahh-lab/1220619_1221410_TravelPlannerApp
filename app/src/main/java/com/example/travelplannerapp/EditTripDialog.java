package com.example.travelplannerapp;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.travelplannerapp.database.DatabaseHelper;
import com.example.travelplannerapp.models.Trip;

public class EditTripDialog {

    public static void show(
            Context context,
            DatabaseHelper db,
            Trip trip,
            Runnable refreshCallback) {

        View view =
                LayoutInflater.from(context)
                        .inflate(
                                R.layout.dialog_add_trip,
                                null);


        EditText etDestination =
                view.findViewById(R.id.etDestination);

        EditText etCountry =
                view.findViewById(R.id.etCountry);

        EditText etDuration =
                view.findViewById(R.id.etDuration);

        EditText etPrice =
                view.findViewById(R.id.etPrice);

        EditText etRating =
                view.findViewById(R.id.etRating);

        EditText etDescription =
                view.findViewById(R.id.etDescription);

        EditText etImage =
                view.findViewById(R.id.etImage);



        etDestination.setText(trip.getDestination());
        etCountry.setText(trip.getCountry());
        etDuration.setText(String.valueOf(trip.getDurationDays()));
        etPrice.setText(String.valueOf(trip.getPrice()));
        etRating.setText(String.valueOf(trip.getRating()));
        etDescription.setText(trip.getDescription());
        etImage.setText(trip.getImage());

        new AlertDialog.Builder(context)
                .setTitle("Edit Trip")
                .setView(view)

                .setPositiveButton(
                        "Update",
                        (dialog, which) -> {

                            trip.setDestination(
                                    etDestination.getText().toString());

                            trip.setCountry(
                                    etCountry.getText().toString());

                            trip.setDurationDays(
                                    Integer.parseInt(
                                            etDuration.getText().toString()));

                            trip.setPrice(
                                    Double.parseDouble(
                                            etPrice.getText().toString()));

                            trip.setRating(
                                    Double.parseDouble(
                                            etRating.getText().toString()));

                            trip.setDescription(
                                    etDescription.getText().toString());

                            trip.setImage(
                                    etImage.getText().toString());

                            db.updateTrip(trip);

                            Toast.makeText(
                                            context,
                                            "Trip Updated",
                                            Toast.LENGTH_SHORT)
                                    .show();

                            refreshCallback.run();
                        })

                .setNegativeButton(
                        "Cancel",
                        null)

                .show();
    }
}