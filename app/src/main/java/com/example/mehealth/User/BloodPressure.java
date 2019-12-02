package com.example.mehealth.User;

import java.util.ArrayList;

/**
 * Keeps track of the user's blood pressure history.
 */
public class BloodPressure {
    private ArrayList<Integer> lowerBPHistory;
    private ArrayList<Integer> upperBPHistory;

    public BloodPressure() {
        this.lowerBPHistory = new ArrayList<>();
        this.upperBPHistory = new ArrayList<>();
    }

    public int getLatestLowerBP() {
        if (lowerBPHistory.size() == 0) {
            return 0;
        }
        return lowerBPHistory.get(lowerBPHistory.size() - 1);
    }

    public int getLatestUpperBP() {
        if (upperBPHistory.size() == 0) {
            return 0;
        }
        return upperBPHistory.get(upperBPHistory.size() - 1);
    }

    public void addLowerBPRecord(int lowerBloodPressure) {
        this.lowerBPHistory.add(lowerBloodPressure);
    }

    public void addUpperBPRecord(int upperBloodPressure) {
        this.upperBPHistory.add(upperBloodPressure);
    }

    public void clearLowerBP() {
        lowerBPHistory.clear();
    }

    public void clearUpperBP() {
        upperBPHistory.clear();
    }
}
