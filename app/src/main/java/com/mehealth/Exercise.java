package com.mehealth;

import androidx.annotation.NonNull;

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

    @NonNull
    @Override
    public String toString() {
        return this.nimi;
    }
}
