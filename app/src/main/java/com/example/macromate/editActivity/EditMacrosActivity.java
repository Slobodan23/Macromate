package com.example.macromate.editActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
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
import com.google.android.material.textfield.TextInputEditText;

public class EditMacrosActivity extends AppCompatActivity {

    private TextInputEditText etCaloriesTarget, etProteinTarget, etCarbsTarget, etFatTarget;
    private Button btnSave, btnCancel, btnCuttingPreset, btnBulkingPreset;
    private Database database;
    private Korisnik currentUser;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isNightMode = sharedPreferences.getBoolean("NIGHT_MODE", false);

        AppCompatDelegate.setDefaultNightMode(
                isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_macros);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        loadUserData();
        setupButtons();
    }

    private void initializeViews() {
        etCaloriesTarget = findViewById(R.id.etCaloriesTarget);
        etProteinTarget = findViewById(R.id.etProteinTarget);
        etCarbsTarget = findViewById(R.id.etCarbsTarget);
        etFatTarget = findViewById(R.id.etFatTarget);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        btnCuttingPreset = findViewById(R.id.btnCuttingPreset);
        btnBulkingPreset = findViewById(R.id.btnBulkingPreset);

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

        // Load current macro targets or set defaults
        Float caloriesTarget = currentUser.getCaloriesTarget();
        Float proteinTarget = currentUser.getProteinTarget();
        Float carbsTarget = currentUser.getCarbsTarget();
        Float fatTarget = currentUser.getFatTarget();

        // Set default values if null
        if (caloriesTarget == null) caloriesTarget = 2000.0f;
        if (proteinTarget == null) proteinTarget = 150.0f;
        if (carbsTarget == null) carbsTarget = 250.0f;
        if (fatTarget == null) fatTarget = 65.0f;

        etCaloriesTarget.setText(String.valueOf(caloriesTarget.intValue()));
        etProteinTarget.setText(String.valueOf(proteinTarget.intValue()));
        etCarbsTarget.setText(String.valueOf(carbsTarget.intValue()));
        etFatTarget.setText(String.valueOf(fatTarget.intValue()));
    }

    private void setupButtons() {
        btnCancel.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> saveMacroTargets());
        btnCuttingPreset.setOnClickListener(v -> applyCuttingPreset());
        btnBulkingPreset.setOnClickListener(v -> applyBulkingPreset());
    }

    private void saveMacroTargets() {
        try {
            String caloriesStr = etCaloriesTarget.getText().toString().trim();
            String proteinStr = etProteinTarget.getText().toString().trim();
            String carbsStr = etCarbsTarget.getText().toString().trim();
            String fatStr = etFatTarget.getText().toString().trim();

            if (caloriesStr.isEmpty() || proteinStr.isEmpty() || carbsStr.isEmpty() || fatStr.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            float calories = Float.parseFloat(caloriesStr);
            float protein = Float.parseFloat(proteinStr);
            float carbs = Float.parseFloat(carbsStr);
            float fat = Float.parseFloat(fatStr);

            if (calories <= 0 || protein < 0 || carbs < 0 || fat < 0) {
                Toast.makeText(this, "Please enter valid positive values", Toast.LENGTH_SHORT).show();
                return;
            }


            currentUser.setCaloriesTarget(calories);
            currentUser.setProteinTarget(protein);
            currentUser.setCarbsTarget(carbs);
            currentUser.setFatTarget(fat);


            database.editKorisnikWithMacros(userId, currentUser.getEmail(), currentUser.getLozinka(),
                    currentUser.getIme(), currentUser.getPrezime(), currentUser.getGodine(),
                    currentUser.getKilaza(), currentUser.getVisina(), calories, protein, carbs, fat);

            Toast.makeText(this, "Macro targets saved successfully!", Toast.LENGTH_SHORT).show();
            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }

    private void applyCuttingPreset() {

        float baseCalories = 1800.0f;
        if (currentUser.getKilaza() != null) {
            baseCalories = currentUser.getKilaza() * 22; // 22 kcal per kg for cutting
        }

        etCaloriesTarget.setText(String.valueOf((int) baseCalories));
        etProteinTarget.setText(String.valueOf((int) (currentUser.getKilaza() != null ? currentUser.getKilaza() * 2.2f : 130))); // 2.2g per kg
        etCarbsTarget.setText(String.valueOf((int) (baseCalories * 0.35f / 4))); // 35% of calories from carbs
        etFatTarget.setText(String.valueOf((int) (baseCalories * 0.25f / 9))); // 25% of calories from fat

        Toast.makeText(this, "Cutting preset applied!", Toast.LENGTH_SHORT).show();
    }

    private void applyBulkingPreset() {

        float baseCalories = 2500.0f;
        if (currentUser.getKilaza() != null) {
            baseCalories = currentUser.getKilaza() * 35; // 35 kcal per kg for bulking
        }

        etCaloriesTarget.setText(String.valueOf((int) baseCalories));
        etProteinTarget.setText(String.valueOf((int) (currentUser.getKilaza() != null ? currentUser.getKilaza() * 2.0f : 160))); // 2.0g per kg
        etCarbsTarget.setText(String.valueOf((int) (baseCalories * 0.45f / 4))); // 45% of calories from carbs
        etFatTarget.setText(String.valueOf((int) (baseCalories * 0.30f / 9))); // 30% of calories from fat

        Toast.makeText(this, "Bulking preset applied!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        loadUserData();
    }
}
