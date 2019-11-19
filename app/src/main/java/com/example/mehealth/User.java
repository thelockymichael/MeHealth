package com.example.mehealth;

import java.util.ArrayList;

public class User {
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
}
