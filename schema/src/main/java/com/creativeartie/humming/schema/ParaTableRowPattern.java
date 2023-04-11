package com.creativeartie.humming.schema;

import java.util.regex.*;

/**
 * A table row
 *
 * @see ParaBasicPatterns
 * @see ParaHeadingPattern
 * @see ParaListPattern
 * @see ParaNotePatterns
 * @see ParaReferencePatterns
 * @see com.creativeartie.humming.document.Para#newLine where is used
 * @see IdentityReferencePattern pointers of footnotes and endnotes
 */
@SuppressWarnings("nls")
public enum ParaTableRowPattern implements PatternEnum {
    /** Table cell pattern. */
    TEXT(TextSpanPatterns.CELL.getRawPattern()),
    /** Table cell separator pattern. */
    SEP("\\|"),
    /** Line ending pattern */
    END("\\|?\\n?"),
    /** Empty table row pattern. */
    EMPTY("\\|\\n?");

    private static Pattern matchPattern;
    private static Pattern checkPattern;

    private static String getFullPattern(boolean withName) {
        return
        // @formatter:off
            "(" +
                "(" +
                    SEP.getPattern(withName) +
                    TEXT.getPattern(withName) +
                ")+" + END.getPattern(withName) +
            ")|" + EMPTY.getPattern(withName);
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
        if (matchPattern == null) {
            checkPattern = Pattern.compile("^" + getFullPattern(true) + "$");
            matchPattern = Pattern.compile(
            // @formatter:off
                "(" +
                    SEP.getPattern(true) +
                    TEXT.getPattern(true) +
                ")|" + END.getPattern(true));
            // @formatter:on
        }
        final Matcher match = checkPattern.matcher(text);
        if (!match.find()) return null;
        return matchPattern.matcher(text);
    }

    private String rawPattern;

    ParaTableRowPattern(String pattern) {
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
