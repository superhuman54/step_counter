package com.navercorp.recruit.kimkihwan.stepcounter.util;

import java.text.DecimalFormat;

/**
 * Created by jamie on 10/3/16.
 */

public class UnitUtils {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.#");

    private static final String SUFFIX_METERS = " m";
    private static final String SUFFIX_KILOMETERS = " km";

    public static String forDisplay(double meters) {
        int integer = (int) Math.floor(meters);
        double value = integer / 1000;
        if (value < 1) {
            return new StringBuilder().append(DECIMAL_FORMAT.format(meters)).append(SUFFIX_METERS).toString();
        } else {
            return new StringBuilder().append(value).append(SUFFIX_KILOMETERS).toString();
        }
    }

}
