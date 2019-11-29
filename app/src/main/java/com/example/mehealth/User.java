package com.example.mehealth;

import java.util.ArrayList;

public class User  {
    public ArrayList<Integer> weightHistory;
    private ArrayList<Integer> alapaineHistoria;
    private ArrayList<Integer> ylapaineHistoria;
    private int vettaJuotuTanaan;

    public User() {
        this.weightHistory = new ArrayList<>();
        this.alapaineHistoria = new ArrayList<>();
        this.ylapaineHistoria = new ArrayList<>();
        this.vettaJuotuTanaan = 0;
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

    public int getWeightNow() {
        if (weightHistory.size() == 0) {
            return 0;
        }
        return weightHistory.get(weightHistory.size() - 1);
    }

    public ArrayList<Integer> getWeightHistory() {
        return this.weightHistory;
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

    public void vettaJuotuReset() {
        this.vettaJuotuTanaan = 0;
    }

    public void juoVetta(int dl) {
        this.vettaJuotuTanaan += dl;
    }

    public int getJuotuVesi() {
        return this.vettaJuotuTanaan;
    }

}
