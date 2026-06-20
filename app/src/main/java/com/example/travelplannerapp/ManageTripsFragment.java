package com.example.travelplannerapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class ManageTripsFragment extends Fragment {

    @Override
    public android.view.View onCreateView(
            android.view.LayoutInflater inflater,
            android.view.ViewGroup container,
            Bundle savedInstanceState) {

        TextView tv = new TextView(getContext());

        tv.setText("Manage Trips");

        return tv;
    }
}