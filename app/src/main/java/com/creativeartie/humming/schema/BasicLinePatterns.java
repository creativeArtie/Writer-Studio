package com.creativeartie.humming.schema;

import java.util.regex.*;

import com.google.common.base.*;

public enum BasicLinePatterns implements PatternEnum {
    QUOTE("\\>"), AGENDA("!"), TEXT(FormattedPattern.TEXT.getRawPattern()),
    BREAK("\\*\\*\\*");

    private static String fullPattern;
    private static Pattern matchPattern;

    private static String getFullPattern(boolean withName) {
        return
        // @formatter:off
            "(" +
                "(" +
                    QUOTE.getPattern(withName) + "|" +
                    AGENDA.getPattern(withName) +
                ")?" + TEXT.getPattern(withName) + "?" +
           ")|" + BREAK.getPattern(withName);
        // @formatter:on
    }

    public static String getFullPattern() {
        if (fullPattern == null) {
            fullPattern = getFullPattern(false);
        }
        return fullPattern;
    }

    public static Matcher matcher(String text) {
        if (matchPattern == null) {
            matchPattern = Pattern.compile("^" + getFullPattern(true) + "$");
        }
        Matcher matcher = matchPattern.matcher(text);
        Preconditions.checkArgument(matcher.find(), "Pattern not match");
        return matcher;
    }

    private final String rawPattern;

    private BasicLinePatterns(String pat) {
        rawPattern = pat;
    }

    @Override
    public String getRawPattern() {
        return rawPattern;
    }

    @Override
    public String getPatternName() {
        return name();
    }

    @Override
    public boolean runFind() {
        return false;
    }

}
