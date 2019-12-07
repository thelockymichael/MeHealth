package com.mehealth.User;

import java.util.Date;
import java.util.Objects;

public class MoodValue {
    int mood;
    Date date;

    public MoodValue(int mood, Date date) {
        this.mood = mood;
        this.date = date;
    }

    public int getMood() {
        return mood;
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
        MoodValue moodValue = (MoodValue) o;
        return mood == moodValue.mood &&
                Objects.equals(date, moodValue.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mood, date);
    }
}
