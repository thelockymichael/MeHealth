package com.example.mehealth.User;

import java.util.ArrayList;

public class BloodPressure {
    private ArrayList<Integer> lowerBloodPressureHistory;
    private ArrayList<Integer> upperBloodPressureHistory;

    public BloodPressure() {
        this.lowerBloodPressureHistory = new ArrayList<>();
        this.upperBloodPressureHistory = new ArrayList<>();
    }

    public int getLatestLowerBloodPressure() {
        if (lowerBloodPressureHistory.size() == 0) {
            return 0;
        }
        return lowerBloodPressureHistory.get(lowerBloodPressureHistory.size() - 1);
    }

    public int getLatestUpperBloodPressure() {
        if (upperBloodPressureHistory.size() == 0) {
            return 0;
        }
        return upperBloodPressureHistory.get(upperBloodPressureHistory.size() - 1);
    }

    public void addLowerBloodPressureRecord(int lowerBloodPressure) {
        this.lowerBloodPressureHistory.add(lowerBloodPressure);
    }

    public void addUpperBloodPressureRecord(int upperBloodPressure) {
        this.upperBloodPressureHistory.add(upperBloodPressure);
    }

    public void clearLowerBP() {
        lowerBloodPressureHistory.clear();
    }

    public void clearUpperBP() {
        upperBloodPressureHistory.clear();
    }
}
