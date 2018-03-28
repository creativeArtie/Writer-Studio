package com.creativeartie.writerstudio.export.value;

public class Utilities{

    /** Converts inches to points. */
    public static float inchToPoint(float inches){
        return inches * 72;
    }

    /** Converts centimeters to points. */
    public static float cmToPoint(float cm){
        return cm * 28.3465f;
    }

    public static String toRomanSuperscript(int num){
        return RomanNumbering.SUPER.toRoman(num);
    }
}