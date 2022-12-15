package com.creativeartie.humming.schema;

import java.util.regex.*;

public enum NoteLinePatterns implements PatternEnum {
    HEADING() {
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
                NoteLineParts.SOURCE.getPattern(withName) +
                "((" +
                    NoteLineParts.FIELD.getPattern(withName) +
                    NoteLineParts.SOURCER.getPattern(withName) +
                    NoteLineParts.VALUE.getPattern(withName) +
                ")|" + NoteLineParts.ERROR.getPattern(withName) + ")";
            //@formatter:on
        }

    };

    enum NoteLineParts implements PatternEnum {
        STARTER("!"), NOTE("%"), HEADING("="), SOURCE(">"), SOURCER(":"),
        TEXT(FormattedPattern.getFullPattern(BasicTextPatterns.TEXT)),
        TITLE(FormattedPattern.getFullPattern(BasicTextPatterns.HEADING)),
        IDER("#"), ID(IdentityPattern.getFullPattern()),
        ERROR(BasicTextPatterns.TEXT.getRawPattern()),
        FIELD(BasicTextPatterns.CITE.getRawPattern()),
        VALUE(BasicTextPatterns.TEXT.getRawPattern());

        private String rawPattern;

        private NoteLineParts(String pattern) {
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
        if (rawPattern == null) {
            rawPattern = getValuePattern(false);
        }
        return null;
    }

    public Matcher matcher(String text) {
        if (matchPattern == null) {
            matchPattern = Pattern.compile("^" + getValuePattern(true) + "$");
        }
        Matcher match = matchPattern.matcher(text);

        if (match.find()) return match;
        return null;
    }

    protected abstract String getValuePattern(boolean withName);

    @Override
    public String getPatternName() {
        return name();
    }

}
