package com.mehealth.utilities;

import android.text.InputFilter;
import android.text.Spanned;

/**
 * Source: https://stackoverflow.com/questions/14212518/is-there-a-way-to-define-a-min-and-max-value-for-edittext-in-android
 * Filters input for editTexts in the app.
 */
public class InputFilterMinMax implements InputFilter {

    private int min;
    private int max;

    /**
     * @param min   Minimum number to be allowed.
     * @param max   Maximum number to be allowed.
     */
    public InputFilterMinMax(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        try {
            int input = Integer.parseInt(dest.toString() + source.toString());
            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException nfe) { }
        return "";
    }

    private boolean isInRange(int a, int b, int c) {
        /*if (b > a) {
            return c >= a && c <= b;
        } else {
            return c >= b && c <= a;
        }*/

        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}