package com.creativeartie.jwriter.main;

public class Checker {

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
        String error = addBegin(field) + "can not be null.";
        if (test == null){
            throw new IllegalArgumentException(error);
        }
        return test;
    }
    
    public static IllegalArgumentException typeNotUse(Object type, 
        String field)
    {
        return new IllegalArgumentException("Argument \"" + field + 
            "\" can not be used: " + type);
    }
    
    public static int checkInt(int test, String field, int lower, 
        boolean isLower, int upper, boolean isUpper)
    {
        String error = addBegin(field) + "is not between " + 
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
        String error = addBegin(field) + "does not have the right size: " +
                test.length;
        if (test.length != size){
            throw new IllegalArgumentException(error);
        }
        return test;
    }
    
    private static String addBegin(String forField){
        return "Argument \"" + forField + "\" ";
    }
    
    private Checker() {}
}
