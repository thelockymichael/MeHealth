package com.mehealth;

import androidx.annotation.NonNull;

public class Exercise {
    private String nimi;
    private double kaloritTunnissaPerKilo;

    public Exercise(String nimi, double kaloritTunnissaPerKilo) {
        this.nimi = nimi;
        this.kaloritTunnissaPerKilo = kaloritTunnissaPerKilo;
    }

    public String getNimi() {
        return nimi;
    }

    public double getKaloritTunnissa() {
        return kaloritTunnissaPerKilo;
    }

    @NonNull
    @Override
    public String toString() {
        return this.nimi;
    }
}
