package com.example.mehealth.User;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.mehealth.SharedPref;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Keeps track of the water drank by the user.
 */
public class WaterDrankToday {
    private int waterDrankToday;

    public WaterDrankToday() {
        waterDrankToday = 0;
    }

    /**
     * Adds the water drank to according to the parameter
     * @param dl    Amount of water to drink
     */
    public void drinkWater(int dl) {
        this.waterDrankToday += dl;
    }

    /**
     * Getter for water drank today
     * @return  an int of how much water has been drank today
     */
    public int getWaterDrankToday(User user, SharedPref pref) {
        checkWater(user, pref);
        return this.waterDrankToday;
    }

    public void clear() {
        waterDrankToday = 0;
    }

    /**
     * Checks whether a new day has come to reset the water drank.
     * If current date is different from the date saved before, water drank today is set to 0;
     * @param user
     * @param pref
     */
    public void checkWater(User user, SharedPref pref) {
        //Get the current date
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = dateFormat.format(date);

        //Get the previous date from shared preferences
        String oldFormattedDate = pref.getString("oldDate");

        //If the current date differs from the old date, water drank today is set to 0
        if (!formattedDate.equals(oldFormattedDate)) {
            clear();
        }
        //Saves the current date into shared preferences as the old date
        oldFormattedDate = formattedDate;
        pref.putString("oldDate", oldFormattedDate);
    }

}
