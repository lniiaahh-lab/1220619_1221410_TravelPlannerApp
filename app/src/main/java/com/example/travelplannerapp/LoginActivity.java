package com.example.travelplannerapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.travelplannerapp.database.DatabaseHelper;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private CheckBox cbRememberMe;
    private Button btnLogin, btnSignUp;
    private SharedPreferences sharedPreferences;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cbRememberMe = findViewById(R.id.cbRememberMe);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        
        databaseHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("TravelPlannerPrefs", MODE_PRIVATE);

        // Load saved email if Remember Me was checked
        String savedEmail = sharedPreferences.getString("savedEmail", "");
        if (!savedEmail.isEmpty()) {
            etEmail.setText(savedEmail);
            cbRememberMe.setChecked(true);
        }

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                etEmail.setError("Email is required");
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Enter a valid email");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                etPassword.setError("Password is required");
                return;
            }

            // Check against database
            if (databaseHelper.checkUser(email, password)) {
                // Save email if Remember Me is checked
                if (cbRememberMe.isChecked()) {
                    sharedPreferences.edit().putString("savedEmail", email).apply();
                } else {
                    sharedPreferences.edit().remove("savedEmail").apply();
                }

                sharedPreferences.edit().putString("currentUserEmail", email).apply();

                Toast.makeText(this,
                        "Login successful!",
                        Toast.LENGTH_SHORT).show();

                Intent intent;

                if (databaseHelper.isAdmin(email)) {

                    intent =
                            new Intent(LoginActivity.this, AdminHomeActivity.class);

                } else {

                    intent =
                            new Intent(
                                    LoginActivity.this,
                                    HomeActivity.class);
                }

                startActivity(intent);

                finish();
            } else {
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
            }
        });

        btnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
