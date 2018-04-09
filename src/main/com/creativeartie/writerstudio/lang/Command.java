package com.creativeartie.writerstudio.lang;

import java.util.*; // List

public interface Command{

    public static String escapeText(String text, String escape, String ... enders){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++){
            for (String ender: enders) {
                if (text.startsWith(ender, i)) {
                    builder.append(escape);
                    break;
                }
            }
            builder.append(text.charAt(i) + "");
        }
        return builder.toString();
    }

    public String getResult();
}