package com.mehealth.user.dailytracking;

import com.mehealth.utilities.DateCheck;
import com.mehealth.utilities.SharedPref;

/**
 * Keeps track of the water drank by the user.
 * @author Amin Karaoui
 */
public class WaterDrankToday {
    private int waterDrankToday;
    private DateCheck dateCheck;

    /**
     * Initialize with water drank today as 0.
     */
    public WaterDrankToday() {
        waterDrankToday = 0;
        dateCheck = new DateCheck();
    }

    /**
     * Getter for water drank today.
     * @return  an int of how much water has been drank today.
     */
    public int getWaterDrankToday(SharedPref pref) {
        checkWater(pref);
        return this.waterDrankToday;
    }

    /**
     *
     * @return How much water left to drink today in an integer.
     */
    public int getHowMuchWaterToDrink() {
        if (20 - this.waterDrankToday > 0) {
            return 20 - this.waterDrankToday;
        }
        return 0;
    }

    /**
     * Adds the water drank to according to the parameter.
     * @param dl    Amount of water to drink.
     */
    public void addWaterDrank(int dl) {
        this.waterDrankToday += dl;
    }

    /**
     * Set water drank today to 0.
     */
    public void clear() {
        waterDrankToday = 0;
    }

    /**
     * Checks whether a new day has come to reset the water drank.
     * If current date is different from the date saved before, water drank today is set to 0.
     * @param pref SharedPref in use.
     */
    public void checkWater(SharedPref pref) {
        if (dateCheck.newDay(pref, "oldDateWater")) {
            clear();
        }
    }

}
