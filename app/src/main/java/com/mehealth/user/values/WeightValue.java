package com.mehealth.user.values;

import java.util.Date;

/**
 * For storing a single weight value.
 * @author Amin Karaoui
 */
public class WeightValue {
    private double weight;
    private Date date;

    /**
     * Add a weight value.
     * @param weight Value of the weight.
     * @param date Date added.
     */
    public WeightValue(double weight, Date date) {
        this.weight = weight;
        this.date = date;
    }

    /**
     *
     * @return Weight of the object.
     */
    public double getWeight() {
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
    public boolean isDateEqual(Date date) {
        return date.equals(this.date);
    }
}
