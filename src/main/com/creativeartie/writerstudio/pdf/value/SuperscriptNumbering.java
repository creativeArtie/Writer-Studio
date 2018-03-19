package com.creativeartie.writerstudio.pdf.value;

public class SuperscriptNumbering{
    private final static String[] NUMBERS = new String[]{
        "⁰", "¹", "²", "³", "⁴", "⁵", "⁶", "⁷", "⁸", "⁹"};

    public static String toSuperscript(int number){
        String text = String.valueOf(number);
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++){
            builder.append(NUMBERS[Integer.parseInt(text.charAt(i) + "")]);
        }
        return builder.toString();
    }
}