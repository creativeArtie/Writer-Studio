package com.creativeartie.humming.schema;

import java.util.regex.*;

/**
 * Lines that can be grouped into a single note with heading and sources.
 *
 * @see ParaBasicPatterns
 * @see ParaHeadingPattern
 * @see ParaListPattern
 * @see ParaReferencePatterns
 * @see ParaTableRowPattern
 * @see com.creativeartie.humming.document.Para#newLine where is used
 * @see IdentityReferencePattern pointer to citation / notes
 */
@SuppressWarnings("nls")
public enum ParaNotePatterns implements PatternEnum {
    /** Summary heading line with ID pattern. */
    SUMMARY() {
        @Override
        protected String getValuePattern(boolean withName) {
            return // @formatter:off
                NoteLineParts.HEADING.getPattern(withName) +
                NoteLineParts.TITLE.getPattern(withName)  + "?"+
                "(" +
                    NoteLineParts.IDER.getPattern(withName) +
                    "(" +
                        NoteLineParts.ID.getPattern(withName) + "|" +
                        NoteLineParts.ERROR.getPattern(withName) +
                    ")?" +
                ")?";
            //@formatter:on
        }
    },
    /** Field line pattern. */
    FIELD {
        @Override
        protected String getValuePattern(boolean withName) {
            return // @formatter:off
                NoteLineParts.FIELD.getPattern(withName) +
                "((" +
                    NoteLineParts.KEY.getPattern(withName) +
                    NoteLineParts.FIELDER.getPattern(withName) +
                    NoteLineParts.VALUE.getPattern(withName) +
                ")|" + NoteLineParts.ERROR.getPattern(withName) + ")";
            //@formatter:on
        }
    },
    /** Note detail line pattern. */
    NOTE() {
        @Override
        protected String getValuePattern(boolean withName) {
            return // @formatter:off
                NoteLineParts.NOTE.getPattern(withName) +
                NoteLineParts.TEXT.getPattern(withName) + "?";
            //@formatter:on
        }
    };

    /** Parts of a note */
    public enum NoteLineParts implements PatternEnum {
        /** Note heading starter pattern. */
        HEADING("%="),
        /** Note field starter pattern. */
        FIELD("%>"),
        /** Note detail starter pattern. */
        NOTE("%"),

        /** Note detail pattern. */
        TEXT(TextFormattedPatterns.BASIC.getRawPattern()),

        /** Note heading text pattern. */
        TITLE(TextFormattedPatterns.HEADING.getRawPattern()),
        /** Note ID marker pattern. */
        IDER("#"),
        /** Note Id pattern */
        ID(IdentityPattern.getFullPattern()),

        /** Note field key pattern. */
        KEY(TextSpanPatterns.KEY.getRawPattern()),

        /** Note field separator pattern. */
        FIELDER("="),
        /** Note field value pattern. */
        VALUE(TextSpanPatterns.SIMPLE.getRawPattern()),

        /** Note error pattern. */
        ERROR(TextSpanPatterns.SIMPLE.getRawPattern()),

        /** Line ending pattern */
        ENDER("\n?");

        private String rawPattern;

        NoteLineParts(String pattern) {
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

    private String rawPattern;
    private Pattern matchPattern;

    @Override
    public String getRawPattern() {
        if (rawPattern == null) rawPattern = getValuePattern(false) + NoteLineParts.ENDER.getPattern(false);
        return rawPattern;
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
            matchPattern = Pattern.compile("^" + getValuePattern(true) + NoteLineParts.ENDER.getPattern(true) + "$");
        final Matcher match = matchPattern.matcher(text);

        if (match.find()) return match;
        return null;
    }

    /**
     * Gets the note line special pattern.
     *
     * @param withName
     *        use name pattern?
     *
     * @return the note pattern.
     */
    abstract String getValuePattern(boolean withName);

    @Override
    public String getPatternName() {
        return name();
    }
}
