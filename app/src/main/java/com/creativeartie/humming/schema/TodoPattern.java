package com.creativeartie.humming.schema;

import java.util.regex.*;

import com.google.common.base.*;

public enum TodoPattern implements PatternEnum {
    START("\\{\\!"), TEXT(BasicTextPatterns.SPECIAL.getRawPattern()),
    END("\\}");

    private static String fullPattern;
    private static Pattern matchPattern;

    private static String getFullPattern(boolean withName) {
        return
        // @formatter:off
            START.getPattern(withName) +
            TEXT.getPattern(withName) + "?" +
            END.getPattern(withName) + "?"
            ;
         // @formatter:on
    }

    public static String getFullPattern() {
        if (fullPattern == null) {
            // @formatter:off
            fullPattern = getFullPattern(false);
             // @formatter:on
        }
        return fullPattern;
    }

    public static Matcher matcher(String text) {
        if (matchPattern == null) {
            matchPattern = Pattern.compile("^" + getFullPattern(true) + "$");
        }
        Matcher match = matchPattern.matcher(text);
        Preconditions.checkArgument(match.find(), "No pattern found.");
        return match;
    }

    private final String pattern;

    private TodoPattern(String pat) {
        pattern = pat;
    }

    @Override
    public String getRawPattern() {
        return pattern;
    }

    @Override
    public String getPatternName() {
        return name();
    }

}
