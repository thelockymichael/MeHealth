package com.example.mehealth;

/**
 * Keeps track of the user's information including the water drank daily,
 * weight history and blood pressure history.
 */
public class User  {
    public WaterDrankToday water;
    public Mood mood;
    public BloodPressure bloodPressure;
    public Weight weight;

    public User() {
        weight = new Weight();
        water = new WaterDrankToday();
        mood = new Mood();
        bloodPressure = new BloodPressure();
    }
    /**
     * Resets every value in the user class that is being tracked
     */
    public void resetEverything() {
        weight.clear();
        bloodPressure.clearLowerBP();
        bloodPressure.clearUpperBP();
        water.clear();
        mood.clear();
    }

}
