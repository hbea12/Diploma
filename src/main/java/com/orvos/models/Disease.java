package com.orvos.models;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Disease {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String betegsegId;
    private String betegsegNev;



    public Disease() {
    }

    public Disease(String betegsegId, String betegsegNev) {
        this.betegsegId = betegsegId;
        this.betegsegNev = betegsegNev;
    }
}
