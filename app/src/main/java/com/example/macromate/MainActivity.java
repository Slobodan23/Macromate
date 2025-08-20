package com.example.macromate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import com.example.macromate.fragments.DatePickerFragment;
import com.example.macromate.fragments.MacrosFragment;
import com.example.macromate.fragments.ObrociFragment;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private MacrosFragment macrosFragment;
    private ObrociFragment obrociFragment;
    private DatePickerFragment datePickerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isNightMode = sharedPreferences.getBoolean("NIGHT_MODE", false);

        AppCompatDelegate.setDefaultNightMode(
                isNightMode ? AppCompatDelegate.MODE_NIGHT_YES : AppCompatDelegate.MODE_NIGHT_NO
        );

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupFragments(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (obrociFragment != null) {
            obrociFragment.refreshObroci();
        }
    }

    private void setupFragments(Bundle savedInstanceState) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        datePickerFragment = (DatePickerFragment) fragmentManager.findFragmentById(R.id.datePickerFragment);
        macrosFragment = (MacrosFragment) fragmentManager.findFragmentById(R.id.macrosFragment);
        obrociFragment = (ObrociFragment) fragmentManager.findFragmentById(R.id.obrociFragment);

        android.util.Log.d("MainActivity", "DatePickerFragment found: " + (datePickerFragment != null));
        android.util.Log.d("MainActivity", "MacrosFragment found: " + (macrosFragment != null));
        android.util.Log.d("MainActivity", "ObrociFragment found: " + (obrociFragment != null));

        fragmentManager.executePendingTransactions();
        android.util.Log.d("MainActivity", "Pending transactions executed");

        if (datePickerFragment != null && macrosFragment != null && obrociFragment != null) {
            macrosFragment.setObrociFragment(obrociFragment);
            obrociFragment.setMacrosFragment(macrosFragment);

            datePickerFragment.setOnDateChangeListener(this::onDateChanged);

            Date initialDate = datePickerFragment.getSelectedDate();
            obrociFragment.setSelectedDate(initialDate);

            android.util.Log.d("MainActivity", "All fragments linked together successfully");
        } else {
            android.util.Log.e("MainActivity", "Failed to link fragments");
        }
    }

    private void onDateChanged(Date selectedDate) {
        android.util.Log.d("MainActivity", "Date changed to: " + selectedDate);

        if (obrociFragment != null) {
            obrociFragment.setSelectedDate(selectedDate);
            obrociFragment.refreshObroci();
        }

        if (macrosFragment != null) {
            macrosFragment.refreshMacros();
        }
    }
}