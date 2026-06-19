package com.example.travelplannerapp;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.travelplannerapp.database.DatabaseHelper;

public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 100;

    ImageView imgProfile;

    TextView tvEmail;

    EditText etFirstName;
    EditText etLastName;
    EditText etPhone;
    EditText etPassword;

    Button btnSave;

    DatabaseHelper databaseHelper;

    String currentUserEmail;

    SharedPreferences prefs;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view =
                inflater.inflate(
                        R.layout.fragment_profile,
                        container,
                        false);

        imgProfile = view.findViewById(R.id.imgProfile);

        tvEmail = view.findViewById(R.id.tvEmail);

        etFirstName = view.findViewById(R.id.etFirstName);

        etLastName = view.findViewById(R.id.etLastName);

        etPhone = view.findViewById(R.id.etPhone);

        etPassword = view.findViewById(R.id.etPassword);

        btnSave = view.findViewById(R.id.btnSave);

        databaseHelper =
                new DatabaseHelper(getContext());

        prefs =
                requireActivity().getSharedPreferences(
                        "TravelPlannerPrefs",
                        Activity.MODE_PRIVATE);

        currentUserEmail =
                prefs.getString(
                        "currentUserEmail",
                        "");

        loadUserData();

        loadProfileImage();

        imgProfile.setOnClickListener(v -> openGallery());

        btnSave.setOnClickListener(v -> saveChanges());

        return view;
    }

    private void loadUserData() {

        Cursor cursor =
                databaseHelper.getUserByEmail(
                        currentUserEmail);

        if (cursor != null && cursor.moveToFirst()) {

            tvEmail.setText(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "email")));

            etFirstName.setText(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "firstName")));

            etLastName.setText(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "lastName")));

            etPhone.setText(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "phone")));

            etPassword.setText(
                    cursor.getString(
                            cursor.getColumnIndexOrThrow(
                                    "password")));

            cursor.close();
        }
    }

    private void saveChanges() {

        String firstName =
                etFirstName.getText()
                        .toString()
                        .trim();

        String lastName =
                etLastName.getText()
                        .toString()
                        .trim();

        String phone =
                etPhone.getText()
                        .toString()
                        .trim();

        String password =
                etPassword.getText()
                        .toString()
                        .trim();

        if (firstName.length() < 3) {

            etFirstName.setError(
                    "Minimum 3 characters");

            return;
        }

        if (lastName.length() < 3) {

            etLastName.setError(
                    "Minimum 3 characters");

            return;
        }

        if (TextUtils.isEmpty(phone)) {

            etPhone.setError(
                    "Phone number is required");

            return;
        }

        if (!isValidPassword(password)) {

            etPassword.setError(
                    "Password must be 6+ chars, include a letter and a number");

            return;
        }

        boolean success =
                databaseHelper.updateUserProfile(
                        currentUserEmail,
                        firstName,
                        lastName,
                        phone,
                        password);

        if (success) {

            Toast.makeText(
                            getContext(),
                            "Profile updated successfully!",
                            Toast.LENGTH_SHORT)
                    .show();

        } else {

            Toast.makeText(
                            getContext(),
                            "Update failed",
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private boolean isValidPassword(String password) {

        if (password.length() < 6)
            return false;

        boolean hasLetter = false;

        boolean hasDigit = false;

        for (char c : password.toCharArray()) {

            if (Character.isLetter(c))
                hasLetter = true;

            if (Character.isDigit(c))
                hasDigit = true;
        }

        return hasLetter && hasDigit;
    }

    private void openGallery() {

        Intent intent =
                new Intent(
                        Intent.ACTION_PICK);

        intent.setType("image/*");

        startActivityForResult(
                intent,
                PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data);

        if (requestCode ==
                PICK_IMAGE_REQUEST
                && resultCode ==
                Activity.RESULT_OK
                && data != null
                && data.getData() != null) {

            Uri imageUri =
                    data.getData();

            imgProfile.setImageURI(
                    imageUri);

            prefs.edit()
                    .putString(
                            "profileImageUri",
                            imageUri.toString())
                    .apply();
        }
    }

    private void loadProfileImage() {

        String imageUriString =
                prefs.getString(
                        "profileImageUri",
                        null);

        if (imageUriString != null) {

            Uri imageUri =
                    Uri.parse(
                            imageUriString);

            imgProfile.setImageURI(
                    imageUri);
        }
    }
}