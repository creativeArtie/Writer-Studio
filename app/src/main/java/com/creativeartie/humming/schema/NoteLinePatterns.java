package com.creativeartie.humming.schema;

import java.util.regex.*;

/**
 * Lines that can be grouped into a single note with heading and sources.
 *
 * @see ReferencePattern#CITEREF
 */
public enum NoteLinePatterns implements PatternEnum {
    SUMMARY() {
        @Override
        protected String getValuePattern(boolean withName) {
            return // @formatter:off
                NoteLineParts.STARTER.getPattern(withName) +
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
                NoteLineParts.STARTER.getPattern(withName) +
                NoteLineParts.NOTE.getPattern(withName) +
                NoteLineParts.TEXT.getPattern(withName) + "?";
            //@formatter:on
        }
    },
    SOURCE {
        @Override
        protected String getValuePattern(boolean withName) {
            return // @formatter:off
                NoteLineParts.STARTER.getPattern(withName) +
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
        STARTER("!"), NOTE("%"), HEADING("%="), FIELD(">"), FIELDER(":"), TEXT(LineTextPatterns.BASIC.getRawPattern()),
        TITLE(LineTextPatterns.HEADING.getRawPattern()), IDER("#"), ID(IdentityPattern.getFullPattern()),
        ERROR(BasicTextPatterns.SIMPLE.getRawPattern()), KEY(BasicTextPatterns.CITE.getRawPattern()),
        VALUE(BasicTextPatterns.SIMPLE.getRawPattern()), ENDER("\n?");

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
