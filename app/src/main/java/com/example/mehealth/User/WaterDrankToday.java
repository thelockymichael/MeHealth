package com.example.mehealth.User;

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
    public int getWaterDrankToday() {
        return this.waterDrankToday;
    }

    public void waterDrankTodayReset() {
        this.waterDrankToday = 0;
    }

    public void clear() {
        waterDrankToday = 0;
    }

}
