package com.example.fitnessapp;

public class Cvik {
    private int id;
    private String nazev;
    private int serie;
    private int opakovani;
    private double vaha;

    public Cvik(String nazev, int serie, int opakovani, double vaha) {
        this.nazev = nazev;
        this.serie = serie;
        this.opakovani = opakovani;
        this.vaha = vaha;
    }

    // Gettery a settery
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public int getSerie() {
        return serie;
    }

    public void setSerie(int serie) {
        this.serie = serie;
    }

    public int getOpakovani() {
        return opakovani;
    }

    public void setOpakovani(int opakovani) {
        this.opakovani = opakovani;
    }

    public double getVaha() {
        return vaha;
    }

    public void setVaha(double vaha) {
        this.vaha = vaha;
    }
}
