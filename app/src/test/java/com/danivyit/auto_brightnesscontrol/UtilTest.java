package com.danivyit.auto_brightnesscontrol;

import android.content.res.Resources;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class UtilTest extends Util {

    /**
     *
     * @param expected
     */
    public void assertSimilar(double expected, double actual, double error) {
        double absDiff = Math.abs(expected - actual);
        assert(absDiff < error);
    }

    @Test
    public void formatDouble() {
        double d = 0.123456789;

        String s0 = "0";
        String s1 = "0.1";
        String s2 = "0.12";
        String s3 = "0.123";
        String s4 = "0.1235";
        String s5 = "0.12346";

        assertEquals(s0, Util.formatDouble(d, 0));
        assertEquals(s1, Util.formatDouble(d, 1));
        assertEquals(s2, Util.formatDouble(d, 2));
        assertEquals(s3, Util.formatDouble(d, 3));
        assertEquals(s4, Util.formatDouble(d, 4));
        assertEquals(s5, Util.formatDouble(d, 5));
    }

    @Test
    public void mapSeekValue() {
        double err = 1e-12;
        int iStart = 0;
        int iMid = 1;
        int iEnd = 2;
        double dStart = 4.0;
        double dMid = 4.5;
        double dEnd = 5.0;

        assertSimilar(dStart, Util.mapRange(iStart, iStart, iEnd, dStart, dEnd), err);
        assertSimilar(dMid,   Util.mapRange(iMid,   iStart, iEnd, dStart, dEnd), err);
        assertSimilar(dEnd,   Util.mapRange(iEnd,   iStart, iEnd, dStart, dEnd), err);
    }

}