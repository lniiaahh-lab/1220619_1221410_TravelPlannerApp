package com.example.travelplannerapp;

import android.app.AlertDialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.travelplannerapp.database.DatabaseHelper;

public class AdminUsersFragment extends Fragment {

    DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(
            android.view.LayoutInflater inflater,
            android.view.ViewGroup container,
            Bundle savedInstanceState) {

        databaseHelper =
                new DatabaseHelper(getContext());

        ScrollView scrollView =
                new ScrollView(getContext());

        scrollView.setBackgroundColor(
                getResources().getColor(
                        R.color.colorBackground));

        LinearLayout mainLayout =
                new LinearLayout(getContext());

        mainLayout.setOrientation(
                LinearLayout.VERTICAL);

        mainLayout.setPadding(
                20,
                20,
                20,
                20);

        TextView title =
                new TextView(getContext());

        title.setText("Users Management");

        title.setTextSize(26);

        title.setTextColor(
                getResources().getColor(
                        R.color.theme_color_4));

        title.setPadding(
                20,
                20,
                20,
                30);

        title.setGravity(Gravity.CENTER);

        mainLayout.addView(title);

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

            String phone =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "phone"));

            String category =
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "category"));

            LinearLayout card =
                    new LinearLayout(getContext());

            card.setOrientation(
                    LinearLayout.VERTICAL);

            card.setPadding(
                    35,
                    35,
                    35,
                    35);

            card.setBackgroundColor(
                    Color.WHITE);

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

            params.setMargins(
                    0,
                    0,
                    0,
                    25);

            card.setLayoutParams(params);

            TextView tvName =
                    new TextView(getContext());

            tvName.setText(
                    firstName + " " + lastName);

            tvName.setTextSize(20);

            tvName.setTextColor(
                    getResources().getColor(
                            R.color.theme_color_4));

            tvName.setPadding(
                    0,
                    0,
                    0,
                    12);

            card.addView(tvName);

            TextView tvEmail =
                    new TextView(getContext());

            tvEmail.setText(
                    "Email: " + email);

            card.addView(tvEmail);

            TextView tvPhone =
                    new TextView(getContext());

            tvPhone.setText(
                    "Phone: " + phone);

            card.addView(tvPhone);

            TextView tvCategory =
                    new TextView(getContext());

            tvCategory.setText(
                    "Favorite Category: "
                            + category);

            card.addView(tvCategory);

            Button btnDelete =
                    new Button(getContext());

            btnDelete.setText(
                    "Delete User");

            btnDelete.setBackgroundTintList(
                    android.content.res.ColorStateList.valueOf(
                            getResources().getColor(
                                    R.color.theme_color_4)));

            btnDelete.setTextColor(
                    Color.WHITE);

            LinearLayout.LayoutParams btnParams =
                    new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);

            btnParams.topMargin = 25;

            btnDelete.setLayoutParams(
                    btnParams);

            btnDelete.setOnClickListener(v -> {

                new AlertDialog.Builder(
                        getContext())

                        .setTitle(
                                "Delete User")

                        .setMessage(
                                "Are you sure you want to delete "
                                        + firstName
                                        + "?")

                        .setPositiveButton(
                                "Delete",
                                (dialog, which) -> {

                                    boolean deleted =
                                            databaseHelper.deleteUser(
                                                    email);

                                    if (deleted) {

                                        Toast.makeText(
                                                        getContext(),
                                                        "User deleted successfully",
                                                        Toast.LENGTH_SHORT)
                                                .show();

                                        requireActivity()
                                                .getSupportFragmentManager()
                                                .beginTransaction()
                                                .replace(
                                                        R.id.fragmentContainer,
                                                        new AdminUsersFragment())
                                                .commit();
                                    }
                                })

                        .setNegativeButton(
                                "Cancel",
                                null)

                        .show();
            });

            card.addView(btnDelete);

            mainLayout.addView(card);
        }

        cursor.close();

        scrollView.addView(mainLayout);

        return scrollView;
    }
}