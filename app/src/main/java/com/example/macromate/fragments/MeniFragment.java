package com.example.macromate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.macromate.MainActivity;
import com.example.macromate.R;
import com.example.macromate.editActivity.ProfilActivity;

public class MeniFragment extends Fragment {

    private LinearLayout btnDashboard, btnProfile, btnAdd;

    public MeniFragment() {
        // Required empty public constructor
    }

    public static MeniFragment newInstance() {
        return new MeniFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meni, container, false);

        initializeViews(view);
        setupMenuClicks();

        return view;
    }

    private void initializeViews(View view) {
        btnDashboard = view.findViewById(R.id.btnDashboard);
        btnProfile = view.findViewById(R.id.btnProfile);
        btnAdd = view.findViewById(R.id.btnAdd);
    }

    private void setupMenuClicks() {
        btnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to MainActivity (Dashboard)
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Add clicked", Toast.LENGTH_SHORT).show();
                // TODO: Open add meal dialog
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ProfilActivity
                Intent intent = new Intent(getActivity(), ProfilActivity.class);
                startActivity(intent);
            }
        });
    }
}