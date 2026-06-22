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

        new AlertDialog.Builder(context)
                .setTitle("Add Trip")
                .setView(view)

                .setPositiveButton(
                        "Save",
                        (dialog, which) -> {

                            Trip trip =
                                    new Trip(
                                            0,
                                            etDestination.getText().toString(),
                                            etCountry.getText().toString(),
                                            Integer.parseInt(etDuration.getText().toString()),
                                            Double.parseDouble(etPrice.getText().toString()),
                                            Double.parseDouble(etRating.getText().toString()),
                                            etDescription.getText().toString(),
                                            etImage.getText().toString()
                                    );

                            if (db.addTrip(trip)) {

                                Toast.makeText(
                                                context,
                                                "Trip Added",
                                                Toast.LENGTH_SHORT)
                                        .show();

                                refreshCallback.run();
                            }
                        })

                .setNegativeButton(
                        "Cancel",
                        null)

                .show();
    }
}