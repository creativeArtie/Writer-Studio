package com.creativeartie.writerstudio.main;

import java.io.*; // IOException
import java.util.*; // Collection;

import com.google.common.collect.*; // Range

public final class ParameterChecker {

    public static void argumentCheck(boolean test, String field, String postfix){
        field = createStart(field);
        if (! test) throw new IllegalArgumentException(field + postfix);
    }

    private static int argumentCheck(int index, String field,
            Range<Integer> range){
        argumentCheck(range.contains(index), field, outOfRange(index, range));
        return index;
    }

    public static void ioCheck(boolean test, String message)
            throws IOException{
        if (! test) throw new IOException(message);
    }

    public static void stateCheck(boolean test, String message){
        if (! test) throw new IllegalStateException(message);
    }
    
    public static <T> T argumentClass(Object obj, String field, 
			Class<T> clazz) {
		argumentCheck(clazz.isInstance(obj), field, obj.getClass() + 
			" is not an instance of " + clazz);
		return clazz.cast(obj);
	}

    public static IllegalStateException stateBuild(String message){
        return new IllegalStateException(message);
    }

    @SafeVarargs /// Just one == test
    public static <T extends Enum<T>> T argumentNotState(T test, String field,
            T ... nots){
        argumentNotNull(test, field);
        for (T not : nots){
            argumentCheck(test != not, field, " can not be " + not);
        }
        return test;
    }

    @SafeVarargs /// Just one == test
    public static <T extends Enum<T>> T argumentRequireState(T test,
        String field, T ... requires){
        argumentNotNull(test, field);
        for (T require: requires){
            if (require == test){
                return test;
            }
        }
        argumentCheck(false, field, " is not one accepted enums.");
        return test;
    }

    private static int indexCheck(int test, String field, Range<Integer> range){
        if (! range.contains(test)) {
            throw new IndexOutOfBoundsException(
                createStart(field) + outOfRange(test, range)
            );
        }
        return test;
    }

    public static <T> T argumentEquals(T test, String field, T expect){
        String message = " does not equals " + expect;
        if (expect == null){
            argumentCheck(test != null, field, message);
        }
        argumentCheck(expect.equals(test), field, message);
        return test;
    }

    public static <T> T indexEquals(T test, String field, T expect){
        String message = " does not equals " + expect;
        if (expect == null){
            argumentCheck(test != null, field, message);
        }
        if (test != expect){
            throw new IndexOutOfBoundsException(createStart(field) +
                "(" + test + ") is not " + expect + ".");
        }
        return test;
    }

    public static <T> T argumentSame(T item, String field, T expect){
        argumentCheck(item == expect, field, " is not the same as " + expect);
        return item;
    }

    public static <T> T argumentNotNull(T item, String field){
        argumentCheck(item != null, field, " is null");
        return item;
    }

    public static <C extends Collection<T>, T> C argumentNotEmpty(C item,
            String field){
        argumentNotNull(item, field);
        argumentCheck(! item.isEmpty(), field, " is empty");
        return item;
    }

    public static <T> T[] argumentNotEmpty(T[] item, String field){
        argumentNotNull(item, field);
        argumentCheck(item.length != 0, field, " is empty");
        return item;
    }

    public static <T extends CharSequence> T argumentNotEmpty(T item,
            String field){
        argumentNotNull(item, field);
        argumentCheck(item.length() != 0, field, " is empty");
        return item;
    }

    public static IllegalArgumentException argumentNotAccept(Object obj,
            String field) {
        return new IllegalArgumentException(createStart(field) +
            " does not accept " + obj);
    }

    public static <T> T argumentIsInstance(Object obj, String field,
            Class<T> type){
        argumentNotNull(obj, field);
        argumentCheck(type.isInstance(obj), field, " is not an instance of " +
            type);
        return type.cast(obj);
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

    private static String outOfRange(int num, Range<Integer> range){
        return " (" + num + ") is not in range of " + range.toString() + ".";
    }
}
