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

        // Empty fields validation
        if (TextUtils.isEmpty(email)
                || TextUtils.isEmpty(firstName)
                || TextUtils.isEmpty(lastName)
                || TextUtils.isEmpty(phone)
                || TextUtils.isEmpty(password)) {

            Toast.makeText(
                            getContext(),
                            "Please fill all fields",
                            Toast.LENGTH_SHORT)
                    .show();

            return;
        }

        // Email validation
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            etEmail.setError("Enter valid email");

            Toast.makeText(
                            getContext(),
                            "Enter valid email",
                            Toast.LENGTH_SHORT)
                    .show();

            return;
        }

        // Check if email already exists
        if (databaseHelper.emailExists(email)) {

            etEmail.setError("Email already exists");

            Toast.makeText(
                            getContext(),
                            "Email already exists",
                            Toast.LENGTH_SHORT)
                    .show();

            return;
        }

        // First name validation
        if (!firstName.matches("[a-zA-Z ]+")) {

            etFirstName.setError("Enter valid first name");

            Toast.makeText(
                            getContext(),
                            "Enter valid first name",
                            Toast.LENGTH_SHORT)
                    .show();

            return;
        }

        if (firstName.length() < 3) {

            etFirstName.setError("Minimum 3 characters");

            return;
        }

        // Last name validation
        if (!lastName.matches("[a-zA-Z ]+")) {

            etLastName.setError("Enter valid last name");

            Toast.makeText(
                            getContext(),
                            "Enter valid last name",
                            Toast.LENGTH_SHORT)
                    .show();

            return;
        }

        if (lastName.length() < 3) {

            etLastName.setError("Minimum 3 characters");

            return;
        }

        // Phone validation
        if (!phone.matches("[0-9]{9,15}")) {

            etPhone.setError("Enter valid phone number");

            Toast.makeText(
                            getContext(),
                            "Enter valid phone number",
                            Toast.LENGTH_SHORT)
                    .show();

            return;
        }

        // Password validation
        if (!isValidPassword(password)) {

            etPassword.setError(
                    "Password must contain letters and numbers");

            Toast.makeText(
                            getContext(),
                            "Password must contain letters and numbers",
                            Toast.LENGTH_SHORT)
                    .show();

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

            // Clear fields
            etEmail.setText("");
            etFirstName.setText("");
            etLastName.setText("");
            etPhone.setText("");
            etPassword.setText("");

        } else {

            Toast.makeText(
                            getContext(),
                            "Failed To Add Admin",
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