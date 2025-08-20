package com.example.macromate.editActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.macromate.R;
import com.example.macromate.baza.Database;
import com.google.android.material.textfield.TextInputEditText;

public class CreateObrokActivity extends AppCompatActivity {

    private TextInputEditText editTextMealName, editTextCalories, editTextProtein, editTextCarbs, editTextFat;
    private Button buttonSave, buttonCancel;
    private Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_obrok);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        database = Database.getInstance(this);
        setupClickListeners();
    }

    private void initializeViews() {
        editTextMealName = findViewById(R.id.editTextMealName);
        editTextCalories = findViewById(R.id.editTextCalories);
        editTextProtein = findViewById(R.id.editTextProtein);
        editTextCarbs = findViewById(R.id.editTextCarbs);
        editTextFat = findViewById(R.id.editTextFat);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);
    }

    private void setupClickListeners() {
        buttonSave.setOnClickListener(v -> saveMeal());
        buttonCancel.setOnClickListener(v -> finish());
    }

    private void saveMeal() {
        String mealName = editTextMealName.getText().toString().trim();
        String caloriesStr = editTextCalories.getText().toString().trim();
        String proteinStr = editTextProtein.getText().toString().trim();
        String carbsStr = editTextCarbs.getText().toString().trim();
        String fatStr = editTextFat.getText().toString().trim();

        if (TextUtils.isEmpty(mealName)) {
            editTextMealName.setError("Meal name is required");
            return;
        }

        if (TextUtils.isEmpty(caloriesStr)) {
            editTextCalories.setError("Calories are required");
            return;
        }

        if (TextUtils.isEmpty(proteinStr)) {
            editTextProtein.setError("Protein is required");
            return;
        }

        if (TextUtils.isEmpty(carbsStr)) {
            editTextCarbs.setError("Carbohydrates are required");
            return;
        }

        if (TextUtils.isEmpty(fatStr)) {
            editTextFat.setError("Fat is required");
            return;
        }

        try {
            float calories = Float.parseFloat(caloriesStr);
            float protein = Float.parseFloat(proteinStr);
            float carbs = Float.parseFloat(carbsStr);
            float fat = Float.parseFloat(fatStr);

            if (calories < 0 || protein < 0 || carbs < 0 || fat < 0) {
                Toast.makeText(this, "All values must be positive", Toast.LENGTH_SHORT).show();
                return;
            }

            database.addObrok(mealName, calories, fat, carbs, protein);
            Toast.makeText(this, "Meal saved successfully!", Toast.LENGTH_SHORT).show();
            finish();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show();
        }
    }
}