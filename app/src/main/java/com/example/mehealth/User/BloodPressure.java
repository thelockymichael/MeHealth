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
        if (lowerBloodPressure < 1000) {
            this.lowerBPHistory.add(lowerBloodPressure);
        }
    }

    public void addUpperBPRecord(int upperBloodPressure) {
        if (upperBloodPressure < 1000) {
            this.upperBPHistory.add(upperBloodPressure);
        }
    }

    public void clearLowerBP() {
        lowerBPHistory.clear();
    }

    public void clearUpperBP() {
        upperBPHistory.clear();
    }

    public boolean latestBPLower(String bp) {
        if (bp.equals("lower")) {
            if (lowerBPHistory.size() < 2) {
                return true;
            }
            return lowerBPHistory.get(lowerBPHistory.size() - 1) < lowerBPHistory.get(lowerBPHistory.size() - 2);
        } else if (bp.equals("upper")) {
            if (upperBPHistory.size() < 2) {
                return true;
            }
            return upperBPHistory.get(upperBPHistory.size() - 1) < upperBPHistory.get(upperBPHistory.size() - 2);
        }
        return false;
    }

    public boolean bpNotChanged(String bp) {
        if (bp.equals("lower")) {
            if (lowerBPHistory.size() < 2) {
                return true;
            }
            return lowerBPHistory.get(lowerBPHistory.size() - 1) == lowerBPHistory.get(lowerBPHistory.size() - 2);
        } else if (bp.equals("upper")) {
            if (upperBPHistory.size() < 2) {
                return true;
            }
            return upperBPHistory.get(upperBPHistory.size() - 1) == upperBPHistory.get(upperBPHistory.size() - 2);
        }
        return false;
    }


}
