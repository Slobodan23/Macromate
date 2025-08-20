package com.example.macromate.fragments;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.macromate.R;

public class HeaderFragment extends Fragment {

    public HeaderFragment() {
    }

    public static HeaderFragment newInstance() {
        return new HeaderFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_header, container, false);

        setupThemeToggle(view);

        return view;
    }

    private void setupThemeToggle(View view) {
        ImageButton themeToggle = view.findViewById(R.id.themeToggle);

        if (themeToggle != null) {
            themeToggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
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
}