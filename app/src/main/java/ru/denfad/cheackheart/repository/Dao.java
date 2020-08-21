package ru.denfad.cheackheart.repository;


import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.denfad.cheackheart.models.MedicalData;

public class Dao {
    private static Dao instance;
    private MedicalData md = new MedicalData();
    public List<Integer> ecg = new ArrayList<>();
    public List<Integer> ppg = new ArrayList<>();
    public List<Integer> pulse = new ArrayList<>();
    private Dao(){}

    public static Dao getInstance(){
        if(instance==null){
            instance = new Dao();
        }
        return instance;
    }

    public void addMedicalData(MedicalData data){
        this.md = data;
    }

    public void add(String strIn){
        Log.d("incom", strIn);
        parseString(strIn);
    }

    public MedicalData getMedicalData(){
        return md;
    }

    private void parseString(String strIn){
        String[] cutString = strIn.split("%");
        for(int i = 1; i<cutString.length; i++){
            List<Integer> data = new ArrayList<>();
            String[] dataStr = cutString[i].split(" ");
            for(String s:dataStr){
                if(!s.equals("") && !s.equals(" ")) {
                    data.add(Integer.parseInt(s));
                }
            }
            Log.d("data", Arrays.toString(data.toArray()));
            switch (i){
                case 0:
                    ecg.clear();
                    ecg.addAll(data);
                    md.setEcg(cutString[i]);
                    break;
                case 1:
                    ppg.clear();
                    ppg.addAll(data);
                    md.setPpg(cutString[i]);
                    break;
                case 2:
                    pulse.clear();
                    pulse.addAll(data);
                    break;
            }
        }
    }
}
