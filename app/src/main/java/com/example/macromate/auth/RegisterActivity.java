package com.example.macromate.auth;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.macromate.MainActivity;
import com.example.macromate.R;
import com.example.macromate.baza.Database;

public class RegisterActivity extends AppCompatActivity {

    private EditText etEmail, etPassword, etIme, etPrezime, etGodine, etKilaza, etVisina;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isNightMode = sharedPreferences.getBoolean("NIGHT_MODE", false);

        AppCompatDelegate.setDefaultNightMode(
                isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupThemeToggle();
        setupBackButton();
        setupRegisterButton();
    }

    private void initializeViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etIme = findViewById(R.id.etIme);
        etPrezime = findViewById(R.id.etPrezime);
        etGodine = findViewById(R.id.etGodine);
        etKilaza = findViewById(R.id.etKilaza);
        etVisina = findViewById(R.id.etVisina);

        database = Database.getInstance(this);
    }

    private void setupRegisterButton() {
        Button registerButton = findViewById(R.id.btRegister);
        registerButton.setOnClickListener(v -> {
            handleRegistration();
        });
    }

    private void handleRegistration() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String ime = etIme.getText().toString().trim();
        String prezime = etPrezime.getText().toString().trim();
        String godineStr = etGodine.getText().toString().trim();
        String kilazaStr = etKilaza.getText().toString().trim();
        String visinaStr = etVisina.getText().toString().trim();


        if (!validateInput(email, password, ime, prezime, godineStr, kilazaStr, visinaStr)) {
            return;
        }


        if (database.emailExists(email)) {
            Toast.makeText(this, "Email already exists. Please use a different email.", Toast.LENGTH_SHORT).show();
            return;
        }


        try {
            int godine = Integer.parseInt(godineStr);
            float kilaza = Float.parseFloat(kilazaStr);
            int visina = Integer.parseInt(visinaStr);


            long userId = database.addKorisnik(email, password, ime, prezime, godine, kilaza, visina);

            if (userId != -1) {
                Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();


                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putLong("USER_ID", userId);
                editor.putString("USER_EMAIL", email);
                editor.putBoolean("IS_LOGGED_IN", true);
                editor.apply();


                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numeric values for age, weight, and height.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInput(String email, String password, String ime, String prezime,
                                  String godine, String kilaza, String visina) {

        if (email.isEmpty() || password.isEmpty() || ime.isEmpty() || prezime.isEmpty() ||
                godine.isEmpty() || kilaza.isEmpty() || visina.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long.", Toast.LENGTH_SHORT).show();
            return false;
        }


        try {
            int godineInt = Integer.parseInt(godine);
            float kilazaFloat = Float.parseFloat(kilaza);
            int visinaInt = Integer.parseInt(visina);

            if (godineInt < 13 || godineInt > 120) {
                Toast.makeText(this, "Age must be between 13 and 120.", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (kilazaFloat < 30 || kilazaFloat > 300) {
                Toast.makeText(this, "Weight must be between 30 and 300 kg.", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (visinaInt < 100 || visinaInt > 250) {
                Toast.makeText(this, "Height must be between 100 and 250 cm.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers for age, weight, and height.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void setupThemeToggle() {
        ImageButton themeToggle = findViewById(R.id.themeToggle);

        if (themeToggle != null) {
            themeToggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(RegisterActivity.this);
                    boolean currentMode = sharedPreferences.getBoolean("NIGHT_MODE", false);
                    boolean newMode = !currentMode;


                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("NIGHT_MODE", newMode);
                    editor.apply();


                    AppCompatDelegate.setDefaultNightMode(
                            newMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
                    );
                }
            });
        }
    }

    private void setupBackButton() {
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> onBackPressed());
    }
}