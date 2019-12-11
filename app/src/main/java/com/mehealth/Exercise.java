package com.mehealth;

import androidx.annotation.NonNull;

import com.mehealth.User.User;

/**
 * Class for defining an exercise.
 * @author Otso Säävälä
 */
public class Exercise {
    private String nimi;
    private double kaloritMinuutissaPerKilo;

    /**
     * Define the exercise and how many calories it burns in a minute per kilo.
     * @param nimi Name of the exercise.
     * @param kaloritMinuutissaPerKilo How many calories the exercise burns per kilo.
     */
        public Exercise(String nimi, double kaloritMinuutissaPerKilo) {
        this.nimi = nimi;
        this.kaloritMinuutissaPerKilo = kaloritMinuutissaPerKilo;
    }

    /**
     *
     * @return The name of the exercise.
     */
    public String getNimi() {
        return nimi;
    }

    /**
     *
     * @return The calories the exericse burns in a minute per kilo.
     */
    public double getCaloriesInAMinutePerKilo() {
        return kaloritMinuutissaPerKilo;
    }

    /**
     *
     * @param user User to get the user's weight.
     * @return How many calories the exercise burns in an hour per kilo.
     */
    public int getCaloriesInAnHourPerKilo(User user) {
        double weight = user.weight.getLatestWeight();
        double dCaloriesBurned = getCaloriesInAMinutePerKilo() * (1.0* 60) * weight;
        int iCaloriesBurned = (int) Math.round(dCaloriesBurned);

        return iCaloriesBurned;
    }

    @NonNull
    @Override
    public String toString() {
        return this.nimi;
    }
}
