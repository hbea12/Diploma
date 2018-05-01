package com.orvos.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
public class Appointment{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String MondayMorning;
    private String MondayAfternoon;

    private String TuesdayMorning;
    private String TuesdayAfternoon;


    private String WednesdayMorning;
    private String WednesdayAfternoon;


    private String ThursdayMorning;
    private String ThursdayAfternoon;


    private String FridayMorning;
    private String FridayAfternoon;

    @OneToOne(fetch = FetchType.LAZY,
            cascade =  CascadeType.ALL,
            mappedBy = "appointment")
    private Orvos orvos;


    public Appointment(String mondayMorning, String mondayAfternoon, String tuesdayMorning, String tuesdayAfternoon, String wednesdayMorning, String wednesdayAfternoon, String thursdayMorning, String thursdayAfternoon, String fridayMorning, String fridayAfternoon) {
        MondayMorning = mondayMorning;
        MondayAfternoon = mondayAfternoon;
        TuesdayMorning = tuesdayMorning;
        TuesdayAfternoon = tuesdayAfternoon;
        WednesdayMorning = wednesdayMorning;
        WednesdayAfternoon = wednesdayAfternoon;
        ThursdayMorning = thursdayMorning;
        ThursdayAfternoon = thursdayAfternoon;
        FridayMorning = fridayMorning;
        FridayAfternoon = fridayAfternoon;
    }

    public Appointment() {
    }
}
