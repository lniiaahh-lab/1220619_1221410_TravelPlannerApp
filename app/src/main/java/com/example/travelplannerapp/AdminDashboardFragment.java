package com.example.travelplannerapp;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class AdminDashboardFragment extends Fragment {

    @Override
    public android.view.View onCreateView(
            android.view.LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        TextView tv = new TextView(getContext());

        tv.setText("Admin Dashboard");

        tv.setTextSize(28);

        tv.setGravity(Gravity.CENTER);

        return tv;
    }
}