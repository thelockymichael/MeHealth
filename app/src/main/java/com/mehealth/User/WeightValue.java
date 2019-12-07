package com.mehealth.User;

import java.util.Date;
import java.util.Objects;

/**
 * For storing a single weight value.
 */
public class WeightValue {
    private int weight;
    private Date date;

    /**
     * Add a weight value.
     * @param weight Value of the weight.
     * @param date Date added.
     */
    public WeightValue(int weight, Date date) {
        this.weight = weight;
        this.date = date;
    }

    /**
     *
     * @return The object.
     */
    public int getWeight() {
        return weight;
    }

    /**
     *
     * @return The date of the object.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Check if date of this object is equal to given date.
     * @param date Given date.
     * @return True if same date.
     */
    public boolean containsDate(Date date) {
        return date.equals(this.date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeightValue that = (WeightValue) o;
        return weight == that.weight &&
                Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(weight, date);
    }
}
