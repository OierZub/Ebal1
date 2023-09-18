package com.example.ebal1;

public class Produktua {
    private String izena;
    private int kopurua;


    public Produktua(){}

    public Produktua(String psIzena, int piKopurua){
        izena   = psIzena;
        kopurua = piKopurua;
    }

    // Getter eta Setter-ak.
    public String getIzena() {
        return izena;
    }

    public void setIzena(String izena) {
        this.izena = izena;
    }

    public int getKopurua() {
        return kopurua;
    }

    public void setKopurua(int kopurua) {
        this.kopurua = kopurua;
    }
}
