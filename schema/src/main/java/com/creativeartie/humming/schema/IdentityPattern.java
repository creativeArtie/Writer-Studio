package com.creativeartie.humming.schema;

import java.util.regex.*;

/**
 * Pattern for Identity. It contains nesting categories and and id.
 *
 * @see IdentityReferencePattern as pointer to an footnote, endnote, etc.
 * @see ParaNotePatterns.NoteLineParts as an address for notes
 * @see ParaReferencePatterns.RefLineParts as an address for footnote and
 *      endnote.
 * @see com.creativeartie.humming.document.IdentitySpan span counter part
 */
public enum IdentityPattern implements PatternEnum {
    /** Category and ID name pattern. */
    NAME(TextSpanPatterns.ID.getRawPattern()),
    /** Separator pattern. */
    SEP(":");

    private static String fullPattern;
    private static Pattern checkPattern;
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
        if (checkPattern == null) {
            checkPattern = Pattern.compile("^" + getFullPattern() + "$");
            final StringBuilder pattern = new StringBuilder();
            for (final PatternEnum value : IdentityPattern.values()) {
                if (!pattern.isEmpty()) pattern.append("|");
                pattern.append("(" + value.getNamedPattern() + ")");
            }
            matchPattern = Pattern.compile(pattern.toString());
        }
        if (checkPattern.matcher(text).find()) return matchPattern.matcher(text);

        return null;
    }

    /** Get raw full pattern. */
    static String getFullPattern() {
        if (fullPattern == null) fullPattern =
        // @formatter:off
            NAME.getRawPattern() +
            "(" +
                SEP.getRawPattern() +
                NAME.getRawPattern() +
            ")*";
         // @formatter:on
        return fullPattern;
    }

    private String pattern;

    IdentityPattern(String pat) {
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
