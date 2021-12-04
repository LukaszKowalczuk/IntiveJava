package com.example.restapi;

public class RezerwacjaMiejscaParkingowego {
    private String idRezerwujacego;
    private String idMiejsca;
    RezerwacjaMiejscaParkingowego(String idRezerwujacego,String idMiejsca){
        this.idRezerwujacego=idRezerwujacego;
        this.idMiejsca=idMiejsca;
    }
    public String getIdRezerwujacego(){
        return idRezerwujacego;
    }
    public String getIdMiejsca(){
        return idMiejsca;
    }
}
