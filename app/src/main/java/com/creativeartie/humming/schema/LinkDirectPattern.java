package com.creativeartie.humming.schema;

import java.util.regex.*;

import com.google.common.base.*;

public enum LinkDirectPattern implements PatternEnum {
    START("\\{\\@@"), LINK(BasicTextPatterns.LINK.getRawPattern()), SEP("\\|"),
    TEXT(BasicTextPatterns.SPECIAL.getRawPattern()), END("\\}");

    private static String fullPattern;
    private static Pattern basePattern;

    private static String getFullPattern(boolean withName) {
        return
        // @formatter:off
            START.getPattern(withName) +
            LINK.getPattern(withName) + "(" +
                SEP.getPattern(withName) + TEXT.getPattern(withName) + "?" +
            ")?" +
            END.getPattern(withName) + "?"
            ;
         // @formatter:on
    }

    public static String getFullPattern() {
        if (fullPattern == null) {
            fullPattern = getFullPattern(false);
        }
        return fullPattern;
    }

    public static Matcher matcher(String text) {
        if (basePattern == null) {
            basePattern = Pattern.compile("^" + getFullPattern(true) + "$");
        }
        Matcher match = basePattern.matcher(text);
        Preconditions.checkArgument(match.find(), "Pattern does not match");
        return match;
    }

    private final String pattern;

    private LinkDirectPattern(String pat) {
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
