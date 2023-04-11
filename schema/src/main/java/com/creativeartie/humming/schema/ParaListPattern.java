package com.creativeartie.humming.schema;

import java.util.regex.*;

/**
 * Lines that are part a numbered/bullet lists.
 *
 * @see ParaBasicPatterns
 * @see ParaHeadingPattern
 * @see ParaNotePatterns
 * @see ParaReferencePatterns
 * @see ParaTableRowPattern
 * @see com.creativeartie.humming.document.Para#newLine where is used
 */
@SuppressWarnings("nls")
public enum ParaListPattern implements PatternEnum {
    /** Numbered line start pattern. */
    NUMBERED("\\#{1,6}"),
    /** Bullet line start pattern. */
    BULLET("\\-{1,6}"),
    /** List text pattern. */
    TEXT(TextFormattedPatterns.BASIC.getRawPattern()),
    /** Line ending pattern */
    ENDER("\n?");

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
        final Matcher matcher = matchPattern.matcher(text);
        if (matcher.find()) return matcher;
        return null;
    }

    private final String rawPattern;

    ParaListPattern(String pat) {
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
