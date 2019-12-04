package com.mehealth.User;

import android.annotation.SuppressLint;

import com.mehealth.Exercise;
import com.mehealth.SharedPref;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ExercisedToday {
    private int caloriesBurnedToday;

    public ExercisedToday() {
        this.caloriesBurnedToday = 0;
    }

    public void addExercise(Exercise exercise, int minutes, User user) {
        double dCaloriesBurned = exercise.getKaloritMinuutissaPerKilo() * (1.0* minutes) * (1.0 * user.weight.getLatestWeight());

        int iCaloriesBurned = (int) Math.round(dCaloriesBurned);
        this.caloriesBurnedToday += iCaloriesBurned;
    }

    public void clear() {
        this.caloriesBurnedToday = 0;
    }

    public void checkCalories(User user, SharedPref pref) {
        //Get the current date
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = dateFormat.format(date);

        //Get the previous date from shared preferences
        String oldFormattedDate = pref.getString("oldDate");

        //If the current date differs from the old date, calories burned today is set to 0
        if (!formattedDate.equals(oldFormattedDate)) {
            clear();
        }
        //Saves the current date into shared preferences as the old date
        oldFormattedDate = formattedDate;
        pref.putString("oldDate", oldFormattedDate);
    }

    public int getCaloriesBurnedToday() {
        return caloriesBurnedToday;
    }
}
