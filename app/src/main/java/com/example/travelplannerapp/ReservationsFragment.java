package com.example.travelplannerapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplannerapp.adapters.ReservationAdapter;
import com.example.travelplannerapp.database.DatabaseHelper;
import com.example.travelplannerapp.models.Reservation;

import java.util.List;

public class ReservationsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_reservations,
                container,
                false);

        RecyclerView recyclerView =
                view.findViewById(R.id.recyclerReservations);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(getContext()));

        SharedPreferences prefs =
                requireActivity().getSharedPreferences(
                        "TravelPlannerPrefs",
                        getContext().MODE_PRIVATE);

        String email =
                prefs.getString("currentUserEmail", "");

        DatabaseHelper db =
                new DatabaseHelper(getContext());

        List<Reservation> reservations =
                db.getReservationsList(email);

        ReservationAdapter adapter =
                new ReservationAdapter(reservations);

        recyclerView.setAdapter(adapter);

        return view;
    }
}