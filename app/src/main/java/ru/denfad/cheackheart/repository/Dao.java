package ru.denfad.cheackheart.repository;

import android.util.Log;

import ru.denfad.cheackheart.models.MedicalData;

public class Dao {
    private static Dao instance;
    private MedicalData data;
    private Dao(){}

    public static Dao getInstance(){
        if(instance==null){
            instance = new Dao();
        }
        return instance;
    }

    public void addMedicalData(MedicalData data){
        this.data = data;
    }

    public void add(String strIn){
        Log.e("incom",strIn);
    }

    public MedicalData getMedicalData(){
        return data;
    }


}
