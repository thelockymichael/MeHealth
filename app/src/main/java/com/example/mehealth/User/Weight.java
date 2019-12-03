package com.example.mehealth.User;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Keeps track of the user's weight history.
 */
public class Weight {
    private ArrayList<Integer> weightHistory;

    public Weight() {
        weightHistory = new ArrayList<Integer>();
    }

    /**
     * Getter for latest value in weight history
     * @return  an int from the weight list's latest value
     */
    public int getLatestWeight() {
        if (weightHistory.size() == 0) {
            return 0;
        }
        return weightHistory.get(weightHistory.size() - 1);
    }

    public ArrayList<Integer> getWeightHistoryList() {
        return this.weightHistory;
    }

    public void addWeightRecord(int weight) {
        if (weight < 1000) {
            this.weightHistory.add(weight);
        }
    }

    public void clear() {
        weightHistory.clear();
    }

    public double getBMI(double height) {
        double weight = 1.0 * weightHistory.get(weightHistory.size() - 1);
        return (weight / height / height) * 10000;
    }

    public void deleteRecord(int record) {
        if (weightHistory.size() > 0) {
            weightHistory.remove(record);
        }
    }

    public int highestWeightRecord() {
        return Collections.max(this.weightHistory);
    }

    public boolean latestWeightLower() {
        if (weightHistory.size() < 2) {
            return true;
        }
        return weightHistory.get(weightHistory.size() - 1) < weightHistory.get(weightHistory.size() - 2);
    }

    public boolean weightNotChanged() {
        if (weightHistory.size() < 2) {
            return true;
        }
        return weightHistory.get(weightHistory.size() - 1) == weightHistory.get(weightHistory.size() - 2);
    }
}
