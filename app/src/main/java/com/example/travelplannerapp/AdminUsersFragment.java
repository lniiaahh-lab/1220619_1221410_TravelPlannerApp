package com.example.travelplannerapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.travelplannerapp.database.DatabaseHelper;

public class AdminUsersFragment extends Fragment {

    DatabaseHelper databaseHelper;

    @Override
    public android.view.View onCreateView(
            android.view.LayoutInflater inflater,
            android.view.ViewGroup container,
            Bundle savedInstanceState) {

        databaseHelper =
                new DatabaseHelper(getContext());

        ScrollView scrollView =
                new ScrollView(getContext());

        LinearLayout layout =
                new LinearLayout(getContext());

        layout.setOrientation(
                LinearLayout.VERTICAL);

        Cursor cursor =
                databaseHelper.getAllUsers();

        while (cursor.moveToNext()) {

            String email =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "email"));

            String firstName =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "firstName"));

            String lastName =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "lastName"));

            TextView tv =
                    new TextView(getContext());

            tv.setPadding(
                    20,
                    20,
                    20,
                    20);

            tv.setText(
                    firstName
                            + " "
                            + lastName
                            + "\n"
                            + email);

            layout.addView(tv);
        }

        cursor.close();

        scrollView.addView(layout);

        return scrollView;
    }
}