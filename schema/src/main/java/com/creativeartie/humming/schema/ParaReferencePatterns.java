package com.creativeartie.humming.schema;

import java.util.regex.*;

/**
 * Lines that describes footnotes, end notes, and images.
 *
 * @see ParaBasicPatterns
 * @see ParaHeadingPattern
 * @see ParaListPattern
 * @see ParaNotePatterns
 * @see ParaTableRowPattern
 * @see com.creativeartie.humming.document.Para#newLine where is used
 * @see IdentityReferencePattern pointers of footnotes and endnotes
 */
@SuppressWarnings("nls")
public enum ParaReferencePatterns implements PatternEnum {
    /** Footnote line pattern. */
    FOOTNOTE {
        @Override
        protected String getValuePattern(boolean withName) {
            return // @formatter:off
                RefLineParts.START.getPattern(withName) +
                RefLineParts.FOOTNOTE.getPattern(withName) +
                "(" +
                    "(" +
                        RefLineParts.ID.getPattern(withName) +
                        RefLineParts.SEP.getPattern(withName) +
                        RefLineParts.TEXT.getPattern(withName) +
                    ")|" + RefLineParts.ERROR.getPattern(withName) +
                ")";
         // @formatter:on
        }
    },
    /** Endnote line pattern. */
    ENDNOTE {
        @Override
        protected String getValuePattern(boolean withName) {
            return // @formatter:off
                RefLineParts.START.getPattern(withName) +
                RefLineParts.ENDNOTE.getPattern(withName) +
                "(" +
                    "(" +
                        RefLineParts.ID.getPattern(withName) +
                        RefLineParts.SEP.getPattern(withName) +
                        RefLineParts.TEXT.getPattern(withName) +
                    ")|" + RefLineParts.ERROR.getPattern(withName) +
                ")";
         // @formatter:on
        }
    },
    /** Image line pattern. */
    IMAGE {
        @Override
        protected String getValuePattern(boolean withName) {
            return // @formatter:off
                    RefLineParts.START.getPattern(withName) +
                    RefLineParts.IMAGE.getPattern(withName) +
                    "(" +
                        "(" +
                            RefLineParts.ID.getPattern(withName) +
                            "(" +
                                RefLineParts.SEP.getPattern(withName) +
                                RefLineParts.TEXT.getPattern(withName) +
                            ")?" +
                        ")|" + RefLineParts.ERROR.getPattern(withName) +
                    ")";
             // @formatter:on
        }
    };

    /** Parts of a reference */
    public enum RefLineParts implements PatternEnum {
        /** Reference starter pattern. */
        START("!"),
        /** Footnote indicator pattern. */
        FOOTNOTE("\\^"),
        /** Endnote indicator pattern. */
        ENDNOTE("\\*"),
        /** Image indicator pattern. */
        IMAGE("\\+"),

        /** Pointer / Address ID pattern. */
        ID(IdentityPattern.getFullPattern()),
        /** ID separator pattern. */
        SEP("="),
        /** Note text / caption pattern */
        TEXT(TextFormattedPatterns.NOTE.getRawPattern()),

        /** error text pattern */
        ERROR(TextSpanPatterns.SIMPLE.getRawPattern()),
        /** Line ending pattern */
        ENDER("\n?");

        private final String rawPattern;

        RefLineParts(String pattern) {
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

    /**
     * Gets the note line special pattern.
     *
     * @param withName
     *        use name pattern?
     *
     * @return the note pattern.
     */
    protected abstract String getValuePattern(boolean withName);

    private String fullPattern;
    private Pattern matchPattern;

    @Override
    public String getRawPattern() {
        if (fullPattern == null) fullPattern = getValuePattern(false) + RefLineParts.ENDER.getPattern(false);
        return fullPattern;
    }

    /**
     * Match text to this pattern
     *
     * @param text
     *        the text to match
     *
     * @return Matcher of null if not matched
     */
    public Matcher matcher(String text) {
        if (matchPattern == null)
            matchPattern = Pattern.compile("^" + getValuePattern(true) + RefLineParts.ENDER.getPattern(true) + "$");
        final Matcher match = matchPattern.matcher(text);

        if (match.find()) return match;
        return null;
    }

    @Override
    public String getPatternName() {
        return name();
    }
}
