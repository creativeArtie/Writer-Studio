package com.creativeartie.humming.schema;

import java.util.regex.*;

import com.google.common.base.*;

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
public enum ParaHeadingPattern implements PatternEnum {
    /** Outline start pattern. */
    OUTLINE("!"),
    /** Heading start + level pattern. */
    LEVEL("\\={1,6}"),

    /** Heading text pattern. */
    TEXT(TextFormattedPatterns.HEADING.getRawPattern()),

    /** Status text pattern. @see StatusPattern */
    STATUS("\\#[a-zA-Z]*"),
    /** Status detail pattern. */
    DETAILS(TextSpanPatterns.SIMPLE.getRawPattern()),

    /** Line ending pattern */
    ENDER("\n?");

    /** List of status pattern. */
    public enum StatusPattern {
        /** Stub status. */
        STUB,
        /** Outline status. */
        OUTLINE,
        /** Draft status. */
        DRAFT,
        /** Final status. */
        FINAL,
        /** Other status. */
        OTHERS;

        private static Pattern testPattern;

        /**
         * Get the status from text
         *
         * @param text
         *        the text to test
         *
         * @return the {@linkplain StatusPattern} found.
         */
        public static StatusPattern getStatus(String text) {
            if (testPattern == null) testPattern = Pattern.compile("\\#[^\\n]+");
            Preconditions.checkArgument(testPattern.matcher(text).find(), "Pattern not found");
            final String name = text.toUpperCase();
            for (final StatusPattern value : values()) if (name.startsWith("#" + value.name())) return value;
            return OTHERS;
        }
    }

    private final String rawPattern;

    private static String getFullPattern(boolean withName) {
        return
        // @formatter:off
            OUTLINE.getPattern(withName) + "?" +
            LEVEL.getPattern(withName) +
            TEXT.getPattern(withName) + "?" +
            "(" + STATUS.getPattern(withName) + DETAILS.getPattern(withName) + "?)?" +
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
