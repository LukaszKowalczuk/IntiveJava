package com.example.restapi;

public class MiejscaParkingowe {
    private String id;
    private int nrMiejsca;
    private int kondygnacja;
    private boolean niepelnosprawnosc;

    MiejscaParkingowe(String id,int nrMiejsca,int kondygnacja,boolean niepelnosprawnosc){
        this.id=id;
        this.nrMiejsca = nrMiejsca;
        this.kondygnacja = kondygnacja;
        this.niepelnosprawnosc = niepelnosprawnosc;
    }
    public String getId(){
        return id;
    }

    public int getNrMiejsca() {
        return nrMiejsca;
    }

    public int getKondygnacja() {
        return kondygnacja;
    }

    public boolean getNiepelnosprawnosc(){
        return niepelnosprawnosc;
    }
}
