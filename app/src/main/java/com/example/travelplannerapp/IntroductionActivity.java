package com.example.travelplannerapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelplannerapp.api.RetrofitClient;
import com.example.travelplannerapp.models.Trip;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroductionActivity extends AppCompatActivity {

    Button btnConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        btnConnect = findViewById(R.id.btnConnect);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnConnect.setEnabled(false);
                btnConnect.setText("Connecting...");

                RetrofitClient.getApiService().getTrips().enqueue(new Callback<List<trip>>() {

                    @Override
                    public void onResponse(Call<List<trip>> call, Response<List<trip>> response) {
                        if (response.isSuccessful() && response.body() != null) {

                            List<trip> trips = response.body();

                            Toast.makeText(IntroductionActivity.this,
                                    "Connected! " + trips.size() + " trips loaded.",
                                    Toast.LENGTH_SHORT).show();

                            // TODO: save trips to database (next step)

                            // Navigate to Login screen
                            Intent intent = new Intent(IntroductionActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            btnConnect.setEnabled(true);
                            btnConnect.setText("Connect");
                            Toast.makeText(IntroductionActivity.this,
                                    "Connection failed. Try again.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<trip>> call, Throwable t) {
                        btnConnect.setEnabled(true);
                        btnConnect.setText("Connect");
                        Toast.makeText(IntroductionActivity.this,
                                "Error: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}