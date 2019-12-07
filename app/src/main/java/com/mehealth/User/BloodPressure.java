package com.mehealth.User;

import java.util.ArrayList;
import java.util.Date;

/**
 * Keeps track of the user's blood pressure history.
 */
public class BloodPressure {
    private ArrayList<BloodPressureValue> lowerBPHistory;
    private ArrayList<BloodPressureValue> upperBPHistory;

    public BloodPressure() {
        this.lowerBPHistory = new ArrayList<>();
        this.upperBPHistory = new ArrayList<>();
    }

    public ArrayList<BloodPressureValue> getLowerBPHistory() {
        return lowerBPHistory;
    }

    public ArrayList<BloodPressureValue> getUpperBPHistory() {
        return upperBPHistory;
    }

    public int getLatestLowerBP() {
        if (lowerBPHistory.size() == 0) {
            return 0;
        }
        return lowerBPHistory.get(lowerBPHistory.size() - 1).getBloodPressure();
    }

    public int getLatestUpperBP() {
        if (upperBPHistory.size() == 0) {
            return 0;
        }
        return upperBPHistory.get(upperBPHistory.size() - 1).getBloodPressure();
    }

    public void addLowerBPRecord(int lowerBloodPressure, Date date) {
        BloodPressureValue bloodPressureValue = new BloodPressureValue(lowerBloodPressure, date);

        if (lowerBloodPressure < 1000 && !listContainsDate(date, lowerBPHistory)) {
            this.lowerBPHistory.add(bloodPressureValue);
        }
    }

    public void addUpperBPRecord(int upperBloodPressure, Date date) {
        BloodPressureValue bloodPressureValue = new BloodPressureValue(upperBloodPressure, date);

        if (upperBloodPressure < 1000 && !listContainsDate(date, upperBPHistory)) {
            this.upperBPHistory.add(bloodPressureValue);
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
            return lowerBPHistory.get(lowerBPHistory.size() - 1).getBloodPressure() < lowerBPHistory.get(lowerBPHistory.size() - 2).getBloodPressure();
        } else if (bp.equals("upper")) {
            if (upperBPHistory.size() < 2) {
                return true;
            }
            return upperBPHistory.get(upperBPHistory.size() - 1).getBloodPressure() < upperBPHistory.get(upperBPHistory.size() - 2).getBloodPressure();
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

    public boolean listContainsDate(Date date, ArrayList<BloodPressureValue> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).containsDate(date)) {
                return true;
            }
        }
        return false;
    }

}
