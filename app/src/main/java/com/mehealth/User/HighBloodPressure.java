package com.mehealth.User;

/**for storing systolic and diastolic blood pressure values
 *
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

    /** return object
     *
     * @return bloodpresurre values.
     */
    public int getNormalSystolic() {
        return systolic;
    }

    public int getNormalDiastolic() {
        return diastolic;
    }

}
