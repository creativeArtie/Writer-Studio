package com.creativeartie.writer.pdf.value;

enum RomanNumbering{
    LOWER("i", "v", "x", "l", "c", "d", "m"),
    SUPER("ⁱ", "ᵛ", "ˣ", "ˡ", "ᶜ", "ᵈ", "ᵐ");

    private RomanNumbering(String one, String five, String ten, String fifty,
            String hundred, String fiveHundred, String thousands){
        roman1 = one;
        roman5 = five;
        roman10 = ten;
        roman50 = fifty;
        roman100 = hundred;
        roman500 = fiveHundred;
        roman1000 = thousands;
    }

    private final String roman1;
    private final String roman5;
    private final String roman10;
    private final String roman50;
    private final String roman100;
    private final String roman500;
    private final String roman1000;

    public String toRoman(int num){
        StringBuilder ans = new StringBuilder();
        if (num > 1000){
            while (num > 1000){
                ans.append(roman1000);
                num -= 1000;
            }
        }
        if (num > 100){
            ans.append(getText(num / 100, roman1000, roman500, roman100));
            num %= 100;
        }
        if (num > 10){
            ans.append(getText(num / 10, roman100, roman50, roman10));
            num %= 10;
        }
        ans.append(getText(num, roman10, roman5, roman1));
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