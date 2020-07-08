package ru.denfad.cheackheart.models;

import java.util.Calendar;

public class ClientDiagnose {
    private Integer id;
    private String diagnose;
    private String date;

    public ClientDiagnose(String diagnose, String date) {
        this.diagnose = diagnose;
        this.date = date;
    }

    public ClientDiagnose() {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
