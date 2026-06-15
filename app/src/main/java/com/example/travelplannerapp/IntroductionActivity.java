package com.example.travelplannerapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

    private Button btnConnect;

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

                RetrofitClient.getApiService().getTrips().enqueue(new Callback<List<Trip>>() {
                    @Override
                    public void onResponse(Call<List<Trip>> call, Response<List<Trip>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(IntroductionActivity.this, "Connected! " + response.body().size() + " trips loaded.", Toast.LENGTH_SHORT).show();
                            navigateToLogin();
                        } else {
                            handleFailure("Connection failed (Error " + response.code() + "). Proceeding to Login...");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Trip>> call, Throwable t) {
                        handleFailure("Network Error. Proceeding to Login...");
                    }
                });
            }
        });
    }

    private void handleFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        // Even if connection fails, we allow the user to proceed after 2 seconds
        new Handler().postDelayed(this::navigateToLogin, 2000);
    }

    private void navigateToLogin() {
        Intent intent = new Intent(IntroductionActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
