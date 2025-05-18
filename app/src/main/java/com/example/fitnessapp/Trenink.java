package com.example.fitnessapp;

import java.util.ArrayList;

public class Trenink {

    private final int id;
    private final String datum;
    private final String obrazekPath;
    private final ArrayList<Cvik> cviky;

    public Trenink(int id, String datum, String obrazekPath) {
        this.id = id;
        this.datum = datum;
        this.obrazekPath = obrazekPath;
        this.cviky = new ArrayList<>();
    }

    public Trenink(String datum, String obrazekPath) {
        this.id = -1; // nebo 0, pokud používáš jiný default
        this.datum = datum;
        this.obrazekPath = obrazekPath;
        this.cviky = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getDatum() {
        return datum;
    }

    public String getObrazekPath() {
        return obrazekPath;
    }

    public ArrayList<Cvik> getCviky() {
        return cviky;
    }

    public void addCvik(Cvik cvik) {
        this.cviky.add(cvik);
    }

    public void removeCvik(Cvik cvik) {
        this.cviky.remove(cvik);
    }

    public boolean containsCvik(int cvikId) {
        for (Cvik cvik : cviky) {
            if (cvik.getId() == cvikId) {
                return true;
            }
        }
        return false;
    }
}
