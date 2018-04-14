package com.creativeartie.writerstudio.export.value;

import java.text.*; // DecimalFormat

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

    private final static String[] NUMBERS = new String[]{
        "⁰", "¹", "²", "³", "⁴", "⁵", "⁶", "⁷", "⁸", "⁹"};

    public static String toNumberSuperscript(int number){
        String text = String.valueOf(number);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++){
            builder.append(NUMBERS[Integer.parseInt(text.charAt(i) + "")]);
        }
        return builder.toString();
    }

    public static String round(int number){
        return round(number, (int)Math.log10(number));
    }

    public static String round(int number, int digits){
        int min = (int) Math.pow(10, digits);
        if (number < min / 2){
            return "< " + addCommas(min);
        }
        if (number % min >= (min / 2)){
            number += (min - 1);
        }
        number = (number / min) * min;
        return addCommas(number);

    }

    private static String addCommas(int number){
        return new DecimalFormat("#,###").format(number);

    }
}