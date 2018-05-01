package com.orvos.models.forms;

import lombok.Data;

@Data
public class Idoszak {
    String fromDate;
    String toDate;

    public Idoszak(String  fromDate, String  toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    public Idoszak() {
    }
}