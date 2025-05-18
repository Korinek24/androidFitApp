package com.example.fitnessapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TreninkAdapter extends RecyclerView.Adapter<TreninkAdapter.TreninkViewHolder> {

    private ArrayList<Trenink> seznamTreninku;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public TreninkAdapter(ArrayList<Trenink> seznamTreninku, Context context) {
        this.seznamTreninku = seznamTreninku;
        this.context = context;
    }

    @NonNull
    @Override
    public TreninkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_trenink, parent, false);
        return new TreninkViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TreninkViewHolder holder, int position) {
        Trenink trenink = seznamTreninku.get(position);

        holder.tvDatum.setText(trenink.getDatum());

        int pocetCviků = trenink.getCviky() != null ? trenink.getCviky().size() : 0;
        holder.tvPocetCviku.setText("Počet cviků: " + pocetCviků);

        // Posluchač pro kliknutí na položku tréninku
        holder.itemView.setOnClickListener(v -> {
            // Pokud posluchač není null, zavoláme metodu onItemClick
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(trenink);
            }
        });
    }

    @Override
    public int getItemCount() {
        return seznamTreninku.size();
    }

    // Nastavení posluchače pro kliknutí na trénink
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    // Interface pro posluchače kliknutí
    public interface OnItemClickListener {
        void onItemClick(Trenink trenink);
    }

    public static class TreninkViewHolder extends RecyclerView.ViewHolder {
        TextView tvDatum, tvPocetCviku;

        public TreninkViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDatum = itemView.findViewById(R.id.tvDatumTreninku);
            tvPocetCviku = itemView.findViewById(R.id.tvPocetCviků);
        }
    }
}
