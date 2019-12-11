package com.mehealth.user.dailytracking;

/**
 * For storing systolic and diastolic blood pressure values
 * @author Hannu Rytöniemi
 */
public class HighBloodPressure {
    private int ika;
    private int systolic;
    private int diastolic;

    /**
     * Add blood pressure value. Used to check if bloodpressure is too high.
     * @param ika sets age for arraylist.
     * @param systolic sets systoic pressure for arraylist.
     * @param diastolic sets diastolic pressure for arraylist.
     */
    public HighBloodPressure(int ika, int systolic, int diastolic) {
        this.ika = ika;
        this.systolic = systolic;
        this.diastolic = diastolic;
    }

    /**
     * @return The normal systolic value based on user age.
     */
    public int getNormalSystolic() {
        return systolic;
    }


    /**
     *
     * @return The normal diastolic value based on user age.
     */
    public int getNormalDiastolic() {
        return diastolic;
    }

}
