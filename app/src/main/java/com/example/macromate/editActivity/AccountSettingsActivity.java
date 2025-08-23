package com.example.macromate.editActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.macromate.R;
import com.example.macromate.baza.Database;
import com.example.macromate.model.Korisnik;

public class AccountSettingsActivity extends AppCompatActivity {

    private EditText etCurrentPassword, etNewEmail, etNewPassword, etConfirmPassword;
    private Button btnSave, btnCancel;
    private LinearLayout buttonLayout;
    private Database database;
    private Korisnik currentUser;
    private long userId;

    private String originalEmail;
    private boolean hasChanges = false;
    private OnBackPressedCallback backPressedCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isNightMode = sharedPreferences.getBoolean("NIGHT_MODE", false);

        AppCompatDelegate.setDefaultNightMode(
                isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        loadUserData();
        setupTextWatchers();
        setupButtons();
        setupBackPressedCallback();
    }

    private void initializeViews() {
        etCurrentPassword = findViewById(R.id.etCurrentPassword);
        etNewEmail = findViewById(R.id.etNewEmail);
        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        buttonLayout = findViewById(R.id.buttonLayout);

        database = Database.getInstance(this);
    }

    private void loadUserData() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getLong("USER_ID", -1);

        if (userId == -1) {
            Toast.makeText(this, "User not found. Please login again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentUser = database.getKorisnikById(userId);
        if (currentUser == null) {
            Toast.makeText(this, "User data not found.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        originalEmail = currentUser.getEmail();
        etNewEmail.setText(originalEmail);

        updateButtonStates();
    }

    private void setupTextWatchers() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                checkForChanges();
            }
        };

        etCurrentPassword.addTextChangedListener(textWatcher);
        etNewEmail.addTextChangedListener(textWatcher);
        etNewPassword.addTextChangedListener(textWatcher);
        etConfirmPassword.addTextChangedListener(textWatcher);
    }

    private void checkForChanges() {
        String newEmail = etNewEmail.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();

        hasChanges = !newEmail.equals(originalEmail) || !newPassword.isEmpty();
        updateButtonStates();
    }

    private void updateButtonStates() {
        btnSave.setEnabled(hasChanges);
        btnCancel.setEnabled(true);
    }

    private void setupButtons() {
        btnCancel.setOnClickListener(v -> goToDashboard());
        btnSave.setOnClickListener(v -> saveChanges());
    }

    private void goToDashboard() {
        Intent intent = new Intent(this, com.example.macromate.MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void resetFields() {
        etCurrentPassword.setText("");
        etNewEmail.setText(originalEmail);
        etNewPassword.setText("");
        etConfirmPassword.setText("");
        hasChanges = false;
        updateButtonStates();
    }

    private void saveChanges() {
        String currentPassword = etCurrentPassword.getText().toString().trim();
        String newEmail = etNewEmail.getText().toString().trim();
        String newPassword = etNewPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (!validateInput(currentPassword, newEmail, newPassword, confirmPassword)) {
            return;
        }


        if (!currentPassword.equals(currentUser.getLozinka())) {
            Toast.makeText(this, "Current password is incorrect.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {

            String passwordToSave = newPassword.isEmpty() ? currentUser.getLozinka() : newPassword;

            database.editKorisnik(userId, newEmail, passwordToSave,
                    currentUser.getIme(), currentUser.getPrezime(), currentUser.getGodine(),
                    currentUser.getKilaza(), currentUser.getVisina());


            currentUser.setEmail(newEmail);
            if (!newPassword.isEmpty()) {
                currentUser.setLozinka(newPassword);
            }

            originalEmail = newEmail;
            hasChanges = false;
            updateButtonStates();

            Toast.makeText(this, "Account settings updated successfully!", Toast.LENGTH_SHORT).show();


            etCurrentPassword.setText("");
            etNewPassword.setText("");
            etConfirmPassword.setText("");

        } catch (Exception e) {
            Toast.makeText(this, "Error updating account settings. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInput(String currentPassword, String newEmail, String newPassword, String confirmPassword) {
        if (currentPassword.isEmpty()) {
            Toast.makeText(this, "Please enter your current password to make changes.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (newEmail.isEmpty()) {
            Toast.makeText(this, "Email cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
            Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // If user wants to change password
        if (!newPassword.isEmpty()) {
            if (newPassword.length() < 6) {
                Toast.makeText(this, "New password must be at least 6 characters long.", Toast.LENGTH_SHORT).show();
                return false;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "New password and confirmation do not match.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }


        if (!newEmail.equals(originalEmail)) {
            Korisnik existingUser = database.getKorisnikByEmail(newEmail);
            if (existingUser != null) {
                Toast.makeText(this, "This email is already registered.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        return true;
    }

    private void setupBackPressedCallback() {
        backPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (hasChanges) {
                    Toast.makeText(AccountSettingsActivity.this, "You have unsaved changes. Please save or cancel first.", Toast.LENGTH_SHORT).show();
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        };
        getOnBackPressedDispatcher().addCallback(this, backPressedCallback);
    }
}
