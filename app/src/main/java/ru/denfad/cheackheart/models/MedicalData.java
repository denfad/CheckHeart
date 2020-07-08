package ru.denfad.cheackheart.models;

import java.util.Calendar;

public class MedicalData {

    private Integer id;
    private String ecg;
    private String ppg;
    private Calendar dateTime;
    private Boolean deviations;
    private User user;


    public MedicalData(Integer id, String ecg, String ppg, Calendar dateTime, Boolean deviations, User user) {
        this.id=id;
        this.ecg=ecg;
        this.ppg=ppg;
        this.dateTime=dateTime;
        this.user=user;
        this.deviations=deviations;
    }

    public MedicalData(String ecg,String ppg, Calendar dateTime, Boolean deviations, User user) {
        this.ecg=ecg;
        this.ppg=ppg;
        this.dateTime=dateTime;
        this.user=user;
        this.deviations=deviations;
    }


    public MedicalData() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEcg() {
        return ecg;
    }

    public void setEcg(String ecg) {
        this.ecg = ecg;
    }

    public String getPpg() {
        return ppg;
    }

    public void setPpg(String ppg) {
        this.ppg = ppg;
    }

    public Calendar getDateTime() {
        return dateTime;
    }

    public void setDateTime(Calendar dateTime) {
        this.dateTime = dateTime;
    }

    public Boolean getDeviations() {
        return deviations;
    }

    public void setDeviations(Boolean deviations) {
        this.deviations = deviations;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
