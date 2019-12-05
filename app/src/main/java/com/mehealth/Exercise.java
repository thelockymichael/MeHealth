package com.mehealth;

import androidx.annotation.NonNull;

import com.mehealth.User.User;

public class Exercise {
    private String nimi;
    private double kaloritMinuutissaPerKilo;

    public Exercise(String nimi, double kaloritMinuutissaPerKilo) {
        this.nimi = nimi;
        this.kaloritMinuutissaPerKilo = kaloritMinuutissaPerKilo;
    }

    public String getNimi() {
        return nimi;
    }

    public double getKaloritMinuutissaPerKilo() {
        return kaloritMinuutissaPerKilo;
    }

    public int getKaloritTunnissa(User user) {
        double weight = user.weight.getLatestWeight();
        double dCaloriesBurned = getKaloritMinuutissaPerKilo() * (1.0* 60) * weight;
        int iCaloriesBurned = (int) Math.round(dCaloriesBurned);

        return iCaloriesBurned;
    }

    @NonNull
    @Override
    public String toString() {
        return this.nimi;
    }
}
