package com.example.macromate.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.example.macromate.MainActivity;
import com.example.macromate.R;
import com.example.macromate.editActivity.AddObrokActivity;
import com.example.macromate.editActivity.ProfilActivity;

public class MeniFragment extends Fragment {

    private LinearLayout btnDashboard, btnProfile;
    private View fabAdd;

    public MeniFragment() {
    }

    public static MeniFragment newInstance() {
        return new MeniFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meni, container, false);

        initializeViews(view);
        setupMenuClicks(view);

        return view;
    }

    private void initializeViews(View view) {
        btnDashboard = view.findViewById(R.id.btnDashboard);
        btnProfile = view.findViewById(R.id.btnProfile);
        fabAdd = view.findViewById(R.id.fabAdd);
    }

    private void setupMenuClicks(View view) {
        btnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        if (fabAdd != null) {
            fabAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), AddObrokActivity.class);
                    startActivity(intent);
                }
            });
        }

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfilActivity.class);
                startActivity(intent);
            }
        });
    }
}