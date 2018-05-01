package com.orvos.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@ToString(exclude="appointment")
@EqualsAndHashCode(exclude="appointment")
public class Orvos{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String orvosAzonosito;
    private String orvosNev;
    private String zippAndCity;
    private String street;
    private String phoneNumber;
    private Boolean appointmentVisible;
    private Boolean status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment")
    private Appointment appointment;


    public Orvos() {
    }

    public Orvos(String orvosAzonosito, String orvosNev, String zippAndCity, String street, String phoneNumber) {
        this.orvosAzonosito = orvosAzonosito;
        this.orvosNev = orvosNev;
        this.zippAndCity = zippAndCity;
        this.street = street;
        this.phoneNumber = phoneNumber;
    }
}
