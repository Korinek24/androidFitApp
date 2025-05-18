package com.example.fitnessapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class TreninkDB extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fitness.db";
    private static final int DATABASE_VERSION = 2; // ZVYSENO z 1 na 2
    public static final String TABLE_TRENINKY = "treninky";
    public static final String COLUMN_TRENINK_ID = "id";
    public static final String COLUMN_TRENINK_DATUM = "datum";
    public static final String COLUMN_TRENINK_OBRAZEK = "obrazek";
    public static final String TABLE_CVIKY = "cviky";
    public static final String COLUMN_CVIK_ID = "id";
    public static final String COLUMN_CVIK_NAZEV = "nazev";
    public static final String COLUMN_CVIK_SERIE = "serie";
    public static final String COLUMN_CVIK_OPAKOVANI = "opakovani";
    public static final String COLUMN_CVIK_VAHA = "vaha";
    public static final String COLUMN_CVIK_TRENINK_ID = "trenink_id";

    public TreninkDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTreninky = "CREATE TABLE " + TABLE_TRENINKY + " (" +
                COLUMN_TRENINK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TRENINK_DATUM + " TEXT UNIQUE, " +
                COLUMN_TRENINK_OBRAZEK + " TEXT)";
        db.execSQL(createTreninky);

        String createCviky = "CREATE TABLE " + TABLE_CVIKY + " (" +
                COLUMN_CVIK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CVIK_NAZEV + " TEXT, " +
                COLUMN_CVIK_SERIE + " INTEGER, " +
                COLUMN_CVIK_OPAKOVANI + " INTEGER, " +
                COLUMN_CVIK_VAHA + " REAL, " +
                COLUMN_CVIK_TRENINK_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_CVIK_TRENINK_ID + ") REFERENCES " + TABLE_TRENINKY + "(" + COLUMN_TRENINK_ID + ") ON DELETE CASCADE)";
        db.execSQL(createCviky);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_TRENINKY + " ADD COLUMN " + COLUMN_TRENINK_OBRAZEK + " TEXT");
        }
    }

    public void ulozTrenink(String datum, ArrayList<Cvik> cviky, String obrazekCesta) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            Cursor cursor = db.rawQuery("SELECT id FROM treninky WHERE datum = ?", new String[]{datum});
            int treninkId;
            if (cursor.moveToFirst()) {
                treninkId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TRENINK_ID));
                db.delete(TABLE_CVIKY, COLUMN_CVIK_TRENINK_ID + " = ?", new String[]{String.valueOf(treninkId)});
                db.delete(TABLE_TRENINKY, COLUMN_TRENINK_ID + " = ?", new String[]{String.valueOf(treninkId)});
            }
            cursor.close();

            ContentValues treninkValues = new ContentValues();
            treninkValues.put(COLUMN_TRENINK_DATUM, datum);
            treninkValues.put(COLUMN_TRENINK_OBRAZEK, obrazekCesta);
            treninkId = (int) db.insert(TABLE_TRENINKY, null, treninkValues);

            for (Cvik cvik : cviky) {
                ContentValues cvikValues = new ContentValues();
                cvikValues.put(COLUMN_CVIK_NAZEV, cvik.getNazev());
                cvikValues.put(COLUMN_CVIK_SERIE, cvik.getSerie());
                cvikValues.put(COLUMN_CVIK_OPAKOVANI, cvik.getOpakovani());
                cvikValues.put(COLUMN_CVIK_VAHA, cvik.getVaha());
                cvikValues.put(COLUMN_CVIK_TRENINK_ID, treninkId);
                db.insert(TABLE_CVIKY, null, cvikValues);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public ArrayList<Trenink> ziskejTreninky() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Trenink> treninky = new ArrayList<>();

        Cursor cursor = db.rawQuery("SELECT * FROM Treninky ORDER BY datum DESC", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String datum = cursor.getString(cursor.getColumnIndexOrThrow("datum"));
                String obrazek = cursor.getString(cursor.getColumnIndexOrThrow("obrazek"));
                Trenink trenink = new Trenink(id, datum, obrazek);

                ArrayList<Cvik> cviky = ziskejCvikyProTrenink(id);
                for (Cvik c : cviky) {
                    trenink.addCvik(c);
                }

                treninky.add(trenink);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return treninky;
    }

    public ArrayList<Cvik> ziskejCvikyProTrenink(int treninkId) {
        ArrayList<Cvik> cviky = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM Cviky WHERE trenink_id = ?", new String[]{String.valueOf(treninkId)});
        if (cursor.moveToFirst()) {
            do {
                String nazev = cursor.getString(cursor.getColumnIndexOrThrow("nazev"));
                int serie = cursor.getInt(cursor.getColumnIndexOrThrow("serie"));
                int opakovani = cursor.getInt(cursor.getColumnIndexOrThrow("opakovani"));
                double vaha = cursor.getDouble(cursor.getColumnIndexOrThrow("vaha"));

                Cvik cvik = new Cvik(nazev, serie, opakovani, vaha);
                cvik.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));

                cviky.add(cvik);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cviky;
    }
}
