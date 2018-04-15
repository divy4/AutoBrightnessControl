package com.danivyit.auto_brightnesscontrol;

import android.content.res.Resources;
import android.util.TypedValue;

public class Util {

    /**
     * Formats a double to a string with a certain number of decimal points.
     * @param d A double
     * @param precision The number of decimal points to display.
     * @return A String
     */
    static public String formatDouble(double d, int precision) {
        String format = String.format("%%.%df", precision);
        return String.format(format, d);
    }

    /**
     * Maps the value of a SeekBar to [min, max].
     * @param in The input integer.
     * @param minIn The minimum input.
     * @param maxIn The maximum input.
     * @param minOut The maximum output.
     * @param maxOut The minimum output.
     * @return
     */
    static public double mapRange(double in, double minIn, double maxIn, double minOut, double maxOut) {
        return minOut + ((in - minIn) * (maxOut - minOut)) / (maxIn - minIn);
    }

    /**
     * Reads in a float value from R and converts it to a proper float.
     * @param resources A any context from the application.
     * @param rawValue The raw value from R, e.g. R.dimen.f.
     * @return The rawValue converted into a double.
     */
    static public double readRDouble(Resources resources, int rawValue) {
        TypedValue value = new TypedValue();
        resources.getValue(rawValue, value, true);
        return value.getFloat();
    }

    /**
     * Reads in a integer value from R and converts it to a proper integer.
     * @param resources A any context from the application.
     * @param rawValue The raw value from R, e.g. R.integer.i.
     * @return The rawValue converted into a int.
     */
    static public int readRInt(Resources resources, int rawValue) {
        TypedValue value = new TypedValue();
        return resources.getInteger(rawValue);
    }

}
