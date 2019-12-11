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
     * Add a mood record.
     * @param mood Mood as int.
     * @param date Date of the record.
     */
    public void addMoodRecord(int mood, Date date) {
        MoodValue moodValue = new MoodValue(mood, date);
        if (!listContainsDate(date)) {
            this.moodHistory.add(moodValue);
        }
    }

    /**
     *
     * @return Mood history arraylist.
     */
    public ArrayList<MoodValue> getMoodHistory() {
        return moodHistory;
    }

    /**
     * Clear the mood history arraylist.
     */
    public void clear() {
        this.moodHistory.clear();
    }

    /**
     * Check if the list contains a record with the given date.
     * @param date The given date.
     * @return True if the list contains a record with the given date.
     */
    private boolean listContainsDate(Date date) {
        for (int i = 0; i < moodHistory.size(); i++) {
            if (moodHistory.get(i).containsDate(date)) {
                return true;
            }
        }
        return false;
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

    public int getMoodByDate(float date) {
        for (int i = 0; i < moodHistory.size(); i++) {
            if (moodHistory.get(i).getDate().getTime() == date) {
                return moodHistory.get(i).getMood();
            }
        }
        return 11;
    }

    public boolean listDoesNotContainsDate(Date date) {
        for (int i = 0; i < moodHistory.size(); i++) {
            if (moodHistory.get(i).containsDate(date)) {
                return false;
            }
        }
        return true;
    }
}
