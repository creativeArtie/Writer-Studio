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
        if (number % 100 >= 50){
            number += 99;
        }
        number = (number / 100) * 100;
        return number + "";
    }
}