package com.example.travelplannerapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log; // <-- IMPORTED LOG FOR TESTING
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelplannerapp.api.RetrofitClient;
import com.example.travelplannerapp.database.DatabaseHelper;
import com.example.travelplannerapp.models.Trip;
import com.example.travelplannerapp.models.TripResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroductionActivity extends AppCompatActivity {

    private Button btnConnect;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        databaseHelper = new DatabaseHelper(this);
        btnConnect = findViewById(R.id.btnConnect);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnConnect.setEnabled(false);
                btnConnect.setText("Connecting...");

                RetrofitClient.getApiService().getTrips().enqueue(new Callback<TripResponse>() {
                    @Override
                    public void onResponse(Call<TripResponse> call, Response<TripResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Trip> trips = response.body().getRecord();

                            if (trips != null && !trips.isEmpty()) {
                                // 1. Save trips to database
                                databaseHelper.insertTrips(trips);

                                // --- START OF DATABASE TEST ---
                                // 2. Read them back from the database to prove they saved
                                List<Trip> savedTrips = databaseHelper.getAllTrips();
                                Log.d("DB_TEST", "Successfully saved " + savedTrips.size() + " trips to the database!");

                                // Print out the contents to Logcat
                                for (Trip t : savedTrips) {
                                    Log.d("DB_TEST", "Found in DB: " + t.getDestination() + " in " + t.getCountry());
                                }
                                // --- END OF DATABASE TEST ---

                                Toast.makeText(IntroductionActivity.this,
                                        "Connected! " + trips.size() + " trips loaded.",
                                        Toast.LENGTH_SHORT).show();

                                navigateToLogin();
                            } else {
                                showError("No trips found. Try again.");
                            }
                        } else {
                            // Added response code to help debug if it fails!
                            showError("Connection failed. Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<TripResponse> call, Throwable t) {
                        showError("Error: " + t.getMessage());
                        Log.e("API_ERROR", "Failure reason: " + t.getMessage()); // Prints exact error to Logcat
                    }
                });
            }
        });
    }

    private void showError(String message) {
        btnConnect.setEnabled(true);
        btnConnect.setText("Connect");
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(IntroductionActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}