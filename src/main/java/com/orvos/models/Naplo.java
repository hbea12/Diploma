package com.orvos.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Naplo {

    //@Transient
    //private int sSz = 0;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //@Transient
    //private Integer sorszam;
    private String latogatasDatum;
    private String taj;
    private String  betegsegAzonosito;
    private String  gyogzszerAzonosito;
    private String  gyogzszerNev;
    private String  orvosNev;
    private String gyogyszerFelirtAdag;
    private String  orvosAzonosito;
    private String betegPanasz;

    /*@OneToOne
    @JoinColumn(name = "betegsegAzonosito")
    private Disease disease;

    @OneToOne
    @JoinColumn(name = "orvosAzonosito")
    private Orvos orvos;

    @OneToOne
    @JoinColumn(name = "gyogyszerAzonosito")
    private Gyogyszer gyogyszer;*/



    public Naplo() {
    }

    public Naplo(String latogatasDatum, String taj, String  betegsegAzonosito, String  gyogzszerAzonosito,
                 String gyogyszerFelirtAdag, String  orvosAzonosito, String betegPanasz) {
        //this.sorszam = sSz+=1;
        this.latogatasDatum = latogatasDatum;
        this.taj = taj;
        this.betegsegAzonosito = betegsegAzonosito;
        this.gyogzszerAzonosito = gyogzszerAzonosito;
        this.gyogyszerFelirtAdag = gyogyszerFelirtAdag;
        this.orvosAzonosito = orvosAzonosito;
        this.betegPanasz = betegPanasz;
        //sSz++;
    }

    public Naplo(String  latogatasDatum, String taj, String  betegsegAzonosito, String gyogzszerAzonosito,
                 String gyogyszerFelirtAdag, String betegPanasz) {
        //this.sorszam = sSz+=1;
        this.latogatasDatum = latogatasDatum;
        this.taj = taj;
        this.betegsegAzonosito = betegsegAzonosito;
        this.gyogzszerAzonosito = gyogzszerAzonosito;
        this.gyogyszerFelirtAdag = gyogyszerFelirtAdag;
        this.betegPanasz = betegPanasz;
        //sSz++;
    }
}
