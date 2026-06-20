package com.example.travelplannerapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.travelplannerapp.database.DatabaseHelper;
import com.example.travelplannerapp.models.User;

public class AddAdminFragment extends Fragment {

    EditText etEmail;
    EditText etFirstName;
    EditText etLastName;
    EditText etPhone;
    EditText etPassword;

    Button btnAddAdmin;

    DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view =
                inflater.inflate(
                        R.layout.fragment_add_admin,
                        container,
                        false);

        databaseHelper =
                new DatabaseHelper(getContext());

        etEmail = view.findViewById(R.id.etEmail);
        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);
        etPhone = view.findViewById(R.id.etPhone);
        etPassword = view.findViewById(R.id.etPassword);

        btnAddAdmin =
                view.findViewById(R.id.btnAddAdmin);

        btnAddAdmin.setOnClickListener(v -> addAdmin());

        return view;
    }

    private void addAdmin() {

        String email =
                etEmail.getText().toString().trim();

        String firstName =
                etFirstName.getText().toString().trim();

        String lastName =
                etLastName.getText().toString().trim();

        String phone =
                etPhone.getText().toString().trim();

        String password =
                etPassword.getText().toString().trim();

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Invalid Email");
            return;
        }

        if (firstName.length() < 3) {
            etFirstName.setError("Minimum 3 chars");
            return;
        }

        if (lastName.length() < 3) {
            etLastName.setError("Minimum 3 chars");
            return;
        }

        if (!isValidPassword(password)) {
            etPassword.setError("Password must contain letters and numbers");
            return;
        }

        User admin =
                new User(
                        email,
                        firstName,
                        lastName,
                        password,
                        phone,
                        "Male",
                        "Adventure");

        boolean success =
                databaseHelper.insertAdmin(admin);

        if (success) {

            Toast.makeText(
                            getContext(),
                            "Admin Added Successfully",
                            Toast.LENGTH_SHORT)
                    .show();

        } else {

            Toast.makeText(
                            getContext(),
                            "Admin Already Exists",
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
}