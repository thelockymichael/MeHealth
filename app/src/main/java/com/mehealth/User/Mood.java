package com.mehealth.User;

import java.util.ArrayList;
import java.util.Date;

/**
 * Keeps track of the user's mood history
 */
public class Mood {
    private ArrayList<MoodValue> moodHistory;

    public Mood() {
        moodHistory = new ArrayList<>();
    }

    public int getLatestMood() {
        if (moodHistory.size() == 0) {
            return 0;
        }
        return moodHistory.get(moodHistory.size() - 1).getMood();
    }

    public void addMoodRecord(int mood, Date date) {
        MoodValue moodValue = new MoodValue(mood, date);
        if (!listContainsDate(date)) {
            this.moodHistory.add(moodValue);
        }
    }

    public ArrayList<MoodValue> getMoodHistory() {
        return moodHistory;
    }

    public void clear() {
        this.moodHistory.clear();
    }

    public boolean listContainsDate(Date date) {
        for (int i = 0; i < moodHistory.size(); i++) {
            if (moodHistory.get(i).containsDate(date)) {
                return true;
            }
        }
        return false;
    }

    public void removeMoodByDate(float date) {
        for (int i = 0; i < moodHistory.size(); i++) {
            if (moodHistory.get(i).getDate().getTime() == date) {
                moodHistory.remove(i);
            }
        }
    }
}
