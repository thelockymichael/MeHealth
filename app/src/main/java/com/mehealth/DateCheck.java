package com.mehealth;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateCheck {

    public DateCheck() {
    }

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