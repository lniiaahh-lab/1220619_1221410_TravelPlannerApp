package com.example.travelplannerapp;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.travelplannerapp.database.DatabaseHelper;
import com.example.travelplannerapp.models.Trip;

public class AddTripDialog {

    public static void show(
            Context context,
            DatabaseHelper db,
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

        AlertDialog dialog =
                new AlertDialog.Builder(context)
                        .setTitle("Add Trip")
                        .setView(view)
                        .setPositiveButton(
                                "Save",
                                null)
                        .setNegativeButton(
                                "Cancel",
                                null)
                        .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(v -> {

                    String destination =
                            etDestination.getText()
                                    .toString()
                                    .trim();

                    String country =
                            etCountry.getText()
                                    .toString()
                                    .trim();

                    String durationStr =
                            etDuration.getText()
                                    .toString()
                                    .trim();

                    String priceStr =
                            etPrice.getText()
                                    .toString()
                                    .trim();

                    String ratingStr =
                            etRating.getText()
                                    .toString()
                                    .trim();

                    String description =
                            etDescription.getText()
                                    .toString()
                                    .trim();

                    String image =
                            etImage.getText()
                                    .toString()
                                    .trim();

                    // Check empty fields
                    if (destination.isEmpty()
                            || country.isEmpty()
                            || durationStr.isEmpty()
                            || priceStr.isEmpty()
                            || ratingStr.isEmpty()
                            || description.isEmpty()
                            || image.isEmpty()) {

                        Toast.makeText(
                                        context,
                                        "Please fill all fields",
                                        Toast.LENGTH_SHORT)
                                .show();

                        return;
                    }

                    try {

                        int duration =
                                Integer.parseInt(durationStr);

                        double price =
                                Double.parseDouble(priceStr);

                        double rating =
                                Double.parseDouble(ratingStr);

                        if (rating < 0 || rating > 5) {

                            Toast.makeText(
                                            context,
                                            "Rating must be between 0 and 5",
                                            Toast.LENGTH_SHORT)
                                    .show();

                            return;
                        }

                        Trip trip =
                                new Trip(
                                        0, // Auto Increment ID
                                        destination,
                                        country,
                                        duration,
                                        price,
                                        rating,
                                        description,
                                        image
                                );

                        if (db.addTrip(trip)) {

                            Toast.makeText(
                                            context,
                                            "Trip Added Successfully",
                                            Toast.LENGTH_SHORT)
                                    .show();

                            refreshCallback.run();

                            dialog.dismiss();

                        } else {

                            Toast.makeText(
                                            context,
                                            "Failed to add trip",
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }

                    } catch (NumberFormatException e) {

                        Toast.makeText(
                                        context,
                                        "Duration, Price and Rating must be valid numbers",
                                        Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }
}