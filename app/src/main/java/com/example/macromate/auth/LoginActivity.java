package com.example.macromate.auth;
import java.util.List;
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
import com.example.macromate.model.Korisnik;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
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
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        logAllUsers();
        setupThemeToggle();
        setupLoginButton();
    }

    private void initializeViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        database = Database.getInstance(this);
    }

    private void setupLoginButton() {
        Button loginButton = findViewById(R.id.btLogin);
        loginButton.setOnClickListener(v -> {
            handleLogin();
        });
    }

    private void handleLogin() {

        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();


        if (!validateInput(email, password)) {
            return;
        }


        Korisnik user = database.getKorisnikByEmailAndPassword(email, password);

        if (user != null) {

            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();


            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putLong("USER_ID", user.getId());
            editor.putString("USER_EMAIL", user.getEmail());
            editor.putBoolean("IS_LOGGED_IN", true);
            editor.apply();


            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {

            etEmail.setText("");
            etPassword.setText("");
            etEmail.requestFocus();
            Toast.makeText(this, "Invalid email or password. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInput(String email, String password) {

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return false;
        }


        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
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
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
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
    private void logAllUsers() {
        List<Korisnik> allUsers = database.getAllKorisnici();

        System.out.println("=== ALL USERS IN DATABASE ===");
        System.out.println("Total users: " + allUsers.size());

        if (allUsers.isEmpty()) {
            System.out.println("No users found in database.");
        } else {
            for (int i = 0; i < allUsers.size(); i++) {
                Korisnik user = allUsers.get(i);
                System.out.println("User " + (i + 1) + ":");
                System.out.println("  ID: " + user.getId());
                System.out.println("  Email: " + user.getEmail());
                System.out.println("  Password: " + user.getLozinka());
                System.out.println("  Name: " + user.getIme() + " " + user.getPrezime());
                System.out.println("  Age: " + user.getGodine());
                System.out.println("  Weight: " + user.getKilaza() + " kg");
                System.out.println("  Height: " + user.getVisina() + " cm");
                System.out.println("  ---");
            }
        }
        System.out.println("=== END OF USER LIST ===");
    }
}