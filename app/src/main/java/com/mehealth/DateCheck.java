package com.mehealth;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Class to check whether the day has changed.
 * @author Amin Karaoui
 */
public class DateCheck {

    public DateCheck() {
    }

    /**
     * Check whether the day has changed.
     * @param pref SharedPref that keeps track of the old date.
     * @param dateKey What key the date is saved as in the SharedPref
     * @return True if a new day has come.
     */
    public boolean newDay(SharedPref pref, String dateKey) {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = Calendar.getInstance().getTime();
        String formattedDate = dateFormat.format(date);
        String oldFormattedDate = pref.getString(dateKey, formattedDate);


        if (!formattedDate.equals(oldFormattedDate)) {
            pref.putString(dateKey, formattedDate);
            return true;
        }
        pref.putString(dateKey, formattedDate);
        return false;
    }

}