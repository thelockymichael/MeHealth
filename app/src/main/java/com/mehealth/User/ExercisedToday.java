package com.mehealth.User;

import com.mehealth.DateCheck;
import com.mehealth.Exercise;
import com.mehealth.SharedPref;

public class ExercisedToday {
    private int caloriesBurnedToday;
    private DateCheck dateCheck;

    public ExercisedToday() {
        this.caloriesBurnedToday = 0;
        dateCheck = new DateCheck();
    }

    public void addExercise(Exercise exercise, int minutes, User user) {
        double dCaloriesBurned = exercise.getKaloritMinuutissaPerKilo() * (1.0* minutes) * (1.0 * user.weight.getLatestWeight());

        int iCaloriesBurned = (int) Math.round(dCaloriesBurned);
        this.caloriesBurnedToday += iCaloriesBurned;
    }

    public void clear() {
        this.caloriesBurnedToday = 0;
    }

    public void checkCalories(SharedPref pref) {
        if (dateCheck.newDay(pref, "oldDateExercise")) {
            clear();
        }

    }

    public int getCaloriesBurnedToday() {
        return caloriesBurnedToday;
    }
}
