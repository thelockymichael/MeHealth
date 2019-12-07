package com.mehealth.User;

import java.util.Date;
import java.util.Objects;

/**
 * For storing a single blood pressure value. Contains date added and the actual blood pressure value.
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
     *
     * @return the object.
     */
    public int getBloodPressure() {
        return bloodPressure;
    }

    /**
     *
     * @return the date of the object.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Check if date of this object is equal to given date.
     * @param date  given date.
     * @return  true if same date.
     */
    public boolean containsDate(Date date) {
        return date.equals(this.date);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BloodPressureValue that = (BloodPressureValue) o;
        return bloodPressure == that.bloodPressure &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bloodPressure, date);
    }
}
