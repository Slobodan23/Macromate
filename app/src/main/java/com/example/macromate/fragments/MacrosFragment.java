package com.example.macromate.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.macromate.R;
import com.example.macromate.baza.Database;
import com.example.macromate.model.Korisnik;
import com.example.macromate.model.Obrok;
import java.util.List;

public class MacrosFragment extends Fragment {

    private ProgressBar caloriesProgress, carbsProgress, fatsProgress, proteinProgress;
    private TextView caloriesText, carbsText, fatsText, proteinText;
    private ObrociFragment obrociFragment;
    private Database database;
    private Korisnik currentUser;


    private int caloriesGoal = 2000;
    private int carbsGoal = 250;
    private int fatsGoal = 65;
    private int proteinGoal = 150;

    public MacrosFragment() {

    }

    public static MacrosFragment newInstance() {
        return new MacrosFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_macros, container, false);

        initializeViews(view);

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

        caloriesProgress.setMax(100);
        carbsProgress.setMax(100);
        fatsProgress.setMax(100);
        proteinProgress.setMax(100);

        database = Database.getInstance(getContext());
        loadUserData();
        updateMacroProgressSafe();
    }

    private void loadUserData() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        long userId = prefs.getLong("USER_ID", -1);
        if (userId != -1) {
            currentUser = database.getKorisnikById(userId);
            if (currentUser != null) {

                Float caloriesTarget = currentUser.getCaloriesTarget();
                Float carbsTarget = currentUser.getCarbsTarget();
                Float fatTarget = currentUser.getFatTarget();
                Float proteinTarget = currentUser.getProteinTarget();

                caloriesGoal = (caloriesTarget != null) ? Math.round(caloriesTarget) : 2000;
                carbsGoal = (carbsTarget != null) ? Math.round(carbsTarget) : 250;
                fatsGoal = (fatTarget != null) ? Math.round(fatTarget) : 65;
                proteinGoal = (proteinTarget != null) ? Math.round(proteinTarget) : 150;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        loadUserData();
        updateMacroProgressSafe();
    }

    public void setObrociFragment(ObrociFragment obrociFragment) {
        this.obrociFragment = obrociFragment;

        updateMacroProgressSafe();
    }

    private void updateMacroProgressSafe() {

        if (caloriesProgress == null) return;

        int currentCalories = calculateDailyCalories();
        int currentCarbs = calculateDailyCarbs();
        int currentFats = calculateDailyFats();
        int currentProtein = calculateDailyProtein();


        int caloriesPercent = Math.min(100, (currentCalories * 100) / caloriesGoal);
        int carbsPercent = Math.min(100, (currentCarbs * 100) / carbsGoal);
        int fatsPercent = Math.min(100, (currentFats * 100) / fatsGoal);
        int proteinPercent = Math.min(100, (currentProtein * 100) / proteinGoal);

        caloriesProgress.setProgress(caloriesPercent);
        carbsProgress.setProgress(carbsPercent);
        fatsProgress.setProgress(fatsPercent);
        proteinProgress.setProgress(proteinPercent);

        caloriesText.setText(currentCalories + " / " + caloriesGoal);
        carbsText.setText(currentCarbs + " / " + carbsGoal);
        fatsText.setText(currentFats + " / " + fatsGoal);
        proteinText.setText(currentProtein + " / " + proteinGoal);
    }

    private int calculateDailyCalories() {
        if (obrociFragment == null) return 0;
        List<Obrok> obroci = obrociFragment.getAllTodaysObroci();
        if (obroci == null) return 0;
        float total = 0;
        for (Obrok obrok : obroci) {
            total += obrok.getKcal();
        }
        return Math.round(total);
    }

    private int calculateDailyCarbs() {
        if (obrociFragment == null) return 0;
        List<Obrok> obroci = obrociFragment.getAllTodaysObroci();
        if (obroci == null) return 0;
        float total = 0;
        for (Obrok obrok : obroci) {
            total += obrok.getCarb();
        }
        return Math.round(total);
    }

    private int calculateDailyFats() {
        if (obrociFragment == null) return 0;
        List<Obrok> obroci = obrociFragment.getAllTodaysObroci();
        if (obroci == null) return 0;
        float total = 0;
        for (Obrok obrok : obroci) {
            total += obrok.getFat();
        }
        return Math.round(total);
    }

    private int calculateDailyProtein() {
        if (obrociFragment == null) return 0;
        List<Obrok> obroci = obrociFragment.getAllTodaysObroci();
        if (obroci == null) return 0;
        float total = 0;
        for (Obrok obrok : obroci) {
            total += obrok.getProtein();
        }
        return Math.round(total);
    }

    public void refreshMacros() {
        updateMacroProgressSafe();
    }

    public void refreshMacroTargets() {

        loadUserData();
        updateMacroProgressSafe();
    }
}