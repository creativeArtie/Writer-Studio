package com.creativeartie.writerstudio.main;

import java.io.*;
import java.time.*;
import java.util.*;

import com.google.common.collect.*;

public final class ParameterChecker {

    public static void argumentCheck(boolean test, String message){
        if (! test) throw new IllegalArgumentException(message);
    }

    private static int argumentCheck(int index, String field,
            Range<Integer> range){
        argumentCheck(range.contains(index), outOfRange(index, field, range));
        return index;
    }

    public static void ioCheck(boolean test, String message) throws IOException{
        if (! test) throw new IOException(message);
    }

    public static void stateCheck(boolean test, String message){
        if (! test) throw new IllegalStateException(message);
    }

    private static int indexCheck(int test, String field, Range<Integer> range){
        if (! range.contains(test)) throw new IndexOutOfBoundsException(
            outOfRange(test, field, range));
        return test;
    }

    public static <T> T argumentEquals(T test, String field, T expect){
        String message = createStart(field) + " does not equals " + expect;
        if (expect == null){
            argumentCheck(test != null, message);
        }
        argumentCheck(expect.equals(test), message);
        return test;
    }

    public static <T> T argumentSame(T item, String field, T expect){
        argumentCheck(item == expect, createStart(field) +
            " is not the same as " + expect);
        return item;
    }

    public static <T> T argumentNotNull(T item, String field){
        argumentCheck(item != null, createStart(field) + " is null");
        return item;
    }

    public static <T> Collection<T> argumentNotEmpty(Collection<T> item,
            String field){
        argumentNotNull(item, field);
        argumentCheck(! item.isEmpty(), createStart(field) + " is empty");
        return item;
    }

    public static <T> T[] argumentNotEmpty(T[] item, String field){
        argumentNotNull(item, field);
        argumentCheck(item.length != 0, createStart(field) + " is empty");
        return item;
    }

    public static <T extends CharSequence> T argumentNotEmpty(T item,
            String field){
        argumentNotNull(item, field);
        argumentCheck(item.length() != 0, createStart(field) + " is empty");
        return item;
    }

    public static IllegalArgumentException argumentNotAccept(Object obj,
            String field) {
        return new IllegalArgumentException(createStart(field) +
            " does not accept " + obj);
    }

    public static int argumentOpen(int test, String field, int min, int max){
        return argumentCheck(test, field, Range.open(min, max));
    }

    public static int indexOpen(int test, String field, int min, int max){
        return indexCheck(test, field, Range.open(min, max));
    }

    public static int argumentClose(int test, String field, int min, int max){
        return argumentCheck(test, field, Range.closed(min, max));
    }

    public static int indexClose(int test, String field, int min, int max){
        return indexCheck(test, field, Range.closed(min, max));
    }

    public static int argumentCloseOpen(int test, String field, int min, int max){
        return argumentCheck(test, field, Range.closedOpen(min, max));
    }

    public static int indexCloseOpen(int test, String field, int min, int max){
        return indexCheck(test, field, Range.closedOpen(min, max));
    }

    public static int argumentOpenClose(int test, String field, int min, int max){
        return argumentCheck(test, field, Range.openClosed(min, max));
    }

    public static int indexOpenClose(int test, String field, int min, int max){
        return indexCheck(test, field, Range.openClosed(min, max));
    }

    public static int argumentGreaterThan(int test, String field, int min){
        return argumentCheck(test, field, Range.greaterThan(min));
    }

    public static int indexGreaterThan(int test, String field, int min){
        return indexCheck(test, field, Range.greaterThan(min));
    }

    public static int argumentAtLeast(int test, String field, int min){
        return argumentCheck(test, field, Range.atLeast(min));
    }

    public static int indexAtLeast(int test, String field, int max){
        return indexCheck(test, field, Range.atLeast(max));
    }

    public static int argumentLessThan(int test, String field, int max){
        return argumentCheck(test, field, Range.lessThan(max));
    }

    public static int indexLessThan(int test, String field, int min){
        return indexCheck(test, field, Range.lessThan(min));
    }

    public static int argumentAtMost(int test, String field, int min){
        return argumentCheck(test, field, Range.atMost(min));
    }

    public static int indexAtMost(int test, String field, int min){
        return indexCheck(test, field, Range.atMost(min));
    }

    private static String createStart(String field){
        return "Parameter \"" + field + "\"";
    }

    private static String outOfRange(int num, String field,
            Range<Integer> range){
        return createStart(field) + "(" + num + ") is not in range of " +
            range.toString() + ".";
    }
}