package com.example.macromate.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.macromate.R;
import com.example.macromate.adapteri.ObrokAdapter;
import com.example.macromate.baza.Database;
import com.example.macromate.model.Obrok;
import com.example.macromate.model.ObrociDan;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ObrociFragment extends Fragment {

    private RecyclerView dorucakRecyclerView, rucakRecyclerView, veceraRecyclerView, uzinaRecyclerView;
    private ObrokAdapter dorucakAdapter, rucakAdapter, veceraAdapter, uzinaAdapter;
    private TextView emptyDorucakText, emptyRucakText, emptyVeceraText, emptyUzinaText;
    private Database database;
    private MacrosFragment macrosFragment;

    public ObrociFragment() {

    }

    public static ObrociFragment newInstance() {
        return new ObrociFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        android.util.Log.d("ObrociFragment", "onCreateView called");
        View view = inflater.inflate(R.layout.fragment_obroci, container, false);
        android.util.Log.d("ObrociFragment", "Layout inflated successfully");

        database = Database.getInstance(getContext());
        android.util.Log.d("ObrociFragment", "Database instance: " + (database != null));

        initializeViews(view);
        setupRecyclerViews();
        loadTodaysObroci();

        android.util.Log.d("ObrociFragment", "onCreateView completed");
        return view;
    }

    private void initializeViews(View view) {
        dorucakRecyclerView = view.findViewById(R.id.dorucakRecyclerView);
        rucakRecyclerView = view.findViewById(R.id.rucakRecyclerView);
        veceraRecyclerView = view.findViewById(R.id.veceraRecyclerView);
        uzinaRecyclerView = view.findViewById(R.id.uzinaRecyclerView);

        emptyDorucakText = view.findViewById(R.id.emptyDorucakText);
        emptyRucakText = view.findViewById(R.id.emptyRucakText);
        emptyVeceraText = view.findViewById(R.id.emptyVeceraText);
        emptyUzinaText = view.findViewById(R.id.emptyUzinaText);
    }

    private void setupRecyclerViews() {
        dorucakAdapter = new ObrokAdapter(new ArrayList<>());
        rucakAdapter = new ObrokAdapter(new ArrayList<>());
        veceraAdapter = new ObrokAdapter(new ArrayList<>());
        uzinaAdapter = new ObrokAdapter(new ArrayList<>());

        dorucakRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        dorucakRecyclerView.setAdapter(dorucakAdapter);

        rucakRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        rucakRecyclerView.setAdapter(rucakAdapter);

        veceraRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        veceraRecyclerView.setAdapter(veceraAdapter);

        uzinaRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        uzinaRecyclerView.setAdapter(uzinaAdapter);
    }

    private void loadTodaysObroci() {
        if (database == null) return;

        Date today = new Date();
        ObrociDan obrociDan = database.getObrociDanByDate(today);

        android.util.Log.d("ObrociFragment", "Loading today's meals for date: " + today);

        if (obrociDan != null) {
            android.util.Log.d("ObrociFragment", "Found ObrociDan with ID: " + obrociDan.getId());

            List<Obrok> dorucak = obrociDan.getDorucak() != null ? obrociDan.getDorucak() : new ArrayList<>();
            List<Obrok> rucak = obrociDan.getRucak() != null ? obrociDan.getRucak() : new ArrayList<>();
            List<Obrok> vecera = obrociDan.getVecera() != null ? obrociDan.getVecera() : new ArrayList<>();
            List<Obrok> uzina = obrociDan.getUzina() != null ? obrociDan.getUzina() : new ArrayList<>();

            android.util.Log.d("ObrociFragment", "Dorucak meals: " + dorucak.size());
            android.util.Log.d("ObrociFragment", "Rucak meals: " + rucak.size());
            android.util.Log.d("ObrociFragment", "Vecera meals: " + vecera.size());
            android.util.Log.d("ObrociFragment", "Uzina meals: " + uzina.size());

            dorucakAdapter.updateData(dorucak);
            rucakAdapter.updateData(rucak);
            veceraAdapter.updateData(vecera);
            uzinaAdapter.updateData(uzina);

            updateEmptyTextVisibility(dorucak, rucak, vecera, uzina);
        } else {
            android.util.Log.d("ObrociFragment", "No ObrociDan found for today");
            dorucakAdapter.updateData(new ArrayList<>());
            rucakAdapter.updateData(new ArrayList<>());
            veceraAdapter.updateData(new ArrayList<>());
            uzinaAdapter.updateData(new ArrayList<>());

            updateEmptyTextVisibility(new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        }

        updateMacrosFragment();
    }

    private void updateEmptyTextVisibility(List<Obrok> dorucak, List<Obrok> rucak, List<Obrok> vecera, List<Obrok> uzina) {
        emptyDorucakText.setVisibility(dorucak.isEmpty() ? View.VISIBLE : View.GONE);
        emptyRucakText.setVisibility(rucak.isEmpty() ? View.VISIBLE : View.GONE);
        emptyVeceraText.setVisibility(vecera.isEmpty() ? View.VISIBLE : View.GONE);
        emptyUzinaText.setVisibility(uzina.isEmpty() ? View.VISIBLE : View.GONE);
    }

    public void setMacrosFragment(MacrosFragment macrosFragment) {
        this.macrosFragment = macrosFragment;
    }

    private void updateMacrosFragment() {
        if (macrosFragment != null) {
            macrosFragment.refreshMacros();
        }
    }

    public List<Obrok> getAllTodaysObroci() {
        if (database == null) return new ArrayList<>();

        Date today = new Date();
        ObrociDan obrociDan = database.getObrociDanByDate(today);
        List<Obrok> allObroci = new ArrayList<>();

        if (obrociDan != null) {
            if (obrociDan.getDorucak() != null) allObroci.addAll(obrociDan.getDorucak());
            if (obrociDan.getRucak() != null) allObroci.addAll(obrociDan.getRucak());
            if (obrociDan.getVecera() != null) allObroci.addAll(obrociDan.getVecera());
            if (obrociDan.getUzina() != null) allObroci.addAll(obrociDan.getUzina());
        }

        return allObroci;
    }

    public void refreshObroci() {
        loadTodaysObroci();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadTodaysObroci();
    }
}