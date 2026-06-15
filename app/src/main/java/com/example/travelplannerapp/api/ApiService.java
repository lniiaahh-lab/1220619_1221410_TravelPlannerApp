package com.example.travelplannerapp.api;

import com.example.travelplannerapp.models.Trip;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("trips")
    Call<List<Trip>> getTrips();
}