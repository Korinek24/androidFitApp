package com.example.fitnessapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.pm.PackageManager;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NovyTrenink extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 101;

    LinearLayout containerCviky;
    Button btnPridatCvik, btnUlozitTrenink, btnVybratObrazek;
    ImageView imagePreview;
    private static final int CAMERA_REQUEST_CODE = 2;
    private Uri photoUri;



    Uri selectedImageUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_novy_trenink);

        containerCviky = findViewById(R.id.containerCviky);
        btnPridatCvik = findViewById(R.id.btnPridatCvik);
        btnUlozitTrenink = findViewById(R.id.btnUlozitTrenink);
        btnVybratObrazek = findViewById(R.id.btnVybratObrazek);
        imagePreview = findViewById(R.id.imagePreview);

        btnPridatCvik.setOnClickListener(v -> {
            View novyCvikView = getLayoutInflater().inflate(R.layout.item_cvik, null);
            containerCviky.addView(novyCvikView);
        });

        Button btnVyfotitObrazek = findViewById(R.id.btnFotit);

        btnVyfotitObrazek.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
            } else {
                otevriFotoaparat();
            }
        });



        btnVybratObrazek.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            } else {
                vyberObrazek();
            }
        });

        btnUlozitTrenink.setOnClickListener(v -> {
            TreninkDB dbHelper = new TreninkDB(NovyTrenink.this);
            String datum = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            ArrayList<Cvik> noveCviky = new ArrayList<>();
            int count = containerCviky.getChildCount();
            for (int i = 0; i < count; i++) {
                View cvikView = containerCviky.getChildAt(i);

                EditText etNazev = cvikView.findViewById(R.id.etNazev);
                EditText etSerie = cvikView.findViewById(R.id.etSerie);
                EditText etOpakovani = cvikView.findViewById(R.id.etOpakovani);
                EditText etVaha = cvikView.findViewById(R.id.etVaha);

                String nazev = etNazev.getText().toString().trim();
                if (nazev.isEmpty()) continue;

                try {
                    int serie = Integer.parseInt(etSerie.getText().toString());
                    int opakovani = Integer.parseInt(etOpakovani.getText().toString());
                    float vaha = Float.parseFloat(etVaha.getText().toString());

                    noveCviky.add(new Cvik(nazev, serie, opakovani, vaha));
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Chyba ve formátu čísel u jednoho z cviků", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if (noveCviky.isEmpty()) {
                Toast.makeText(this, "Zadej alespoň jeden cvik", Toast.LENGTH_SHORT).show();
                return;
            }

            String cestaKObrazku = null;
            if (selectedImageUri != null) {
                cestaKObrazku = ulozObrazekDoInterniPameti(selectedImageUri, datum);
            }

            dbHelper.ulozTrenink(datum, noveCviky, cestaKObrazku);
            Toast.makeText(this, "Trénink uložen", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void vyberObrazek() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    private void otevriFotoaparat() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = vytvorSouborProFotku();
            if (photoFile != null) {
                photoUri = FileProvider.getUriForFile(this, getPackageName() + ".provider", photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        }
    }
    private File vytvorSouborProFotku() {
        try {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String fileName = "JPEG_" + timeStamp + "_";
            File storageDir = getFilesDir(); // interní úložiště
            return File.createTempFile(fileName, ".jpg", storageDir);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String ulozObrazekDoInterniPameti(Uri uri, String datum) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            File file = new File(getFilesDir(), datum + "_trenink.jpg");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                vyberObrazek();
            } else {
                Toast.makeText(this, "Přístup k úložišti zamítnut", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            imagePreview.setImageURI(selectedImageUri);
        } else if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            selectedImageUri = photoUri;
            imagePreview.setImageURI(photoUri);
        }
    }


    private Uri ulozBitmapDoPameti(Bitmap bitmap) {
        try {
            File file = new File(getFilesDir(), "foto_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.close();
            return Uri.fromFile(file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
