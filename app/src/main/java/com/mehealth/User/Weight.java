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
     * Getter for latest value in weight history.
     * @return  An int from the weight list's latest value.
     */
    public int getLatestWeight() {
        if (weightHistory.size() == 0) {
            return 0;
        }
        return weightHistory.get(weightHistory.size() - 1).getWeight();
    }

    /**
     *
     * @return Weighthistory arraylist.
     */
    public ArrayList<WeightValue> getWeightHistory() {
        return weightHistory;
    }

    /**
     * Add a weight record.
     * @param weight Weight as int.
     * @param date Date of the record.
     */
    public void addWeightRecord(int weight, Date date) {
        WeightValue weightValue = new WeightValue(weight, date);

        if (weight < 1000 && listDoesNotContainsDate(date)) {
            weightHistory.add(weightValue);
        }
    }

    /**
     * Clear the weight history arraylist.
     */
    public void clear() {
        weightHistory.clear();
    }

    /*public double getBMI(double height) {
        double weight = 1.0 * weightHistory.get(weightHistory.size() - 1);
        return (weight / height / height) * 10000;
    }*/

    /**
     * Delete a specific record from the weight history list.
     * @param record Index of the record.
     */
    public void deleteRecord(int record) {
        if (weightHistory.size() > 0) {
            weightHistory.remove(record);
        }
    }

    /**
     * Check if the latest added record is lower than the previous one.
     * @return True if newest weight is lower.
     */
    public boolean latestWeightLower() {
        if (weightHistory.size() < 2) {
            return true;
        }
        return weightHistory.get(weightHistory.size() - 1).getWeight() < weightHistory.get(weightHistory.size() - 2).getWeight();
    }

    /**
     * Check if the latest added record is the same as the previous one.
     * @return True if newest weight is the same or the list has less than 2 records.
     */
    public boolean weightNotChanged() {
        if (weightHistory.size() < 2) {
            return true;
        }
        return weightHistory.get(weightHistory.size() - 1).equals(weightHistory.get(weightHistory.size() - 2));
    }

    /**
     * Check if the list contains a record with the given date.
     * @param date The given date.
     * @return True if the list contains a record with the given date.
     */
    public boolean listDoesNotContainsDate(Date date) {
        for (int i = 0; i < weightHistory.size(); i++) {
            if (weightHistory.get(i).containsDate(date)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Remove a record on the given date.
     * @param date Float value of the Date object, can be accessed with date.getTime()
     */
    public void removeWeightByDate(float date) {
        for (int i = 0; i < weightHistory.size(); i++) {
            if (weightHistory.get(i).getDate().getTime() == date) {
                weightHistory.remove(i);
            }
        }
    }
}
