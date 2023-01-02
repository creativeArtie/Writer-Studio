package com.creativeartie.humming.schema;

import java.util.regex.*;

/**
 * Lines that are part a numbered/bullet lists.
 */
public enum ListLinePattern implements PatternEnum {
    NUMBERED("\\#{1,6}"), BULLET("\\-{1,6}"), TEXT(TextLinePatterns.BASIC.getRawPattern()), ENDER("\n?");

    private static String fullPattern;
    private static Pattern matchPattern;

    private static String getFullPattern(boolean withName) {
        return
        // @formatter:off
            "(" +
                NUMBERED.getPattern(withName) + "|" +
                BULLET.getPattern(withName) +
            ")" + TEXT.getPattern(withName) + "?" +
            ENDER.getPattern(withName);
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
        final Matcher matcher = matchPattern.matcher(text);
        if (matcher.find()) return matcher;
        return null;
    }

    private final String rawPattern;

    ListLinePattern(String pat) {
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
}
