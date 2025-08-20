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

import com.example.macromate.fragments.MacrosFragment;
import com.example.macromate.fragments.ObrociFragment;

public class MainActivity extends AppCompatActivity {

    private MacrosFragment macrosFragment;
    private ObrociFragment obrociFragment;

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

        macrosFragment = (MacrosFragment) fragmentManager.findFragmentById(R.id.macrosFragment);
        obrociFragment = (ObrociFragment) fragmentManager.findFragmentById(R.id.obrociFragment);

        android.util.Log.d("MainActivity", "MacrosFragment found: " + (macrosFragment != null));
        android.util.Log.d("MainActivity", "ObrociFragment found: " + (obrociFragment != null));

        fragmentManager.executePendingTransactions();
        android.util.Log.d("MainActivity", "Pending transactions executed");

        if (macrosFragment != null && obrociFragment != null) {
            macrosFragment.setObrociFragment(obrociFragment);
            obrociFragment.setMacrosFragment(macrosFragment);
            android.util.Log.d("MainActivity", "Fragments linked together successfully");
        } else {
            android.util.Log.e("MainActivity", "Failed to link fragments - macrosFragment: " + macrosFragment + ", obrociFragment: " + obrociFragment);
        }
    }
}