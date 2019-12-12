package com.mehealth.user.values;

import java.util.Date;

/**
 *
 * For storing a single blood pressure value. Contains date added and the actual blood pressure value.
 * @author Amin Karaoui
 */
public class BloodPressureValue {
    private int bloodPressure;
    private Date date;

    /**
     * Add a blood pressure value.
     * @param bloodPressure Value of the blood pressure.
     * @param date  Date added.
     */
    public BloodPressureValue(int bloodPressure, Date date) {
        this.bloodPressure = bloodPressure;
        this.date = date;
    }

    /**
     * @return The object.
     */
    public int getBloodPressure() {
        return bloodPressure;
    }

    /**
     * @return The date of the object.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Check if date of this object is equal to given date.
     * @param date  Given date.
     * @return  True if same date.
     */
    public boolean containsDate(Date date) {
        return date.equals(this.date);
    }
}
