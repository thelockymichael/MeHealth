package com.mehealth.User;

import java.util.ArrayList;
import java.util.Date;

/**
 * Keeps track of the user's weight history.
 */
public class Weight {
    private ArrayList<WeightValue> weightHistory;

    public Weight() {
        weightHistory = new ArrayList<>();
    }

    /**
     * Getter for latest value in weight history
     * @return  an int from the weight list's latest value
     */
    public int getLatestWeight() {
        if (weightHistory.size() == 0) {
            return 0;
        }
        return weightHistory.get(weightHistory.size() - 1).getWeight();
    }

    public ArrayList<WeightValue> getWeightHistory() {
        return weightHistory;
    }

    public void addWeightRecord(int weight, Date date) {
        WeightValue weightValue = new WeightValue(weight, date);

        if (weight < 1000 && !listContainsDate(date)) {
            weightHistory.add(weightValue);
        }
    }

    public void clear() {
        weightHistory.clear();
    }

    /*public double getBMI(double height) {
        double weight = 1.0 * weightHistory.get(weightHistory.size() - 1);
        return (weight / height / height) * 10000;
    }*/

    public void deleteRecord(int record) {
        if (weightHistory.size() > 0) {
            weightHistory.remove(record);
        }
    }

    public boolean latestWeightLower() {
        if (weightHistory.size() < 2) {
            return true;
        }
        return weightHistory.get(weightHistory.size() - 1).getWeight() < weightHistory.get(weightHistory.size() - 2).getWeight();
    }

    public boolean weightNotChanged() {
        if (weightHistory.size() < 2) {
            return true;
        }
        return weightHistory.get(weightHistory.size() - 1).equals(weightHistory.get(weightHistory.size() - 2));
    }

    public boolean listContainsDate(Date date) {
        for (int i = 0; i < weightHistory.size(); i++) {
            if (weightHistory.get(i).containsDate(date)) {
                return true;
            }
        }
        return false;
    }
}
