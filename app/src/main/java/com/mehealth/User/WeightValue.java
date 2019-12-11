package com.mehealth.User;

import java.util.Date;

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
     * @return Weight of the object.
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
}
