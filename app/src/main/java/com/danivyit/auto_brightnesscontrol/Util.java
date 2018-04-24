package com.danivyit.auto_brightnesscontrol;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

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

    /**
     * Stores a string onto the hard drive.
     * @param context The context of the application.
     * @param filename The name of the file to store to.
     * @param str The string to store.
     * @return True if successful.
     */
    static public boolean storeString(Context context, String filename, String str) {
        try {
            File file = new File(context.getFilesDir(), filename);
            FileWriter writer = new FileWriter(file);
            writer.write(str);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Stores a string onto the hard drive.
     * @param context The context of the application.
     * @param filename The name of the file to load from.
     * @return null if unsuccessful.
     */
    static public String loadString(Context context, String filename) {
        String str = "";
        try {
            File file = new File(context.getFilesDir(), filename);
            BufferedReader reader = new BufferedReader(new FileReader(file));
            // read file line by line
            String temp;
            StringBuilder builder = new StringBuilder();
            while (true) {
                temp = reader.readLine();
                if (temp == null) {
                    break;
                }
                builder.append(temp);
                builder.append("\n");
            }
            str = builder.toString();
        } catch (Exception e) {
            return null;
        }
        return str;
    }

}
