package com.example.mehealth;

import java.util.ArrayList;

public class User  {
    public ArrayList<Integer> weightHistory;
    private ArrayList<Integer> lowerBloodPressureHistory;
    private ArrayList<Integer> upperBloodPressureHistory;
    private ArrayList<Integer> moodHistory;
    private int waterDrankToday;

    public User() {
        this.weightHistory = new ArrayList<>();
        this.lowerBloodPressureHistory = new ArrayList<>();
        this.upperBloodPressureHistory = new ArrayList<>();
        this.moodHistory = new ArrayList<>();
        this.waterDrankToday = 0;
    }

    public void drinkWater(int dl) {
        this.waterDrankToday += dl;
    }



    public int getWaterDrankToday() {
        return this.waterDrankToday;
    }

    public int getLatestWeight() {
        if (weightHistory.size() == 0) {
            return 0;
        }
        return weightHistory.get(weightHistory.size() - 1);
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

    public int getLatestMoodRecord() {
        if (moodHistory.size() == 0) {
            return 0;
        }
        return moodHistory.get(moodHistory.size() - 1);
    }

    public ArrayList<Integer> getWeightHistoryList() {
        return this.weightHistory;
    }



    public void addWeightRecord(int weight) {
        this.weightHistory.add(weight);
    }

    public void addLowerBloodPressureRecord(int lowerBloodPressure) {
        this.lowerBloodPressureHistory.add(lowerBloodPressure);
    }

    public void addUpperBloodPressureRecord(int upperBloodPressure) {
        this.upperBloodPressureHistory.add(upperBloodPressure);
    }

    public void addMoodRecord(int mood) {
        this.moodHistory.add(mood);
    }



    public void waterDrankTodayReset() {
        this.waterDrankToday = 0;
    }

    public void resetWeightHistory() {
        this.weightHistory.clear();
    }

}
