package com.example.restapi;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.swing.*;

@RestController
public class Controller {
    Podmiotrezerwujacy[] podmiotRezerwujacy;
    MiejscaParkingowe[] miejscaParkingowe;
    RezerwacjaMiejscaParkingowego[] rezerwacjaMiejscaParkingowego;

    Controller(){
        podmiotRezerwujacy=fillPodmiotRezerwujacy();
        miejscaParkingowe=fillMiejsceParkingowe();
        rezerwacjaMiejscaParkingowego=fillRezerwacjaMiejscaParkingowego(podmiotRezerwujacy,miejscaParkingowe);

    }




    @RequestMapping
    public String helloWorld(){
        String odpowiedz="/users lista uzytkownikow\n/places lista miejsc parkingowych\n/rezerwacje lista rezerwacje\n/freeplaces lista wolnych miejsc\n/usersplaces?name=x x-nazwa uzytkownika daje liste miejsc zajetych przez uzytkownika\n/reserve?reserve=x_X x-nazwa uzytkownika X-nazwa miejsca dodaje rezerwacje\n/unreserve?unreserve=x_X x-nazwa uzytkownika X-nazwa miejsca usuwa rezerwacje";
        return odpowiedz;
    }
    @RequestMapping("/users")
    public Podmiotrezerwujacy[] users(){
        return podmiotRezerwujacy;
    }
    @RequestMapping("/places")
    public MiejscaParkingowe[] places(){return miejscaParkingowe;}
    @RequestMapping("/rezerwacje")
    public RezerwacjaMiejscaParkingowego[] rezerwacje(){return rezerwacjaMiejscaParkingowego;}
    @RequestMapping("/freeplaces")
    public MiejscaParkingowe[] freeplaces(){
        int L1=miejscaParkingowe.length;
        int L2=rezerwacjaMiejscaParkingowego.length;
        boolean[] wolne=new boolean[L1];
        int iloscMiejsc=0;
        for (int i=0;i<L1;i++){
            wolne[i]=true;
            for(int j=0;j<L2;j++){
                if(rezerwacjaMiejscaParkingowego[j].getIdMiejsca().equals(miejscaParkingowe[i].getId())){
                    wolne[i]=false;
                    break;
                }
            }
            if(wolne[i]==true)
                iloscMiejsc++;
        }
        MiejscaParkingowe[] wolneMiejsca=new MiejscaParkingowe[iloscMiejsc];
        int j=0;
        for (int i=0;i<L1;i++){
            if (wolne[i]==true){
                wolneMiejsca[j]=miejscaParkingowe[i];
                j++;
            }
        }
        return wolneMiejsca;
    }
    // /usersplaces?name=Adrian
    @GetMapping("/usersplaces")
    public MiejscaParkingowe[] usersplaces(@RequestParam(value = "name")String name){
        boolean isOnTheList=false;
        for (int i=0;i< podmiotRezerwujacy.length;i++){
            if(name.equals(podmiotRezerwujacy[i].getNazwa())){
                isOnTheList=true;
                break;
            }
        }
        if (isOnTheList==false) {
            return null;
        }
        else{
            boolean[] czyZarezerwowane=new boolean[rezerwacjaMiejscaParkingowego.length];
            int ilosczarzerwowanych=0;
            for (int i=0;i< rezerwacjaMiejscaParkingowego.length;i++){
                czyZarezerwowane[i]=false;
                if(name.equals(rezerwacjaMiejscaParkingowego[i].getIdRezerwujacego())){
                    czyZarezerwowane[i]=true;
                    ilosczarzerwowanych++;
                }
            }
            MiejscaParkingowe[] zarezerwowaneMiejsca=new MiejscaParkingowe[ilosczarzerwowanych];
            String[] idZarezerwowanychMiejsc=new String[ilosczarzerwowanych];
            int j=0;
            for (int i=0;i< rezerwacjaMiejscaParkingowego.length;i++) {
                if (czyZarezerwowane[i]) {
                    idZarezerwowanychMiejsc[j] = rezerwacjaMiejscaParkingowego[i].getIdMiejsca();
                    j++;
                }
            }
            for (j=0;j<ilosczarzerwowanych;j++) {
                for (int i = 0; i < miejscaParkingowe.length; i++) {
                    if (idZarezerwowanychMiejsc[j].equals(miejscaParkingowe[i].getId())){
                        zarezerwowaneMiejsca[j]=miejscaParkingowe[i];
                    }
                }
            }
            return zarezerwowaneMiejsca;
        }

    }
    @GetMapping("/reserve")
    public String reserve(@RequestParam(value = "reserve")String rezerwacja){
        int L=rezerwacja.length();
        boolean found=false;
        int spacePlace=0;
        for (int i=0;i<L;i++){
            if (rezerwacja.charAt(i)=='_'){
                found=true;
                spacePlace=i;
                break;
            }
        }
        String rezerwacjaName="";
        String rezerwacjaPlace="";
        if (found){
            rezerwacjaName=rezerwacja.substring(0,spacePlace);
            rezerwacjaPlace=rezerwacja.substring(spacePlace+1);
        }
        else {
            return "400 Błędny format rezerwacji";
        }
        if (rezerwacjaName.length()<2||rezerwacjaName.length()>20){
            return "406 Nazwa uzytkownika musi miec miedzy 2 a 20 znakow";
        }
        int L1= miejscaParkingowe.length;
        boolean placeOnTheList=false;
        for (int i=0;i<L1;i++){
            if (rezerwacjaPlace.equals(miejscaParkingowe[i].getId())){
                placeOnTheList=true;
            }
        }
        if (!placeOnTheList){
            return "404 Miejsce parkingowe nie istnieje";
        }
        boolean miejsceZajete=false;
        int L2=rezerwacjaMiejscaParkingowego.length;
        for (int i=0;i<L2;i++){
            if (rezerwacjaPlace.equals(rezerwacjaMiejscaParkingowego[i].getIdMiejsca())){
                miejsceZajete=true;
            }
        }
        if (miejsceZajete){
            return "409 Miejsce parkingowe jest juz zarezerwowane";
        }
        int L3=podmiotRezerwujacy.length;
        boolean nameOnTheList=false;
        for (int i=0;i<L3;i++){
            if (rezerwacjaName.equals(podmiotRezerwujacy[i].getNazwa())){
                nameOnTheList=true;
            }
        }
        if (!nameOnTheList){
            addName(rezerwacjaName);
        }
        addReservation(rezerwacjaName,rezerwacjaPlace);
        return "200 Udalo sie zarezerwowac miejsce";
    }
    @GetMapping("/unreserve")
    public String unreserve(@RequestParam(value = "unreserve")String rezerwacja){
        int L=rezerwacja.length();
        boolean found=false;
        int spacePlace=0;
        for (int i=0;i<L;i++){
            if (rezerwacja.charAt(i)=='_'){
                found=true;
                spacePlace=i;
                break;
            }
        }
        String rezerwacjaName="";
        String rezerwacjaPlace="";
        if (found){
            rezerwacjaName=rezerwacja.substring(0,spacePlace);
            rezerwacjaPlace=rezerwacja.substring(spacePlace+1);
        }
        else {
            return "400 Błędny format rezerwacji";
        }
        if (rezerwacjaName.length()<2||rezerwacjaName.length()>20){
            return "406 Nazwa uzytkownika musi miec miedzy 2 a 20 znakow";
        }
        int L1= miejscaParkingowe.length;
        boolean placeOnTheList=false;
        for (int i=0;i<L1;i++){
            if (rezerwacjaPlace.equals(miejscaParkingowe[i].getId())){
                placeOnTheList=true;
            }
        }
        if (!placeOnTheList){
            return "404 Miejsce parkingowe nie istnieje";
        }
        boolean miejsceZajete=false;
        int L2=rezerwacjaMiejscaParkingowego.length;
        for (int i=0;i<L2;i++){
            if (rezerwacjaPlace.equals(rezerwacjaMiejscaParkingowego[i].getIdMiejsca())){
                miejsceZajete=true;
            }
        }
        if (!miejsceZajete){
            return "409 Miejsce parkingowe nie jest zarezerwowane";
        }
        int L3=podmiotRezerwujacy.length;
        boolean nameOnTheList=false;
        for (int i=0;i<L3;i++){
            if (rezerwacjaName.equals(podmiotRezerwujacy[i].getNazwa())){
                nameOnTheList=true;
            }
        }
        if (nameOnTheList){
            removeName(rezerwacjaName);
        }
        removeReservation(rezerwacjaName,rezerwacjaPlace);
        return "200 Udalo sie zarezerwowac miejsce";
    }



