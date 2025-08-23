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
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.macromate.R;
import com.example.macromate.baza.Database;
import com.example.macromate.model.Korisnik;

public class ProfilActivity extends AppCompatActivity {

    private EditText etIme, etPrezime, etGodine, etKilaza, etVisina;
    private Button btnSacuvaj, btnOtkazi, btnEditMacros, btnAccountSettings;
    private LinearLayout buttonLayout;
    private Database database;
    private Korisnik currentUser;
    private long userId;

    private String originalIme, originalPrezime;
    private int originalGodine, originalVisina;
    private float originalKilaza;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isNightMode = sharedPreferences.getBoolean("NIGHT_MODE", false);

        AppCompatDelegate.setDefaultNightMode(
                isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        loadUserData();
        setupTextWatchers();
        setupButtons();
    }

    private void initializeViews() {
        etIme = findViewById(R.id.etIme);
        etPrezime = findViewById(R.id.etPrezime);
        etGodine = findViewById(R.id.etGodine);
        etKilaza = findViewById(R.id.etKilaza);
        etVisina = findViewById(R.id.etVisina);
        btnSacuvaj = findViewById(R.id.btnSacuvaj);
        btnOtkazi = findViewById(R.id.btnOtkazi);
        btnEditMacros = findViewById(R.id.btnEditMacros);
        btnAccountSettings = findViewById(R.id.btnAccountSettings);
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

        originalIme = currentUser.getIme();
        originalPrezime = currentUser.getPrezime();
        originalGodine = currentUser.getGodine();
        originalKilaza = currentUser.getKilaza();
        originalVisina = currentUser.getVisina();

        etIme.setText(originalIme);
        etPrezime.setText(originalPrezime);
        etGodine.setText(String.valueOf(originalGodine));
        etKilaza.setText(String.valueOf(originalKilaza));
        etVisina.setText(String.valueOf(originalVisina));

        buttonLayout.setVisibility(View.VISIBLE);
        btnSacuvaj.setEnabled(true);
        btnOtkazi.setEnabled(true);
    }

    private void setupTextWatchers() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        etIme.addTextChangedListener(textWatcher);
        etPrezime.addTextChangedListener(textWatcher);
        etGodine.addTextChangedListener(textWatcher);
        etKilaza.addTextChangedListener(textWatcher);
        etVisina.addTextChangedListener(textWatcher);
    }

    private void setupButtons() {
        btnOtkazi.setOnClickListener(v -> resetFields());
        btnSacuvaj.setOnClickListener(v -> saveChanges());
        btnEditMacros.setOnClickListener(v -> openEditMacrosActivity());
        btnAccountSettings.setOnClickListener(v -> openAccountSettingsActivity());
    }

    private void openEditMacrosActivity() {
        Intent intent = new Intent(this, EditMacrosActivity.class);
        startActivity(intent);
    }

    private void openAccountSettingsActivity() {
        Intent intent = new Intent(this, AccountSettingsActivity.class);
        startActivity(intent);
    }

    private void resetFields() {
        etIme.setText(originalIme);
        etPrezime.setText(originalPrezime);
        etGodine.setText(String.valueOf(originalGodine));
        etKilaza.setText(String.valueOf(originalKilaza));
        etVisina.setText(String.valueOf(originalVisina));

        btnSacuvaj.setEnabled(true);
        btnOtkazi.setEnabled(true);
    }

    private void saveChanges() {
        String ime = etIme.getText().toString().trim();
        String prezime = etPrezime.getText().toString().trim();
        String godineStr = etGodine.getText().toString().trim();
        String kilazaStr = etKilaza.getText().toString().trim();
        String visinaStr = etVisina.getText().toString().trim();

        if (!validateInput(ime, prezime, godineStr, kilazaStr, visinaStr)) {
            return;
        }

        try {
            int godine = Integer.parseInt(godineStr);
            float kilaza = Float.parseFloat(kilazaStr);
            int visina = Integer.parseInt(visinaStr);

            database.editKorisnik(userId, currentUser.getEmail(), currentUser.getLozinka(),
                    ime, prezime, godine, kilaza, visina);

            originalIme = ime;
            originalPrezime = prezime;
            originalGodine = godine;
            originalKilaza = kilaza;
            originalVisina = visina;

            currentUser.setIme(ime);
            currentUser.setPrezime(prezime);
            currentUser.setGodine(godine);
            currentUser.setKilaza(kilaza);
            currentUser.setVisina(visina);

            Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers for age, weight, and height.", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInput(String ime, String prezime, String godine, String kilaza, String visina) {
        if (ime.isEmpty() || prezime.isEmpty() || godine.isEmpty() || kilaza.isEmpty() || visina.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
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
}