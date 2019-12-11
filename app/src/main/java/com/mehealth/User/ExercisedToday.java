package com.mehealth.User;

import com.mehealth.DateCheck;
import com.mehealth.Exercise;
import com.mehealth.SharedPref;

/**
 *
 * Class to keep track of the calories burned by the user today.
 * @author Amin Karaoui
 */
public class ExercisedToday {
    private int caloriesBurnedToday;
    private DateCheck dateCheck;

    /**
     * Initializes with 0 calories burned today and a DateChecker.
     */
    public ExercisedToday() {
        caloriesBurnedToday = 0;
        dateCheck = new DateCheck();
    }

    /**
     * Add exercise performed.
     * @param exercise The exercise that was done.
     * @param minutes How many minutes were exercised.
     * @param user The user exercising.
     * @param intensity The intensity of the exercise.
     */
    public void addExercise(Exercise exercise, int minutes, User user, double intensity) {
        double weight = user.weight.getLatestWeight();
        double dCaloriesBurned = exercise.getCaloriesInAMinutePerKilo() * (1.0* minutes) * weight * intensity;

        int iCaloriesBurned = (int) Math.round(dCaloriesBurned);
        this.caloriesBurnedToday += iCaloriesBurned;
    }

    /**
     * Sets the calories burned today back to 0.
     */
    public void clear() {
        this.caloriesBurnedToday = 0;
    }

    /**
     * Clears list if a the date has changed.
     * @param pref SharedPref that keeps track of the shared preferences.
     */
    public void checkCalories(SharedPref pref) {
        if (dateCheck.newDay(pref, "oldDateExercise")) {
            clear();
        }

    }

    /**
     *
     * @return How many calories have been burned today.
     */
    public int getCaloriesBurnedToday() {
        return caloriesBurnedToday;
    }
}
