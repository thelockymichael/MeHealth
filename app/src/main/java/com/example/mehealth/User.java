package com.example.mehealth;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class User implements Serializable {
    private ArrayList<Integer> weightHistory;
    private ArrayList<Integer> alapaineHistoria;
    private ArrayList<Integer> ylapaineHistoria;
    private int pituus;
    private int ika;

    public User() {
        this.weightHistory = new ArrayList<>();
        this.alapaineHistoria = new ArrayList<>();
        this.ylapaineHistoria = new ArrayList<>();
    }

    public void addWeightRecord(int weight) {
        this.weightHistory.add(weight);
    }

    public void addAlapaineRecord(int alapaine) {
        this.alapaineHistoria.add(alapaine);
    }

    public void addYlaPaineRecord(int ylapaine) {
        this.ylapaineHistoria.add(ylapaine);
    }

    public void setPituus(int pituus) {
        this.pituus = pituus;
    }

    public void setIka(int ika) {
        this.ika = ika;
    }

    public int getWeightNow() {
        if (weightHistory.size() == 0) {
            return 0;
        }
        return weightHistory.get(weightHistory.size() - 1);
    }

    public int getAlaPaineNow() {
        if (alapaineHistoria.size() == 0) {
            return 0;
        }
        return alapaineHistoria.get(alapaineHistoria.size() - 1);
    }

    public int getYlaPaineNow() {
        if (ylapaineHistoria.size() == 0) {
            return 0;
        }
        return ylapaineHistoria.get(ylapaineHistoria.size() - 1);
    }
}