    private Podmiotrezerwujacy[] fillPodmiotRezerwujacy(){
        String[] defaultnazwa={
                "Adrian",
                "Olaf",
                "Sergiusz",
                "Lubomir",
                "Kosma",
                "Seweryn",
                "Hipolit",
                "Radoslaw",
                "Zygmunt",
                "Florian"};
        Podmiotrezerwujacy[] defaultPodmiotRezerwujacy=new Podmiotrezerwujacy[defaultnazwa.length];
        for(int i=0;i<defaultnazwa.length;i++){
            defaultPodmiotRezerwujacy[i]=new Podmiotrezerwujacy(defaultnazwa[i]);
        }
        return defaultPodmiotRezerwujacy;

    }
    private MiejscaParkingowe[] fillMiejsceParkingowe(){
        int L=60;
        int[] defaultNrMiejsca=new int[L];
        int[] defaultKondygnacja= new int[L];
        boolean[] defaultNiepelnosprawnosc=new boolean[L];
        String[] defaultId=new String[L];
        MiejscaParkingowe[] defaultMiejsceParkingowe=new MiejscaParkingowe[L];
        for(int i=0;i<L;i++){
            defaultNrMiejsca[i]=i+1;
            defaultKondygnacja[i]=i/20;
            defaultNiepelnosprawnosc[i]=false;
            if(i==12||i==18||i==32||i==48)
                defaultNiepelnosprawnosc[i]=true;
            defaultId[i]=Integer.toString(defaultNrMiejsca[i])+"-"+Integer.toString(defaultKondygnacja[i]);
            defaultMiejsceParkingowe[i]=new MiejscaParkingowe(defaultId[i],defaultNrMiejsca[i],defaultKondygnacja[i],defaultNiepelnosprawnosc[i]);
        }
        return defaultMiejsceParkingowe;
    }
    private RezerwacjaMiejscaParkingowego[] fillRezerwacjaMiejscaParkingowego(Podmiotrezerwujacy[] osoba,MiejscaParkingowe[] parking){
        String[] o=new String[osoba.length];
        for (int i=0;i<osoba.length;i++){
            o[i]=osoba[i].getNazwa();
        }
        String[] m=new String[parking.length];
        for (int i=0;i<parking.length;i++){
            m[i]=parking[i].getId();
        }
        String[] defaultOsoba={o[0],o[0],o[1],o[2],o[3],
                o[3],o[4],o[5],o[6],o[6],
                o[7],o[8],o[8],o[8],o[9]};
        String[] defaultMiejsce={m[2],m[25],m[13],m[33],m[45],
        m[7],m[52],m[11],m[57],m[0],
        m[12],m[16],m[37],m[44],m[30]};
        RezerwacjaMiejscaParkingowego[] defaultRezerwacja=new RezerwacjaMiejscaParkingowego[defaultOsoba.length];
        for(int i=0;i<defaultOsoba.length;i++){
            defaultRezerwacja[i]=new RezerwacjaMiejscaParkingowego(defaultOsoba[i],defaultMiejsce[i]);
        }
        return defaultRezerwacja;
    }
    private void addName(String name){
        int L=podmiotRezerwujacy.length;
        Podmiotrezerwujacy[] nowyPodmiot=new Podmiotrezerwujacy[L+1];
        for (int i=0;i<L;i++){
            nowyPodmiot[i]=podmiotRezerwujacy[i];
        }
        nowyPodmiot[L]=new Podmiotrezerwujacy(name);
        podmiotRezerwujacy=nowyPodmiot;
    }
    private void addReservation(String name,String place){
        int L=rezerwacjaMiejscaParkingowego.length;
        RezerwacjaMiejscaParkingowego[] nowaRezerwacja=new  RezerwacjaMiejscaParkingowego[L+1];
        for (int i=0;i<L;i++){
            nowaRezerwacja[i]=rezerwacjaMiejscaParkingowego[i];
        }
        nowaRezerwacja[L]=new RezerwacjaMiejscaParkingowego(name,place);
        rezerwacjaMiejscaParkingowego=nowaRezerwacja;
    }
    private void removeName(String name){
        int L=podmiotRezerwujacy.length;
        Podmiotrezerwujacy[] nowyPodmiot=new Podmiotrezerwujacy[L-1];
        int nastepny=0;
        for (int i=0;i<L-1;i++){
            if (name.equals(podmiotRezerwujacy[i].getNazwa())){
                nastepny=1;
            }
            nowyPodmiot[i]=podmiotRezerwujacy[i+nastepny];
        }
        podmiotRezerwujacy=nowyPodmiot;
    }
    private void removeReservation(String name,String place){
        int L=rezerwacjaMiejscaParkingowego.length;
        RezerwacjaMiejscaParkingowego[] nowaRezerwacja=new  RezerwacjaMiejscaParkingowego[L-1];
        int nastepny=0;
        for (int i=0;i<L-1;i++){
            if(place.equals(rezerwacjaMiejscaParkingowego[i].getIdMiejsca())){
                nastepny=1;
            }
            nowaRezerwacja[i]=rezerwacjaMiejscaParkingowego[i+nastepny];
        }
        rezerwacjaMiejscaParkingowego=nowaRezerwacja;
    }
}

