package com.example.mehealth;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class User implements Serializable {
    private ArrayList<Integer> weightHistory;
    private ArrayList<Integer> alipaineHistoria;
    private ArrayList<Integer> ylipaineHistoria;
    private int pituus;
    private int ika;

    public User() {
        this.weightHistory = new ArrayList<>();
        this.alipaineHistoria = new ArrayList<>();
        this.ylipaineHistoria = new ArrayList<>();
    }

    public void addWeightRecord(int weight) {
        this.weightHistory.add(weight);
    }

    public void addAlipaineRecord(int alipaine) {
        this.alipaineHistoria.add(alipaine);
    }

    public void addYliPaineRecord(int ylipaine) {
        this.ylipaineHistoria.add(ylipaine);
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

    public int getAliPaineNow() {
        if (alipaineHistoria.size() == 0) {
            return 0;
        }
        return alipaineHistoria.get(alipaineHistoria.size() - 1);
    }

    public int getYliPaineNow() {
        if (ylipaineHistoria.size() == 0) {
            return 0;
        }
        return ylipaineHistoria.get(ylipaineHistoria.size() - 1);
    }
}
