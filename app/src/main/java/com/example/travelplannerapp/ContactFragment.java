package com.example.travelplannerapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class ContactFragment extends Fragment {

    Button btnCall;
    Button btnLocation;
    Button btnEmail;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view =
                inflater.inflate(
                        R.layout.fragment_contact,
                        container,
                        false);

        btnCall =
                view.findViewById(R.id.btnCall);

        btnLocation =
                view.findViewById(R.id.btnLocation);

        btnEmail =
                view.findViewById(R.id.btnEmail);

        btnCall.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            Intent.ACTION_DIAL);

            intent.setData(
                    Uri.parse(
                            "tel:+970599123456"));

            startActivity(intent);
        });

        btnLocation.setOnClickListener(v -> {

            Uri uri =
                    Uri.parse(
                            "geo:0,0?q=Nablus,Palestine");

            Intent intent =
                    new Intent(
                            Intent.ACTION_VIEW,
                            uri);

            startActivity(intent);
        });

        btnEmail.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            Intent.ACTION_SENDTO);

            intent.setData(
                    Uri.parse(
                            "mailto:travelplanner@gmail.com"));

            intent.putExtra(
                    Intent.EXTRA_SUBJECT,
                    "Travel Planner Inquiry");

            startActivity(intent);
        });

        return view;
    }
}