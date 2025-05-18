package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button btnNovyTrenink;
    Button btnHistorieTreninku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btnNovyTrenink = findViewById(R.id.pridatTrenink);
        btnHistorieTreninku = findViewById(R.id.historieTreninku);

        btnNovyTrenink.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NovyTrenink.class);
            startActivity(intent);
        });

        btnHistorieTreninku.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, historieTreninku.class);
            startActivity(intent);
        });
    }
}
