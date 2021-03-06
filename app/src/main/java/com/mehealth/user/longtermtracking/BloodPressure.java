package com.mehealth.user.longtermtracking;

import com.mehealth.user.values.BloodPressureValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Keeps track of the user's blood pressure history.
 * @author Amin Karaoui
 */
public class BloodPressure {
    private ArrayList<BloodPressureValue> lowerBPHistory;
    private ArrayList<BloodPressureValue> upperBPHistory;

    /**
     * Initializes both the lower and upper blood pressure lists.
     */
    public BloodPressure() {
        this.lowerBPHistory = new ArrayList<>();
        this.upperBPHistory = new ArrayList<>();
    }

    /**
     *
     * @return The lower blood pressure list.
     */
    public ArrayList<BloodPressureValue> getLowerBPHistory() {
        return lowerBPHistory;
    }

    /**
     *
     * @return The upper blood pressure list.
     */
    public ArrayList<BloodPressureValue> getUpperBPHistory() {
        return upperBPHistory;
    }

    /**
     *
     * @return The latest lower blood pressure value in the list.
     */
    public int getLatestLowerBP() {
        if (lowerBPHistory.size() == 0) {
            return 0;
        }
        return lowerBPHistory.get(lowerBPHistory.size() - 1).getBloodPressure();
    }

    /**
     *
     * @return The latest upper blood pressure value in the list.
     */
    public int getLatestUpperBP() {
        if (upperBPHistory.size() == 0) {
            return 0;
        }
        return upperBPHistory.get(upperBPHistory.size() - 1).getBloodPressure();
    }

    /**
     *
     * @param date Float of date given, e.g. date.getTime()
     * @return A lower bp value corresponding to the time of date given.
     */
    public int getLowerBPByDate(float date) {
        for (int i = 0; i < lowerBPHistory.size(); i++) {
            if (date == lowerBPHistory.get(i).getDate().getTime()) {
                return lowerBPHistory.get(i).getBloodPressure();
            }
        }
        return 0;
    }

    /**
     *
     * @param date Float of date given, e.g. date.getTime()
     * @return An upper bp value corresponding to the time of date given.
     */
    public int getUpperBPByDate(float date) {
        for (int i = 0; i < upperBPHistory.size(); i++) {
            if (date == upperBPHistory.get(i).getDate().getTime()) {
                return upperBPHistory.get(i).getBloodPressure();
            }
        }
        return 0;
    }

    /**
     * Add a record to the lower blood pressure list.
     * @param lowerBloodPressure Value of the blood pressure.
     * @param date  Date of the value.
     */
    public void addLowerBPRecord(int lowerBloodPressure, Date date) {
        BloodPressureValue bloodPressureValue = new BloodPressureValue(lowerBloodPressure, date);

        if (lowerBloodPressure < 1000 && lowerBloodPressure >= 1 && !isDateInList(date, lowerBPHistory)) {
            this.lowerBPHistory.add(bloodPressureValue);
        }
    }

    /**
     * Add a record to the upper blood pressur list.
     * @param upperBloodPressure Value of the blood pressure.
     * @param date Date of the value.
     */
    public void addUpperBPRecord(int upperBloodPressure, Date date) {
        BloodPressureValue bloodPressureValue = new BloodPressureValue(upperBloodPressure, date);

        if (upperBloodPressure < 1000 && upperBloodPressure >= 1 && !isDateInList(date, upperBPHistory)) {
            this.upperBPHistory.add(bloodPressureValue);
        }
    }

    /**
     * Return boolean checking if the latest value in the list is lower than the one before it.
     * @param bp uower or upper depending on if you want to check lower or upper blood pressure list.
     * @return True if the latest record is lower than the one before or if there are less than 2 values in the list.
     */
    public boolean isLatestBPLower(String bp) {
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

    /**
     * Return boolean checking if the latest blood pressure value is different from the one before it.
     * @param bp lower or upper depending on if you want to check lower or upper blood pressure list.
     * @return True if value has not changed or if there are less than 2 values in the list.
     */
    public boolean isBPNotChanged(String bp) {
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

    /**
     * Checks whether a given blood pressure list contains a value with the given date.
     * @param date Date to check against.
     * @param list List the check on.
     * @return True if the list contains a value with the given date.
     */
    public boolean isDateInList(Date date, ArrayList<BloodPressureValue> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).containsDate(date)) {
                return true;
            }
        }
        return false;
    }

    public boolean isDateInBothLists(Date date) {
        for (int i = 0; i < lowerBPHistory.size(); i++) {
            if (lowerBPHistory.get(i).containsDate(date)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Clear the lower blood pressure history list.
     */
    public void clearLowerBP() {
        lowerBPHistory.clear();
    }

    /**
     * Clear the upper blood pressure history list.
     */
    public void clearUpperBP() {
        upperBPHistory.clear();
    }

    /**
     * Remove a record on the given date.
     * @param date Float value of the Date object, can be accessed with date.getTime()
     */
    public void removeBPByDate(float date) {
        for (int i = 0; i < lowerBPHistory.size(); i++) {
            if (lowerBPHistory.get(i).getDate().getTime() == date) {
                lowerBPHistory.remove(i);
                upperBPHistory.remove(i);
            }
        }
    }

    /**
     * Sorts both lists by date
     */
    public void sortListsByDate() {
        //Sort the lower BP list by date
        Collections.sort(lowerBPHistory, new Comparator<BloodPressureValue>() {
            @Override
            public int compare(BloodPressureValue o1, BloodPressureValue o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        } );

        //Sort the upper BP list by date
        Collections.sort(upperBPHistory, new Comparator<BloodPressureValue>() {
            @Override
            public int compare(BloodPressureValue o1, BloodPressureValue o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        } );
    }

}
