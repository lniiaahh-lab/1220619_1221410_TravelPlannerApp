package com.example.travelplannerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelplannerapp.adapters.AdminReservationAdapter;
import com.example.travelplannerapp.database.DatabaseHelper;
import com.example.travelplannerapp.models.ReservationAdmin;

import java.util.List;

public class AdminReservationsFragment extends Fragment {

    private RecyclerView recyclerReservations;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_admin_reservations,
                container,
                false);

        recyclerReservations =
                view.findViewById(
                        R.id.recyclerReservations);

        recyclerReservations.setLayoutManager(
                new LinearLayoutManager(
                        getContext()));

        DatabaseHelper db =
                new DatabaseHelper(getContext());

        List<ReservationAdmin> reservations =
                db.getAllReservationsList();

        AdminReservationAdapter adapter =
                new AdminReservationAdapter(
                        reservations);

        recyclerReservations.setAdapter(adapter);

        return view;
    }
}