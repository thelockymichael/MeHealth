package com.mehealth.User;

import java.util.ArrayList;

/**
 * Keeps track of the user's mood history
 */
public class Mood {
    private ArrayList<Integer> moodHistory;

    public Mood() {
        moodHistory = new ArrayList<>();
    }

    public int getLatestMoodRecord() {
        if (moodHistory.size() == 0) {
            return 0;
        }
        return moodHistory.get(moodHistory.size() - 1);
    }

    public ArrayList<Integer> getMoodHistory() {
        return moodHistory;
    }

    public void addMoodRecord(int mood) {
        this.moodHistory.add(mood);
    }

    public void clear() {
        this.moodHistory.clear();
    }
}
