package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class historieTreninku extends AppCompatActivity {
    RecyclerView recyclerView;
    TreninkDB db;
    ArrayList<Trenink> seznamTreninku;
    TreninkAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historie_treninku);

        recyclerView = findViewById(R.id.recyclerTreninky);
        db = new TreninkDB(this);

        // Získání všech tréninků
        seznamTreninku = db.ziskejTreninky(); // Metoda pro získání seznamu tréninků

        if (seznamTreninku.isEmpty()) {
            // Můžeš zobrazit nějaký text nebo prázdný stav
            // Například TextView s hláškou "Žádné tréninky nejsou k dispozici"
        }

        // Nastavení adapteru pro RecyclerView
        adapter = new TreninkAdapter(seznamTreninku, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Nastavení posluchače pro kliknutí na trénink
        adapter.setOnItemClickListener(new TreninkAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Trenink trenink) {
                // Po kliknutí na trénink se přejde na detail tréninku
                Intent intent = new Intent(historieTreninku.this, DetailTreninkuActivity.class);
                intent.putExtra("treninkId", trenink.getId());       // <-- důležité!
                intent.putExtra("treninkDatum", trenink.getDatum()); // volitelné
                intent.putExtra("treninkObrazek", trenink.getObrazekPath());
                startActivity(intent);
            }
        });

    }
}
