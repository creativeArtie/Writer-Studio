package com.creativeartie.humming.schema;

import java.util.regex.*;

/**
 * Lines that can be grouped into a single note with heading and sources.
 *
 * @see IdentityReferencePattern#CITEREF
 */
public enum ParaNotePatterns implements PatternEnum {
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
    NOTE() {
        @Override
        protected String getValuePattern(boolean withName) {
            return // @formatter:off
                NoteLineParts.NOTE.getPattern(withName) +
                NoteLineParts.TEXT.getPattern(withName) + "?";
            //@formatter:on
        }
    },
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
    };

    public enum NoteLineParts implements PatternEnum {
        NOTE("%"), HEADING("%="), FIELD("%>"), FIELDER("="), TEXT(TextPhrasePatterns.BASIC.getRawPattern()),
        TITLE(TextPhrasePatterns.HEADING.getRawPattern()), IDER("#"), ID(IdentityPattern.getFullPattern()),
        ERROR(TextSpanPatterns.SIMPLE.getRawPattern()), KEY(TextSpanPatterns.KEY.getRawPattern()),
        VALUE(TextSpanPatterns.SIMPLE.getRawPattern()), ENDER("\n?");

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

    public Matcher matcher(String text) {
        if (matchPattern == null)
            matchPattern = Pattern.compile("^" + getValuePattern(true) + NoteLineParts.ENDER.getPattern(true) + "$");
        final Matcher match = matchPattern.matcher(text);

        if (match.find()) return match;
        return null;
    }

    protected abstract String getValuePattern(boolean withName);

    @Override
    public String getPatternName() {
        return name();
    }
}
