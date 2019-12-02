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

    public void waterDrankTodayReset() {
        this.waterDrankToday = 0;
    }

    public void clear() {
        waterDrankToday = 0;
    }

    public void checkWater(User user, SharedPref pref) {
        //Get the current date
        Date date = Calendar.getInstance().getTime();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = dateFormat.format(date);

        //Get the previous date from shared preferences
        String oldFormattedDate = pref.getString("oldDate");


        if (!formattedDate.equals(oldFormattedDate)) {
            user.water.waterDrankTodayReset();
        }
        oldFormattedDate = formattedDate;
        pref.putString("oldDate", oldFormattedDate);
    }

}
