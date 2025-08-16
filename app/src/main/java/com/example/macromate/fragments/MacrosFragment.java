package com.example.macromate.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.macromate.R;

public class MacrosFragment extends Fragment {

    private ProgressBar caloriesProgress, carbsProgress, fatsProgress, proteinProgress;
    private TextView caloriesText, carbsText, fatsText, proteinText;

    // Daily goals (you can make these configurable later)
    private final int CALORIES_GOAL = 2500;
    private final int CARBS_GOAL = 250;
    private final int FATS_GOAL = 65;
    private final int PROTEIN_GOAL = 150;

    public MacrosFragment() {
        // Required empty public constructor
    }

    public static MacrosFragment newInstance() {
        return new MacrosFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_macros, container, false);

        initializeViews(view);
        updateMacroProgress();

        return view;
    }

    private void initializeViews(View view) {
        caloriesProgress = view.findViewById(R.id.caloriesProgress);
        carbsProgress = view.findViewById(R.id.carbsProgress);
        fatsProgress = view.findViewById(R.id.fatsProgress);
        proteinProgress = view.findViewById(R.id.proteinProgress);

        caloriesText = view.findViewById(R.id.caloriesText);
        carbsText = view.findViewById(R.id.carbsText);
        fatsText = view.findViewById(R.id.fatsText);
        proteinText = view.findViewById(R.id.proteinText);

        // Set max values for progress bars
        caloriesProgress.setMax(100);
        carbsProgress.setMax(100);
        fatsProgress.setMax(100);
        proteinProgress.setMax(100);
    }

    private void updateMacroProgress() {
        // TODO: Calculate actual values from Obroks list for today
        int currentCalories = calculateDailyCalories();
        int currentCarbs = calculateDailyCarbs();
        int currentFats = calculateDailyFats();
        int currentProtein = calculateDailyProtein();

        // Update progress bars (0-100%)
        int caloriesPercent = Math.min(100, (currentCalories * 100) / CALORIES_GOAL);
        int carbsPercent = Math.min(100, (currentCarbs * 100) / CARBS_GOAL);
        int fatsPercent = Math.min(100, (currentFats * 100) / FATS_GOAL);
        int proteinPercent = Math.min(100, (currentProtein * 100) / PROTEIN_GOAL);

        caloriesProgress.setProgress(caloriesPercent);
        carbsProgress.setProgress(carbsPercent);
        fatsProgress.setProgress(fatsPercent);
        proteinProgress.setProgress(proteinPercent);

        // Update text displays
        caloriesText.setText(currentCalories + " / " + CALORIES_GOAL);
        carbsText.setText(currentCarbs + " / " + CARBS_GOAL);
        fatsText.setText(currentFats + " / " + FATS_GOAL);
        proteinText.setText(currentProtein + " / " + PROTEIN_GOAL);
    }

    // TODO: Implement these methods to calculate from your Obroks data
    private int calculateDailyCalories() {
        // Sum up calories from all Obroks for today
        return 0; // Placeholder
    }

    private int calculateDailyCarbs() {
        // Sum up carbohydrates from all Obroks for today
        return 0; // Placeholder
    }

    private int calculateDailyFats() {
        // Sum up fats from all Obroks for today
        return 0; // Placeholder
    }

    private int calculateDailyProtein() {
        // Sum up protein from all Obroks for today
        return 0; // Placeholder
    }

    // Call this method when Obroks data changes
    public void refreshMacros() {
        updateMacroProgress();
    }
}