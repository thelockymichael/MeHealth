package com.mehealth.user.values;

import androidx.annotation.NonNull;

import com.mehealth.user.User;

/**
 * Class for defining an exercise.
 * @author Otso Säävälä
 */
public class Exercise {
    private String name;
    private double caloriesInAMinutePerKilo;

    /**
     * Define the exercise and how many calories it burns in a minute per kilo.
     * @param name Name of the exercise.
     * @param caloriesInAMinutePerKilo How many calories the exercise burns per kilo.
     */
    public Exercise(String name, double caloriesInAMinutePerKilo) {
        this.name = name;
        this.caloriesInAMinutePerKilo = caloriesInAMinutePerKilo;
    }

    /**
     *
     * @return The name of the exercise.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return The calories the exericse burns in a minute per kilo.
     */
    public double getCaloriesInAMinutePerKilo() {
        return caloriesInAMinutePerKilo;
    }

    /**
     *
     * @param user User to get the user's weight.
     * @return How many calories the exercise burns in an hour per kilo.
     */
    public int getCaloriesInAnHourPerKilo(User user) {
        double weight = user.weight.getLatestWeight();
        double dCaloriesBurned = getCaloriesInAMinutePerKilo() * (1.0* 60) * weight;

        return (int) Math.round(dCaloriesBurned);
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
