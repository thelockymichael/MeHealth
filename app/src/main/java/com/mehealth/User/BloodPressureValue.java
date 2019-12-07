package com.mehealth.User;

import java.util.Date;
import java.util.Objects;

public class BloodPressureValue {
    private int bloodPressure;
    private Date date;

    public BloodPressureValue(int bloodPressure, Date date) {
        this.bloodPressure = bloodPressure;
        this.date = date;
    }

    public int getBloodPressure() {
        return bloodPressure;
    }

    public Date getDate() {
        return date;
    }

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
