package com.creativeartie.jwriter.export;

import java.util.*;
import java.io.*;

public interface Exporter extends Closeable{
    public static String toRomanCounter(int num){
        LinkedList<Integer> digits = new LinkedList<>();
        while (num != 0){
            digits.push(num % 26);
            num /= 26;
        }
        StringBuilder builder = new StringBuilder();
        if (! digits.isEmpty()){
            int digit = digits.pop();
            builder.append((char) (digit + 'a') + "");
        }
        return builder.toString();
    }

    public void parse();
}