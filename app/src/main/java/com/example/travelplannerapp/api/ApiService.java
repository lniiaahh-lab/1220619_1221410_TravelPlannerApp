package com.example.travelplannerapp.api;

import com.example.travelplannerapp.models.Trip;
import com.example.travelplannerapp.models.TripResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("v3/qs/6a4121c7da38895dfe0b9d5e")
    Call<TripResponse> getTrips();
}