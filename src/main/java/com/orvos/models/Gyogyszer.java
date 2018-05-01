package com.orvos.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Gyogyszer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    private String gyogyszerAzonosito;
    private String gyogyszerNev;
    private String gyogyszerHatoanyag;
    private Integer gyogyszerAr;
    private Integer gyogyszerMennyiseg;
    private String gyogyszerMertekegyseg;
    private Boolean venykoteles;


    public Gyogyszer(String gyogyszerAzonosito, String gyogyszerNev, String gyogyszerHatoanyag, Integer gyogyszerAr,
                     Integer gyogyszerMennyiseg, String gyogyszerMertekegyseg, Boolean venykoteles) {
        this.gyogyszerAzonosito = gyogyszerAzonosito;
        this.gyogyszerNev = gyogyszerNev;
        this.gyogyszerHatoanyag = gyogyszerHatoanyag;
        this.gyogyszerAr = gyogyszerAr;
        this.gyogyszerMennyiseg = gyogyszerMennyiseg;
        this.gyogyszerMertekegyseg = gyogyszerMertekegyseg;
        this.venykoteles = venykoteles;
    }

    public Gyogyszer() {
    }
}
