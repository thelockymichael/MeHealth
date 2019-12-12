package com.mehealth.utilities;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Filters input for editTexts in the app.
 * Source: https://stackoverflow.com/questions/55314321/how-to-limit-an-integer-value-entered-into-an-edittext-in-android-studio-java
 */
public class InputFilterMinMax implements InputFilter {
    private double minValue;
    private double maxValue;

    /**
     * @param minVal   Minimum number to be allowed.
     * @param maxVal   Maximum number to be allowed.
     */
    public InputFilterMinMax(double minVal, double maxVal) {
        this.minValue = minVal;
        this.maxValue = maxVal;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dStart, int dEnd) {
        try {
            // Remove the string out of destination that is to be replaced
            String newVal = dest.toString().substring(0, dStart) + dest.toString().substring(dEnd, dest.toString().length());
            newVal = newVal.substring(0, dStart) + source.toString() + newVal.substring(dStart, newVal.length());
            double input = Double.parseDouble(newVal);

            if (isInRange(minValue, maxValue, input)) {
                return null;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return "";
    }

    private boolean isInRange(double a, double b, double c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}