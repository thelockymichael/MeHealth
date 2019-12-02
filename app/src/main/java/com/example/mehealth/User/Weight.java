package com.example.mehealth.User;

import java.util.ArrayList;

/**
 * Keeps track of the user's weight history.
 */
public class Weight {
    private ArrayList<Integer> weightHistory;

    Weight() {
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
        return weightHistory.get(weightHistory.size() - 1);
    }

    public ArrayList<Integer> getWeightHistoryList() {
        return this.weightHistory;
    }

    public void addWeightRecord(int weight) {
        this.weightHistory.add(weight);
    }

    public void resetWeightHistory() {
        this.weightHistory.clear();
    }

    void clear() {
        weightHistory.clear();
    }

    public double getBMI(double height) {
        double weight = 1.0 * weightHistory.get(weightHistory.size() - 1);
        return (weight / height / height) * 10000;
    }
}
