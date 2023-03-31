package com.creativeartie.humming.schema;

import java.util.regex.*;

/**
 * Pointers for footnotes, endnotes, notes, meta data and errors. <br>
 * TODO Missing span version for meta data
 *
 * @see TextFormattedPatterns.TextFormattedParts as a span in a text line
 * @see ParaReferencePatterns address for footnote and endnote
 * @see ParaNotePatterns address for citations / note
 * @see com.creativeartie.humming.document.IdentityReference span counter part
 */
public enum IdentityReferencePattern implements PatternEnum {
    /** Pointer start pattern */
    START("\\{"),

    /** Footnote indicator pattern */
    FOOTREF("\\^"),
    /** Endnote indicator pattern */
    ENDREF("\\*"),
    /** Citation (aka note) indicator pattern */
    CITEREF("\\>"),
    /** Meta data indicator pattern */
    METAREF("\\%"),

    /** Identity pattern */
    ID(IdentityPattern.getFullPattern()),
    /** Error pattern */
    ERROR(TextSpanPatterns.SPECIAL.getRawPattern()),

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
        final Matcher answer = matchPattern.matcher(text);
        if (answer.find()) return answer;
        return null;
    }

    /** Get raw full pattern */
    static String getFullPattern() {
        if (fullPattern == null) fullPattern = getFullPattern(false);
        return fullPattern;
    }

    private static String getFullPattern(boolean withName) {
        return
        // @formatter:off
            START.getPattern(withName) + "((" +
                "(" +
                    FOOTREF.getPattern(withName) + "|" +
                    ENDREF.getPattern(withName) + "|" +
                    CITEREF.getPattern(withName) + "|" +
                    METAREF.getPattern(withName) +
                ")" + ID.getPattern(withName) +
                ")|(" + ERROR.getPattern(withName) + ")?" +
            ")" + END.getPattern(withName) + "?";
         // @formatter:on
    }

    private final String pattern;

    IdentityReferencePattern(String pat) {
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
