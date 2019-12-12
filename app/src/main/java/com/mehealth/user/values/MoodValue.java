package com.mehealth.user.values;

import java.util.Date;

/**
 * For storing a single mood value.
 * @author Amin Karaoui
 */
public class MoodValue {
    private int mood;
    private Date date;

    /**
     * Add a mood value.
     * @param mood Value of the mood.
     * @param date Date added.
     */
    public MoodValue(int mood, Date date) {
        this.mood = mood;
        this.date = date;
    }

    /**
     * @return Int value of the mood.
     */
    public int getMood() {
        return mood;
    }

    /**
     * @return The date of the object.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Check if the date of this object is equal to given date.
     * @param date Given date.
     * @return True if same date.
     */
    public boolean isDateEqual(Date date) {
        return date.equals(this.date);
    }
}
