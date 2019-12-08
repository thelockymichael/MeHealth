package com.mehealth.User;

import java.util.Date;
import java.util.Objects;

/**
 * For storing a single mood value.
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
     *
     * @return Mood value of the object.
     */
    public int getMood() {
        return mood;
    }

    /**
     *
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
    public boolean containsDate(Date date) {
        return date.equals(this.date);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoodValue moodValue = (MoodValue) o;
        return mood == moodValue.mood &&
                Objects.equals(date, moodValue.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mood, date);
    }
}
