package com.creativeartie.jwriter.main;

import java.io.*;
import java.time.*;
import java.util.*;

public final class Checker {

    public static void checkIO(boolean test, String massage)
            throws IOException{
        if (! test) throw new IOException(massage);
    }

    public static void checkArgument(boolean test, String massage){
        if (! test) throw new IllegalArgumentException(massage);
    }

    public static void checkState(boolean test, String massage){
        if (! test) throw new IllegalStateException(massage);
    }

    /**
     * Create {@linkplain NullPointerException} for a field.
     * @param <T>
     *      the type object to check
     * @param test
     *      the object to check for null.
     * @param field
     *      name of the field
     * @return the error String for null exceptions.
     */
    public static <T> T checkNotNull(T test, String field){
        if (test != null)return test;
        throw new IllegalArgumentException(addBegin(field) + " cannot be null.");
    }

    public static <T> Collection<T> checkNotEmpty(Collection<T> test,
            String field){
        checkNotNull(test, field);
        if (! test.isEmpty()){
            return test;
        }
        throw new IllegalArgumentException(addBegin(field) + " cannot be empty");
    }

    public static <T> T[] checkNotEmpty(T[] test, String field){
        checkNotNull(test, field);
        if (test.length > 0){
            return test;
        }
        throw new IllegalArgumentException(addBegin(field) + " cannot be empty");
    }

    public static <T extends CharSequence> T checkNotEmpty(T test, String field){
        checkNotNull(test, field);
        if (test.length() > 0){
            return test;
        }
        throw new IllegalArgumentException(addBegin(field) + " cannot be empty");
    }

    public static IllegalArgumentException typeNotUse(Object type,
        String field)
    {
        return new IllegalArgumentException("Argument \"" + field +
            "\" cannot be used: " + type);
    }

    public static LocalDate checkBeforeTodayDate(LocalDate date, String field,
            boolean isInclude){
        checkNotNull(date, field);
        LocalDate now = LocalDate.now();
        if (now.isEqual(date) && isInclude) return date;
        if (now.isBefore(date)) return date;
        if (isInclude) {
            if (now.isEqual(date)) return date;
            else throw new IllegalArgumentException(addBegin(field) +
                " is not before or on " + date + "(today)");
        }
        throw new IllegalArgumentException(addBegin(field) + " is not before" +
            date + "(today)");
    }

    public static int checkRange(int test, String field, int lower,
            boolean atLower, int upper, boolean atUpper){
        checkLessThan(test, field, lower, atLower);
        checkLessThan(test, field, upper, atUpper);
        return test;
    }

    public static int checkGreater(int test, String field, int upper,
            boolean isInclude){
        if (upper > test) return test;
        if (isInclude) {
            if (upper == test) return test;
            throw new IllegalArgumentException(addBegin(field) +
                " is not greater than or equals to " + upper);
        }
        throw new IllegalArgumentException(addBegin(field) +
            " is not greater than " + upper);
    }

    public static int checkLessThan(int test, String field, int lower,
            boolean isInclude){
        if (lower < test) return test;
        if (isInclude) {
            if (lower == test) return test;
            throw new IllegalArgumentException(addBegin(field) +
                " is not less than or equals to " + lower);
        }
        throw new IllegalArgumentException(addBegin(field) +
            " is not less than " + lower);
    }

    public static int[] checkIndexes(int lower, String field1, int upper,
            String field2 , int size){
        checkIndex(upper, field1, size);
        checkIndex(lower, field2, upper);
        return new int[]{lower, upper};
    }

    public static int checkIndex(int test, String field, int size){
        return checkIndex(test, field, size, false);
    }

    public static int checkIndex(int test, String field, int size,
            boolean isInclude){
        if (test > 0 && test < size) return test;
        if (isInclude){
            if (test == size) return test;
            throw new ArrayIndexOutOfBoundsException(addBegin(field) +
                " cannot be below 0 or above " + size);
        }
        throw new ArrayIndexOutOfBoundsException(addBegin(field) +
                " cannot be below 0 or above " + (size + 1));
    }

    public static int checkInt(int test, String field, int lower,
        boolean isLower, int upper, boolean isUpper)
    {
        String error = addBegin(field) + " is not between " +
            lower + " and " + upper + ": " + test;
        if (lower > test || (lower == test && ! isLower) ||
            upper < test || (upper == test && ! isUpper))
        {
            throw new IllegalArgumentException(error);
        }
        return test;
    }

    public static boolean[] checkArraySize(boolean[] test, String field,
        int size)
    {
        checkNotNull(test, field);
        String error = addBegin(field) + " does not have the right size: " +
                test.length;
        if (test.length != size){
            throw new IllegalArgumentException(error);
        }
        return test;
    }

    private static String addBegin(String forField){
        return "Parameter \"" + forField + "\"";
    }

    private Checker() {}
}
