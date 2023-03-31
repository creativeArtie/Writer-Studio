package com.creativeartie.humming.schema;

import java.util.regex.*;

import com.google.common.base.*;

/**
 * A placeholder or todo phrase.
 *
 * @see ParaBasicPatterns#AGENDA line counter part
 * @see TextFormattedPatterns.TextFormattedParts as a span in a text line
 */
public enum IdentityTodoPattern implements PatternEnum {
    /** Pointer start pattern */
    START("\\{\\!"),
    /** Agenda text pattern */
    TEXT(TextSpanPatterns.SPECIAL.getRawPattern()),
    /** Pointer end pattern */
    END("\\}");

    private static String fullPattern;
    private static Pattern matchPattern;

    /**
     * Match text to this pattern
     *
     * @param text
     *        the text to match
     *
     * @return Matcher of null if not matched
     */
    public static Matcher matcher(String text) {
        if (matchPattern == null) matchPattern = Pattern.compile("^" + getFullPattern(true) + "$");
        final Matcher match = matchPattern.matcher(text);
        Preconditions.checkArgument(match.find(), "No pattern found.");
        return match;
    }

    /** get raw full pattern */
    static String getFullPattern() {
        if (fullPattern == null) fullPattern = getFullPattern(false);
        return fullPattern;
    }

    private static String getFullPattern(boolean withName) {
        return
        // @formatter:off
            START.getPattern(withName) +
            TEXT.getPattern(withName) + "?" +
            END.getPattern(withName) + "?"
            ;
         // @formatter:on
    }

    private final String pattern;

    IdentityTodoPattern(String pat) {
        pattern = pat;
    }

    @Override
    public String getPatternName() {
        return name();
    }

    @Override
    public String getRawPattern() {
        return pattern;
    }
}
