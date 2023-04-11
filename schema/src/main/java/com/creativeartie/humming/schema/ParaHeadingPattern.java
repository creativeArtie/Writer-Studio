package com.creativeartie.humming.schema;

import java.util.regex.*;

/**
 * Headings for print and to describe scenes in an outline
 *
 * @see ParaBasicPatterns
 * @see ParaListPattern
 * @see ParaNotePatterns
 * @see ParaReferencePatterns
 * @see ParaTableRowPattern
 * @see com.creativeartie.humming.document.Para#newLine where is used
 */
@SuppressWarnings("nls")
public enum ParaHeadingPattern implements PatternEnum {
    /** Outline start pattern. */
    OUTLINE("!"),
    /** Heading start + level pattern. */
    LEVEL("\\={1,6}"),

    /** Heading text pattern. */
    TEXT(TextFormattedPatterns.HEADING.getRawPattern()),

    /** Heading ID marker pattern. */
    IDER("\\#"),
    /** Heading ID pattern */
    ID(IdentityPattern.getFullPattern()),
    /** Header ID error pattern. */
    ERROR(TextSpanPatterns.SIMPLE.getRawPattern()),

    /** Line ending pattern */
    ENDER("\n?");

    private final String rawPattern;

    private static String getFullPattern(boolean withName) {
        return
        // @formatter:off
            OUTLINE.getPattern(withName) + "?" +
            LEVEL.getPattern(withName) +
            TEXT.getPattern(withName) + "?" +
            "(" + IDER.getPattern(withName) + "(" +
                 ID.getPattern(withName)+ "|" + ERROR.getPattern(withName) + "?" +
            "))?" +
            ENDER.getPattern(withName);
        // @formatter:on
    }

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
        final Matcher matcher = matchPattern.matcher(text);
        if (matcher.find()) return matcher;
        return null;
    }

    ParaHeadingPattern(String pattern) {
        rawPattern = pattern;
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
