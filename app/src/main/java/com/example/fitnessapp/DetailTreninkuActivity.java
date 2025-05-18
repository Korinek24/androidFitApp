package com.example.fitnessapp;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DetailTreninkuActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TreninkDB db;
    ArrayList<Cvik> seznamCviku;
    CvikAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_treninku);

        recyclerView = findViewById(R.id.recyclerDetailCviky);
        db = new TreninkDB(this);

        int treninkId = getIntent().getIntExtra("treninkId", -1);
        if (treninkId == -1) {
            Log.e("DetailTreninkuActivity", "Chybí treninkId v intentu!");
            Toast.makeText(this, "Chyba: TreninkId není předán.", Toast.LENGTH_LONG).show();
            return;
        }

        seznamCviku = db.ziskejCvikyProTrenink(treninkId);

        // Zobrazení datumu
        String datum = getIntent().getStringExtra("treninkDatum");
        TextView tvDatum = findViewById(R.id.tvDetailDatum);
        tvDatum.setText("Datum: " + datum);

        // Zobrazení obrázku
        String obrazekPath = getIntent().getStringExtra("treninkObrazek");
        ImageView ivObrazek = findViewById(R.id.ivDetailObrazek);
        if (obrazekPath != null && !obrazekPath.isEmpty()) {
            ivObrazek.setImageBitmap(BitmapFactory.decodeFile(obrazekPath));
        }

        adapter = new CvikAdapter(seznamCviku, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}
