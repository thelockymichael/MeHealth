package com.mehealth.user.longtermtracking;

import com.mehealth.user.values.MoodValue;

import java.util.ArrayList;
import java.util.Date;

/**
 * Keeps track of the user's mood history
 * @author Amin Karaoui
 */
public class Mood {
    private ArrayList<MoodValue> moodHistory;

    public Mood() {
        moodHistory = new ArrayList<>();
        }

    /**
     * Get latest value in mood history.
     * @return And int from the mood list's latest value.
     */
    public int getLatestMood() {
        if (moodHistory.size() == 0) {
            return 0;
        }
        return moodHistory.get(moodHistory.size() - 1).getMood();
    }

    /**
     *
     * @return Mood history arraylist.
     */
    public ArrayList<MoodValue> getMoodHistory() {
        return moodHistory;
    }

    /**
     *
     * @param date Float of date given, e.g. date.getTime()
     * @return A mood value corresponding to the time of date given.
     */
    public int getMoodByDate(float date) {
        for (int i = 0; i < moodHistory.size(); i++) {
            if (moodHistory.get(i).getDate().getTime() == date) {
                return moodHistory.get(i).getMood();
            }
        }
        return 11;
    }

    /**
     * Add a mood record.
     * @param mood Mood as int.
     * @param date Date of the record.
     */
    public void addMoodRecord(int mood, Date date) {
        MoodValue moodValue = new MoodValue(mood, date);
        if (!isDateInList(date)) {
            this.moodHistory.add(moodValue);
        }
    }

    /**
     * Check if the list contains a record with the given date.
     * @param date The given date.
     * @return True if the list contains a record with the given date.
     */
    public boolean isDateInList(Date date) {
        for (int i = 0; i < moodHistory.size(); i++) {
            if (moodHistory.get(i).isDateEqual(date)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Clear the mood history arraylist.
     */
    public void clear() {
        this.moodHistory.clear();
    }

    /**
     * Remove a record on the given date.
     * @param date Float value of the Date object, can be accessed with date.getTime()
     */
    public void removeMoodByDate(float date) {
        for (int i = 0; i < moodHistory.size(); i++) {
            if (moodHistory.get(i).getDate().getTime() == date) {
                moodHistory.remove(i);
            }
        }
    }
}
