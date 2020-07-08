package ru.denfad.cheackheart.models;

import java.util.Calendar;

public class Diagnose {

    private Integer id;
    private String diagnose;
    private Calendar date;

    public Diagnose(String diagnose, Calendar date) {
        this.diagnose = diagnose;
        this.date = date;
    }

    public Diagnose() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDiagnose() {
        return diagnose;
    }

    public void setDiagnose(String diagnose) {
        this.diagnose = diagnose;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }
}

