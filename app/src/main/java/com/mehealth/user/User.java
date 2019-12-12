package com.mehealth.user;

import com.mehealth.user.dailytracking.ExercisedToday;
import com.mehealth.user.dailytracking.WaterDrankToday;
import com.mehealth.user.longtermtracking.BloodPressure;
import com.mehealth.user.longtermtracking.Mood;
import com.mehealth.user.longtermtracking.Weight;

/**
 * Keeps track of the user's information including the water drank daily,
 * calories burned daily, weight history, blood pressure history, and mood history.
 * @author Amin Karaoui
 */
public class User  {
    public Weight weight;
    public WaterDrankToday water;
    public Mood mood;
    public BloodPressure bloodPressure;
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
    public void clearEverything() {
        weight.clear();
        water.clear();
        mood.clear();
        bloodPressure.clearLowerBP();
        bloodPressure.clearUpperBP();
        exercisedToday.clear();
    }

}
