package com.example.travelplannerapp.api;

import com.example.travelplannerapp.models.Trip;
import com.example.travelplannerapp.models.TripResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("v3/qs/6a30289fda38895dfec4b101")
    Call<TripResponse> getTrips();
}