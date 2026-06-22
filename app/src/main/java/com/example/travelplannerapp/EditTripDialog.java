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

        View view = LayoutInflater.from(context)
                .inflate(R.layout.dialog_add_trip, null);

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

        // Fill current trip data

        etDestination.setText(trip.getDestination());
        etCountry.setText(trip.getCountry());
        etDuration.setText(String.valueOf(trip.getDurationDays()));
        etPrice.setText(String.valueOf(trip.getPrice()));
        etRating.setText(String.valueOf(trip.getRating()));
        etDescription.setText(trip.getDescription());
        etImage.setText(trip.getImage());

        AlertDialog dialog =
                new AlertDialog.Builder(context)
                        .setTitle("Edit Trip")
                        .setView(view)
                        .setPositiveButton("Update", null)
                        .setNegativeButton("Cancel", null)
                        .create();

        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(v -> {

                    String destination =
                            etDestination.getText().toString().trim();

                    String country =
                            etCountry.getText().toString().trim();

                    String duration =
                            etDuration.getText().toString().trim();

                    String price =
                            etPrice.getText().toString().trim();

                    String rating =
                            etRating.getText().toString().trim();

                    String description =
                            etDescription.getText().toString().trim();

                    String image =
                            etImage.getText().toString().trim();

                    // Empty fields validation

                    if (destination.isEmpty()
                            || country.isEmpty()
                            || duration.isEmpty()
                            || price.isEmpty()
                            || rating.isEmpty()
                            || description.isEmpty()
                            || image.isEmpty()) {

                        Toast.makeText(
                                context,
                                "Please fill all fields",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    // Destination & Country validation

                    if (!destination.matches("[a-zA-Z ]+")
                            || !country.matches("[a-zA-Z ]+")) {

                        Toast.makeText(
                                context,
                                "Enter valid content",
                                Toast.LENGTH_SHORT
                        ).show();

                        return;
                    }

                    try {

                        int durationValue =
                                Integer.parseInt(duration);

                        double priceValue =
                                Double.parseDouble(price);

                        double ratingValue =
                                Double.parseDouble(rating);

                        if (durationValue <= 0) {

                            Toast.makeText(
                                    context,
                                    "Duration must be greater than 0",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        if (priceValue <= 0) {

                            Toast.makeText(
                                    context,
                                    "Price must be greater than 0",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        if (ratingValue < 0
                                || ratingValue > 5) {

                            Toast.makeText(
                                    context,
                                    "Rating must be between 0 and 5",
                                    Toast.LENGTH_SHORT
                            ).show();

                            return;
                        }

                        trip.setDestination(destination);
                        trip.setCountry(country);
                        trip.setDurationDays(durationValue);
                        trip.setPrice(priceValue);
                        trip.setRating(ratingValue);
                        trip.setDescription(description);
                        trip.setImage(image);

                        boolean updated =
                                db.updateTrip(trip);

                        if (updated) {

                            Toast.makeText(
                                    context,
                                    "Trip Updated Successfully",
                                    Toast.LENGTH_SHORT
                            ).show();

                            refreshCallback.run();

                            dialog.dismiss();

                        } else {

                            Toast.makeText(
                                    context,
                                    "Failed To Update Trip",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }

                    } catch (NumberFormatException e) {

                        Toast.makeText(
                                context,
                                "Duration, Price and Rating must be valid numbers",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
    }
}