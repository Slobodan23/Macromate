package com.example.macromate.adapteri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.macromate.R;
import com.example.macromate.model.Obrok;
import java.util.List;

public class ObrokAdapter extends RecyclerView.Adapter<ObrokAdapter.ObrokViewHolder> {
    private List<Obrok> obroci;

    public ObrokAdapter(List<Obrok> obroci) {
        this.obroci = obroci;
    }

    @NonNull
    @Override
    public ObrokViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_obrok, parent, false);
        return new ObrokViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObrokViewHolder holder, int position) {
        Obrok obrok = obroci.get(position);
        holder.bind(obrok);
    }

    @Override
    public int getItemCount() {
        return obroci.size();
    }

    public void updateData(List<Obrok> newObroci) {
        android.util.Log.d("ObrokAdapter", "UpdateData called with " + newObroci.size() + " meals");
        for (Obrok obrok : newObroci) {
            android.util.Log.d("ObrokAdapter", "Meal: " + obrok.getIme() + " - " + obrok.getKcal() + "kcal");
        }
        this.obroci = newObroci;
        notifyDataSetChanged();
        android.util.Log.d("ObrokAdapter", "notifyDataSetChanged() called, adapter now has " + getItemCount() + " items");
    }

    static class ObrokViewHolder extends RecyclerView.ViewHolder {
        private TextView obrokName, obrokKcal, obrokCarbs, obrokFats, obrokProtein;

        public ObrokViewHolder(@NonNull View itemView) {
            super(itemView);
            obrokName = itemView.findViewById(R.id.obrokName);
            obrokKcal = itemView.findViewById(R.id.obrokKcal);
            obrokCarbs = itemView.findViewById(R.id.obrokCarbs);
            obrokFats = itemView.findViewById(R.id.obrokFats);
            obrokProtein = itemView.findViewById(R.id.obrokProtein);
        }

        public void bind(Obrok obrok) {
            obrokName.setText(obrok.getIme());
            obrokKcal.setText(String.format("%.0f kcal", obrok.getKcal()));
            obrokCarbs.setText(String.format("C: %.1fg", obrok.getCarb()));
            obrokFats.setText(String.format("F: %.1fg", obrok.getFat()));
            obrokProtein.setText(String.format("P: %.1fg", obrok.getProtein()));
        }
    }
}