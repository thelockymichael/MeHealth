package com.mehealth.User;

public class HighBloodPressure {
    private int ika;
    private int systolic;
    private int diastolic;

    public HighBloodPressure(int ika, int systolic, int diastolic) {
        this.ika = ika;
        this.systolic = systolic;
        this.diastolic = diastolic;
    }

    public int getIka() {
        return ika;
    }

    public int getNormalSystolic() {
        return systolic;
    }

    public int getNormalDiastolic() {
        return diastolic;
    }

}
