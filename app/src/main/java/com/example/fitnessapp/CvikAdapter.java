package com.example.fitnessapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CvikAdapter extends RecyclerView.Adapter<CvikAdapter.CvikViewHolder> {

    private final ArrayList<Cvik> seznamCviku;
    private final Context context;

    public CvikAdapter(ArrayList<Cvik> seznamCviku, Context context) {
        this.seznamCviku = seznamCviku;
        this.context = context;
    }

    @NonNull
    @Override
    public CvikViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_cvik_detail, parent, false);
        return new CvikViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CvikViewHolder holder, int position) {
        Cvik cvik = seznamCviku.get(position);
        holder.tvNazev.setText(cvik.getNazev());
        holder.tvSerie.setText(String.valueOf(cvik.getSerie()));
        holder.tvOpakovani.setText(String.valueOf(cvik.getOpakovani()));
        holder.tvVaha.setText(String.format("%.1f kg", cvik.getVaha()));
    }

    @Override
    public int getItemCount() {
        return seznamCviku.size();
    }

    public static class CvikViewHolder extends RecyclerView.ViewHolder {
        TextView tvNazev, tvSerie, tvOpakovani, tvVaha;

        public CvikViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNazev = itemView.findViewById(R.id.tvNazev);
            tvSerie = itemView.findViewById(R.id.tvSerie);
            tvOpakovani = itemView.findViewById(R.id.tvOpakovani);
            tvVaha = itemView.findViewById(R.id.tvVaha);
        }
    }
}
