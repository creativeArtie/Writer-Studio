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
        if (test != null) return test;
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
                " is not before or on " + now + "(today): " + date);
        }
        throw new IllegalArgumentException(addBegin(field) + " is not before" +
            now + "(today): " + date);
    }

    public static int checkRange(int test, String field, int lower,
            boolean atLower, int upper, boolean atUpper){
        checkLessThan(test, field, upper, atUpper);
        checkGreater(test, field, lower, atLower);
        return test;
    }

    public static int checkGreater(int test, String field, int limit,
            boolean isInclude){
        if (limit < test) return test;
        if (isInclude) {
            if (limit == test) return test;
            throw new IllegalArgumentException(addBegin(field) +
                " is not greater than or equals to " + limit + ": " + test);
        }
        throw new IllegalArgumentException(addBegin(field) +
            " is not greater than " + limit + ": " + test);
    }

    public static int checkLessThan(int test, String field, int limit,
            boolean isInclude){
        if (limit > test) return test;
        if (isInclude) {
            if (limit == test) return test;
            throw new IllegalArgumentException(addBegin(field) +
                " is not less than or equals to " + limit + ": " + test);
        }
        throw new IllegalArgumentException(addBegin(field) +
            " is not less than " + limit + ": " + test);
    }

    public static int checkEqual(int test, String field, int size){
        if (size == test) return test;
            throw new IllegalArgumentException(addBegin(field) +
                " is not equals to " + size + ": " + test);
    }

    public static int[] checkIndexes(int lower, String field1, int upper,
            String field2 , int size){
        return checkIndexes(lower, field1, upper, field2, size, false);
    }

    public static int[] checkIndexes(int lower, String field1, int upper,
            String field2 , int size, boolean isInclude){
        checkIndex(upper, field1, size, isInclude);
        checkIndex(lower, field2, upper);
        return new int[]{lower, upper};
    }

    public static int checkIndex(int test, String field, int size){
        return checkIndex(test, field, size, false);
    }

    public static int checkIndex(int test, String field, int size,
            boolean isInclude){
        if (test >= 0 && test < size) return test;
        if (isInclude){
            if (test == size) return test;
            throw new ArrayIndexOutOfBoundsException(addBegin(field) +
                " cannot be below 0, above " + size + ", or equals " + size +
                    ": " + test);
        }
        throw new ArrayIndexOutOfBoundsException(addBegin(field) +
                " cannot be below 0 or above " + size  + ": " + test );
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
