package com.creativeartie.writerstudio.pdf.value;

public class TextNumbering{
    private static String ONE = "i";
    private static String FIVE = "v";
    private static String TEN = "x";
    private static String FIFTY = "l";
    private static String HUNDRED = "c";
    private static String FIVE_HUNDRED = "d";
    private static String THOUSANDS = "m";

    public static String toRomanLower(int num){
        StringBuilder ans = new StringBuilder();
        if (num > 1000){
            while (num > 1000){
                ans.append("m");
                num -= 1000;
            }
        }
        if (num > 100){
            ans.append(getText(num / 100, THOUSANDS, FIVE_HUNDRED, HUNDRED));
            num %= 100;
        }
        if (num > 10){
            ans.append(getText(num / 10, HUNDRED, FIFTY, TEN));
            num %= 10;
        }
        ans.append(getText(num, TEN, FIVE, ONE));
        return ans.toString();
    }

    private static String getText(int digit, String highest, String middle,
            String smallest){
        if (digit == 10){
            return highest;
        }
        if (digit == 9){
            return smallest + highest;
        }
        if (digit == 5){
            return middle;
        }
        if (digit == 4){
            return smallest + middle;
        }
        StringBuilder builder = new StringBuilder();
        if (digit > 5){
            builder.append(middle);
            digit -= 5;
        }
        for (int i = 0; i < digit; i++){
            builder.append(smallest);
        }
        return builder.toString();
    }

}