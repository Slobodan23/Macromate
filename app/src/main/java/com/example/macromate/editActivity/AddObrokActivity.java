package com.example.macromate.editActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.macromate.R;
import com.example.macromate.baza.Database;
import com.example.macromate.model.Obrok;
import com.example.macromate.model.ObrokSaKolicinom;
import com.example.macromate.model.ObrociDan;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AddObrokActivity extends AppCompatActivity {

    private Spinner spinnerMeals, spinnerMealCategory;
    private TextInputEditText editTextQuantity;
    private TextView textViewCaloriesPreview, textViewProteinPreview, textViewCarbsPreview, textViewFatPreview;
    private Button buttonAddMeal, buttonCancel, buttonCreateMeal;
    private Database database;
    private List<Obrok> availableMeals;
    private Obrok selectedMeal;
    private String[] mealCategories = {"Breakfast", "Lunch", "Dinner", "Snacks"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_obrok);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        database = Database.getInstance(this);
        setupSpinners();
        setupClickListeners();
        setupQuantityListener();
        loadMeals();
    }

    private void initializeViews() {
        spinnerMeals = findViewById(R.id.spinnerMeals);
        spinnerMealCategory = findViewById(R.id.spinnerMealCategory);
        editTextQuantity = findViewById(R.id.editTextQuantity);
        textViewCaloriesPreview = findViewById(R.id.textViewCaloriesPreview);
        textViewProteinPreview = findViewById(R.id.textViewProteinPreview);
        textViewCarbsPreview = findViewById(R.id.textViewCarbsPreview);
        textViewFatPreview = findViewById(R.id.textViewFatPreview);
        buttonAddMeal = findViewById(R.id.buttonAddMeal);
        buttonCancel = findViewById(R.id.buttonCancel);
        buttonCreateMeal = findViewById(R.id.buttonCreateMeal);
    }

    private void setupSpinners() {
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mealCategories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMealCategory.setAdapter(categoryAdapter);

        spinnerMeals.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0 && availableMeals != null && position <= availableMeals.size()) {
                    selectedMeal = availableMeals.get(position - 1);
                    updateNutritionalPreview();
                } else {
                    selectedMeal = null;
                    updateNutritionalPreview();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedMeal = null;
                updateNutritionalPreview();
            }
        });
    }

    private void setupClickListeners() {
        buttonAddMeal.setOnClickListener(v -> addMealToDay());
        buttonCancel.setOnClickListener(v -> finish());
        buttonCreateMeal.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateObrokActivity.class);
            startActivity(intent);
        });
    }

    private void setupQuantityListener() {
        editTextQuantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateNutritionalPreview();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadMeals() {
        availableMeals = database.getAllObroci();

        List<String> mealNames = new ArrayList<>();
        mealNames.add("Select a meal...");

        for (Obrok obrok : availableMeals) {
            mealNames.add(obrok.getIme());
        }

        ArrayAdapter<String> mealsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mealNames);
        mealsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMeals.setAdapter(mealsAdapter);
    }

    private void updateNutritionalPreview() {
        if (selectedMeal == null) {
            textViewCaloriesPreview.setText("Calories: 0 kcal");
            textViewProteinPreview.setText("Protein: 0 g");
            textViewCarbsPreview.setText("Carbohydrates: 0 g");
            textViewFatPreview.setText("Fat: 0 g");
            return;
        }

        float quantity = 1.0f;
        try {
            String quantityStr = editTextQuantity.getText().toString().trim();
            if (!quantityStr.isEmpty()) {
                quantity = Float.parseFloat(quantityStr);
            }
        } catch (NumberFormatException e) {
            quantity = 1.0f;
        }

        ObrokSaKolicinom obrokSaKolicinom = new ObrokSaKolicinom(selectedMeal, quantity);

        textViewCaloriesPreview.setText(String.format("Calories: %.1f kcal", obrokSaKolicinom.getCalculatedKcal()));
        textViewProteinPreview.setText(String.format("Protein: %.1f g", obrokSaKolicinom.getCalculatedProtein()));
        textViewCarbsPreview.setText(String.format("Carbohydrates: %.1f g", obrokSaKolicinom.getCalculatedCarb()));
        textViewFatPreview.setText(String.format("Fat: %.1f g", obrokSaKolicinom.getCalculatedFat()));
    }

    private void addMealToDay() {
        if (selectedMeal == null) {
            Toast.makeText(this, "Please select a meal", Toast.LENGTH_SHORT).show();
            return;
        }

        float quantity;
        try {
            String quantityStr = editTextQuantity.getText().toString().trim();
            if (quantityStr.isEmpty()) {
                quantity = 1.0f;
            } else {
                quantity = Float.parseFloat(quantityStr);
            }

            if (quantity <= 0) {
                Toast.makeText(this, "Quantity must be greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Please enter a valid quantity", Toast.LENGTH_SHORT).show();
            return;
        }

        Obrok scaledObrok = new Obrok(
                null,
                selectedMeal.getIme() + " (" + (quantity * 100) + "g)",
                selectedMeal.getKcal() * quantity,
                selectedMeal.getFat() * quantity,
                selectedMeal.getCarb() * quantity,
                selectedMeal.getProtein() * quantity
        );

        String selectedCategory = mealCategories[spinnerMealCategory.getSelectedItemPosition()];

        addToTodaysMeals(scaledObrok, selectedCategory);

        Toast.makeText(this, "Meal added to " + selectedCategory.toLowerCase() + "!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void addToTodaysMeals(Obrok obrok, String category) {
        long obrokId = database.addObrok(obrok.getIme(), obrok.getKcal(), obrok.getFat(), obrok.getCarb(), obrok.getProtein());
        obrok.setId(obrokId);

        Date targetDate = new Date();
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("SELECTED_DATE")) {
            long dateMillis = intent.getLongExtra("SELECTED_DATE", System.currentTimeMillis());
            targetDate = new Date(dateMillis);
        }

        ObrociDan obrociDan = database.getObrociDanByDate(targetDate);

        if (obrociDan == null) {
            List<Obrok> dorucak = new ArrayList<>();
            List<Obrok> rucak = new ArrayList<>();
            List<Obrok> vecera = new ArrayList<>();
            List<Obrok> uzina = new ArrayList<>();

            switch (category) {
                case "Breakfast":
                    dorucak.add(obrok);
                    break;
                case "Lunch":
                    rucak.add(obrok);
                    break;
                case "Dinner":
                    vecera.add(obrok);
                    break;
                case "Snacks":
                    uzina.add(obrok);
                    break;
            }

            database.addObrociDan(dorucak, rucak, vecera, uzina, targetDate);
        } else {
            List<Obrok> dorucak = obrociDan.getDorucak() != null ? new ArrayList<>(obrociDan.getDorucak()) : new ArrayList<>();
            List<Obrok> rucak = obrociDan.getRucak() != null ? new ArrayList<>(obrociDan.getRucak()) : new ArrayList<>();
            List<Obrok> vecera = obrociDan.getVecera() != null ? new ArrayList<>(obrociDan.getVecera()) : new ArrayList<>();
            List<Obrok> uzina = obrociDan.getUzina() != null ? new ArrayList<>(obrociDan.getUzina()) : new ArrayList<>();

            switch (category) {
                case "Breakfast":
                    dorucak.add(obrok);
                    break;
                case "Lunch":
                    rucak.add(obrok);
                    break;
                case "Dinner":
                    vecera.add(obrok);
                    break;
                case "Snacks":
                    uzina.add(obrok);
                    break;
            }

            database.editObrociDan(obrociDan.getId(), dorucak, rucak, vecera, uzina, targetDate);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMeals();
    }
}