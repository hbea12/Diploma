package com.orvos.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String taj;
    private String nev;
    private String szuletesiDatum;
    private String cim;
    private String orvosAzonosito;
    private String email;

    public Patient(String taj, String nev, String szuletesiDatum, String cim, String orvosAzonosito, String email) {
        this.taj = taj;
        this.nev = nev;
        this.szuletesiDatum = szuletesiDatum;
        this.cim = cim;
        this.orvosAzonosito = orvosAzonosito;
        this.email = email;
    }

    public Patient() {
    }
}
