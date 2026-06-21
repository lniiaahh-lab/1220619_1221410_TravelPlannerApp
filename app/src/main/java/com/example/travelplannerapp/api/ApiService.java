package com.example.travelplannerapp.api;

import com.example.travelplannerapp.models.Trip;
import com.example.travelplannerapp.models.TripResponse;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
<<<<<<< HEAD
    @GET("v3/qs/6a38477cf5f4af5e2919ab7c")
=======
    @GET("v3/qs/6a36bc35da38895dfee35c42")
>>>>>>> 04a0fffc7b7284d4536fdd8f83b8247ea4b805f1
    Call<TripResponse> getTrips();
}