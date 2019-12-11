package com.mehealth.User;

/**
 * Keeps track of the user's information including the water drank daily,
 * calories burned daily, weight history, blood pressure history, and mood history.
 * @author Amin Karaoui
 */
public class User  {
    public WaterDrankToday water;
    public Mood mood;
    public BloodPressure bloodPressure;
    public Weight weight;
    public ExercisedToday exercisedToday;

    /**
     * Creates new user with default values of 0 in everything.
     */
    public User() {
        weight = new Weight();
        water = new WaterDrankToday();
        mood = new Mood();
        bloodPressure = new BloodPressure();
        exercisedToday = new ExercisedToday();
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
        exercisedToday.clear();
    }

}
